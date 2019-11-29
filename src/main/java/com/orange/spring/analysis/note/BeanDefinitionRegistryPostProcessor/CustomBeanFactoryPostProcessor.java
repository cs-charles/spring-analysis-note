package com.orange.spring.analysis.note.BeanDefinitionRegistryPostProcessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.stereotype.Component;

/**
 * @author xieyong
 * @date 2019/11/27
 * @Description:
 */
@Component
public class CustomBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinition bd = beanFactory.getBeanDefinition("registryBean") ;
        MutablePropertyValues mpv = null ;
        if(bd != null && (mpv = bd.getPropertyValues())!=null){
            PropertyValue value = mpv.getPropertyValue("name");
            System.out.print("修改之前 a 的value是："+value.getValue()+"\n");
            value.setConvertedValue("postProcessBeanFactory修改属性");
        }
        /**也可以注册BeanDefinition对象**/
        /*DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory)beanFactory;
        BeanDefinition beanDefinition =  new GenericBeanDefinition(bd);
        defaultListableBeanFactory.registerBeanDefinition("registryBean1",beanDefinition);*/
    }
}
