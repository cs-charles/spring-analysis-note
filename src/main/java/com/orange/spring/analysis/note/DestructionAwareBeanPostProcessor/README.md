#### 接口定义
* DestructionAwareBeanPostProcessor接口继承BeanPostProcessor接口。多出了1个方法：
public void postProcessBeforeDestruction(Object bean, String beanName)

#### 触发时间
在Bean被销毁时触发，Bean对象是在容器被销毁时销毁,所谓销毁仅仅是指关闭Spring的上下文对象，然后移除BeanDefinition对象，但是对于Bean实例并不会被回收。