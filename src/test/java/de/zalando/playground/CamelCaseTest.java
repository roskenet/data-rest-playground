package de.zalando.playground;

import org.junit.Test;

import com.google.common.base.CaseFormat;

public class CamelCaseTest {

    @Test
    public void test(){
        String result = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "founded_year");
        System.out.println(result);
    }
}
