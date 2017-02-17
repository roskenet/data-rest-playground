package de.zalando.playground;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.DEFINED_PORT)
public class DataRestPlaygroundApplicationTests {

    @LocalServerPort
    private int port;

    @Test
    public void contextLoads() throws InterruptedException {
        System.out.println("PORT : " + port);
//        TimeUnit.MINUTES.sleep(3);
    }

}
