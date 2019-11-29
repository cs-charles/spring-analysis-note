package com.orange.spring.analysis.note.BeanPostProcessor;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@SpringBootTest
public class BeanFactoryPostProcessorTest extends AbstractJUnit4SpringContextTests {

    @Test
    public void test(){
        Bean bean = applicationContext.getBean("bean", Bean.class) ;
        System.out.println(bean);
    }
}