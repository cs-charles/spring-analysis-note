package com.orange.spring.analysis.note.FactoryBean;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * @author xieyong
 * @date 2019/12/2
 * @Description:
 */
@Component
public class DecorationFactoryBean implements FactoryBean<FactoryBeanObject> {
    @Override
    public FactoryBeanObject getObject() throws Exception {
        FactoryBeanObject factoryBeanObject = new FactoryBeanObject();
        return factoryBeanObject;
    }

    @Override
    public Class<FactoryBeanObject> getObjectType() {
        return FactoryBeanObject.class;
    }
}
