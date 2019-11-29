package com.orange.spring.analysis.note.BeanPostProcessor;

import org.springframework.stereotype.Component;

/**
 * @author xieyong
 * @date 2019/11/29
 * @Description:
 */
@Component
public class Bean {

    public Bean(){
        System.out.println("无参构造函数被调用啦");
    }

    public Bean(String name){
        System.out.println("构造函数被调用啦");
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
        return "Bean{" +
                "name='" + name + '\'' +
                '}';
    }
}
