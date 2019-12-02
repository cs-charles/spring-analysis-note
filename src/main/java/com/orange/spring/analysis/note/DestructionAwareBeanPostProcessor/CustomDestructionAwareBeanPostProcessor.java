package com.orange.spring.analysis.note.DestructionAwareBeanPostProcessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;

/**
 * @author xieyong
 * @date 2019/12/2
 * @Description:
 */
public class CustomDestructionAwareBeanPostProcessor implements DestructionAwareBeanPostProcessor {
    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
        System.out.println(beanName+"bean对象被销毁了");
    }
}
