package de.zalando.playground;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ManufacturerRepository extends PagingAndSortingRepository<Manufacturer, Integer> , QueryDslPredicateExecutor<Manufacturer>{

}
