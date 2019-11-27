package com.orange.spring.analysis.note.BeanDefinitionRegistryPostProcessor;

/**
 * @author xieyong
 * @date 2019/11/27
 * @Description:
 */
public class RegistryBean {
    public RegistryBean(){

    }

    public RegistryBean(String name){
        this.name = name ;
    }

    private String name ;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "RegistryBean{" +
                "name='" + name + '\'' +
                '}';
    }
}
