package com.orange.spring.analysis.note.InstantiationAwareBeanPostProcessor;

import com.orange.spring.analysis.note.BeanPostProcessor.Bean;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.stereotype.Component;

/**
 * @author xieyong
 * @date 2019/11/29
 * @Description:
 */
@Component
public class CustomInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        System.out.print("beanName:"+beanName+"执行..postProcessAfterInstantiation\n");
        // 会影响postProcessProperties 是否执行，返回false不执行
        return true;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        System.out.print("beanName:"+beanName+"执行..postProcessBeforeInstantiation\n");
        if(beanClass == Bean.class){
            //利用 其 生成动态代理
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(beanClass);
            enhancer.setCallback(new BeanMethodInterceptor());
            Bean bean = (Bean)enhancer.create();
            System.out.print("返回动态代理\n");
            return bean ;
            //返回不为null，直接执行postProcessAfterInitialization
        }
        return null ;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        System.out.print("beanName:"+beanName+"执行..postProcessProperties\n");
        //由于postProcessBeforeInstantiation返回Object对象，所以此方法并不会执行
        if(bean instanceof Bean){
            //修改bean中name 的属性值
            PropertyValue value = pvs.getPropertyValue("name");
            System.out.print("修改之前 name 的value是："+value.getValue()+"\n");
            value.setConvertedValue("我修改啦");
            return pvs;
        }

        return pvs;
    }

    //************************************** BeanPostProcessor **********************************************

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.print("beanName:"+beanName+"执行..postProcessAfterInitialization\n");
        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.print("beanName:"+beanName+"执行..postProcessBeforeInitialization\n");
        return bean;
    }
}
