package org.springframework.data.rest.webmvc.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.core.MethodParameter;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.QuerydslRepositoryInvokerAdapter;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.QuerydslBindingsFactory;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.querydsl.binding.QuerydslPredicateBuilder;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.repository.support.RepositoryInvoker;
import org.springframework.data.repository.support.RepositoryInvokerFactory;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.google.common.base.CaseFormat;
import com.querydsl.core.types.Predicate;

class QuerydslAwareRootResourceInformationHandlerMethodArgumentResolver
        extends RootResourceInformationHandlerMethodArgumentResolver {

    private final Repositories repositories;
    private final QuerydslPredicateBuilder predicateBuilder;
    private final QuerydslBindingsFactory factory;

    /**
     * Creates a new
     * {@link QuerydslAwareRootResourceInformationHandlerMethodArgumentResolver}
     * using the given {@link Repositories}, {@link RepositoryInvokerFactory}
     * and {@link ResourceMetadataHandlerMethodArgumentResolver}.
     * 
     * @param repositories
     *            must not be {@literal null}.
     * @param invokerFactory
     *            must not be {@literal null}.
     * @param resourceMetadataResolver
     *            must not be {@literal null}.
     */
    public QuerydslAwareRootResourceInformationHandlerMethodArgumentResolver(Repositories repositories,
            RepositoryInvokerFactory invokerFactory,
            ResourceMetadataHandlerMethodArgumentResolver resourceMetadataResolver,
            QuerydslPredicateBuilder predicateBuilder, QuerydslBindingsFactory factory) {

        super(repositories, invokerFactory, resourceMetadataResolver);

        this.repositories = repositories;
        this.predicateBuilder = predicateBuilder;
        this.factory = factory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.rest.webmvc.config.
     * RootResourceInformationHandlerMethodArgumentResolver#postProcess(org.
     * springframework.data.repository.support.RepositoryInvoker,
     * java.lang.Class, java.util.Map)
     */
    @Override
    @SuppressWarnings({ "unchecked" })
    protected RepositoryInvoker postProcess(MethodParameter parameter, RepositoryInvoker invoker, Class<?> domainType,
            Map<String, String[]> parameters) {

        Object repository = repositories.getRepositoryFor(domainType);

        Map<String, String[]> myMap = new HashMap<>(parameters);

        for (String key : myMap.keySet()) {
            if (key.contains("_")) {
                String camelCase = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key);
                myMap.put(camelCase, parameters.get(key));
            }
        }

        Iterator<Entry<String,String[]>> it = myMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, String[]> entry = it.next();
            if(entry.getKey().contains("_")){
                it.remove();
            }
        }

//        System.out.println("MEINER");

        if (!QueryDslPredicateExecutor.class.isInstance(repository)
                || !parameter.hasParameterAnnotation(QuerydslPredicate.class)) {
            return invoker;
        }

        ClassTypeInformation<?> type = ClassTypeInformation.from(domainType);

        QuerydslBindings bindings = factory.createBindingsFor(null, type);
        Predicate predicate = predicateBuilder.getPredicate(type, toMultiValueMap(myMap), bindings);

        return new QuerydslRepositoryInvokerAdapter(invoker, (QueryDslPredicateExecutor<Object>) repository, predicate);
    }

    /**
     * Converts the given Map into a {@link MultiValueMap}.
     * 
     * @param source
     *            must not be {@literal null}.
     * @return
     */
    private static MultiValueMap<String, String> toMultiValueMap(Map<String, String[]> source) {

        MultiValueMap<String, String> result = new LinkedMultiValueMap<String, String>();

        for (Entry<String, String[]> entry : source.entrySet()) {
            result.put(entry.getKey(), Arrays.asList(entry.getValue()));
        }

        return result;
    }
}