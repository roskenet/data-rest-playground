package de.zalando.playground;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
//@EnableHypermediaSupport(type = { HypermediaType.HAL })
public class ManufacturerRepositoryTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testWithCamelCase() {
        Assert.assertEquals("BMW", getByURL("/manufacturers?foundedYear=1890").getName()); 
    }
    
    @Test
    public void testWithSnakeCase() {
        Assert.assertEquals("BMW", getByURL("/manufacturers?founded_year=1890&name=BMW").getName()); 
    }
    
    @Test(expected = IndexOutOfBoundsException.class) 
    public void testEmptyResult() {
        getByURL("/manufacturers?founded_year=1920");
    }

    private Manufacturer getByURL(String url) {
        ResponseEntity<PagedResources<Manufacturer>> response =
                restTemplate.exchange(url,
                            HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<Manufacturer>>() {
                    });
        return (Manufacturer) response.getBody().getContent().toArray()[0];
    }
    
}
