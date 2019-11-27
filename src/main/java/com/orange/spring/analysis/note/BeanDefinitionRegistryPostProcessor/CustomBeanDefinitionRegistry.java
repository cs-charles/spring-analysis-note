package com.orange.spring.analysis.note.BeanDefinitionRegistryPostProcessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.stereotype.Component;

/**
 * @author xieyong
 * @date 2019/11/27
 * @Description:
 */
@Component
public class CustomBeanDefinitionRegistry implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        GenericBeanDefinition definition = new GenericBeanDefinition();
        //设置类
        definition.setBeanClass(RegistryBean.class);
        //设置scope
        definition.setScope("singleton");
        //设置是否懒加载
        definition.setLazyInit(false);
        //设置是否可以被其他对象自动注入
        definition.setAutowireCandidate(true);

        // 给属性赋值
        MutablePropertyValues mpv = new MutablePropertyValues() ;
        mpv.add("name", "CustomBeanDefinitionRegistry settings") ;
        definition.setPropertyValues(mpv);

        registry.registerBeanDefinition("registryBean", definition);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
