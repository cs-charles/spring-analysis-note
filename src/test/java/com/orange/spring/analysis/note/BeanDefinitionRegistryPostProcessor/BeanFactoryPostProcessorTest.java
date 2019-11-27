package com.orange.spring.analysis.note.BeanDefinitionRegistryPostProcessor;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@SpringBootTest
public class BeanFactoryPostProcessorTest extends AbstractJUnit4SpringContextTests {

    @Test
    public void test(){
        RegistryBean registryBean = applicationContext.getBean("registryBean", RegistryBean.class) ;
        System.out.println("registryBean :" + JSON.toJSONString(registryBean));
    }
}