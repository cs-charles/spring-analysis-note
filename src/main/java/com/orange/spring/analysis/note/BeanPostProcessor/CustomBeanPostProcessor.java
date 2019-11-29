package com.orange.spring.analysis.note.BeanPostProcessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author xieyong
 * @date 2019/11/29
 * @Description:
 */
@Component
public class CustomBeanPostProcessor implements BeanPostProcessor {

    @Nullable
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("LogicBeanPostProcessor.postProcessAfterInitialization 执行啦 beanName = " + beanName);
        return bean;
    }

    @Nullable
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("LogicBeanPostProcessor.postProcessBeforeInitialization 执行啦 beanName = " + beanName);
        return bean;
    }
}
