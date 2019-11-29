package com.orange.spring.analysis.note.InstaniationAwareBeanPostProcessor;

import com.orange.spring.analysis.note.BeanPostProcessor.Bean;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * @author xieyong
 * @date 2019/11/29
 * @Description:
 */
@SpringBootTest
public class InstantiationAwareBeanPostProcessoTest extends AbstractJUnit4SpringContextTests {

    @Test
    public void test(){
        Bean bean = applicationContext.getBean("bean", Bean.class) ;
        System.out.println(bean);
    }
}
