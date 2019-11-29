#### 接口定义
InstantiationAwareBeanPostProcessor继承自BeanPostProcessor 是spring非常重要的拓展接口，代表这bean的一段生命周期: 实例化(Instantiation) 
````
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {
    
    @Nullable
    default Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    default boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

    @Nullable
    default PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName)
            throws BeansException {

        return null;
    }

    @Deprecated
    @Nullable
    default PropertyValues postProcessPropertyValues(
            PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {

        return pvs;
    }

}
````
由于InstantiationAwareBeanPostProcessor继承自BeanPostProcessor
说明一下：
1. postProcessBeforeInstantiation调用时机为bean实例化(Instantiation)之前 如果返回了bean实例, 则会替代原来正常通过target bean生成的bean的流程. 典型的例如aop返回proxy对象. 此时bean的执行流程将会缩短, 只会执行 
    BeanPostProcessor#postProcessAfterInitialization接口完成初始化。
2. postProcessAfterInstantiation调用时机为bean实例化(Instantiation)之后和任何初始化(Initialization)之前。
3. postProcessProperties调用时机为postProcessAfterInstantiation执行之后并返回true, 返回的PropertyValues将作用于给定bean属性赋值. spring 5.1之后出现以替换@Deprecated标注的postProcessPropertyValues
4. postProcessPropertyValues已经被标注@Deprecated，后续将会被postProcessProperties取代。

#### InstantiationAwareBeanPostProcessor与BeanPostProcessor对比
1. BeanPostProcessor 执行时机为bean初始化（Initialization）阶段，日常可以拓展该接口对bean初始化进行定制化处理。
2. InstantiationAwareBeanPostProcessor 执行时机bean实例化（Instantiation）阶段，典型用于替换bean默认创建方式，例如aop通过拓展接口生成代理对应，主要用于基础框架层面。如果日常业务中需要拓展该，spring推荐使用适配器类InstantiationAwareBeanPostProcessorAdapter。
3. 所有bean创建都会进行回调。

#### InstantiationAwareBeanPostProcessor源码分析：注册时机和触发点
1、由于InstantiationAwareBeanPostProcessor实质也是BeanPostProcessor接口，register时机是一致的     
2、这里着重分析接口触发的时机，跟BeanPostProcessor一样触发入口从AbstractAutowireCapableBeanFactory#createBean开始 ：
总结一下：
* InstantiationAwareBeanPostProcessor的触发入口从AbstractAutowireCapableBeanFactory#createBean开始。
* bean实例化之前会检测是否存在该类型的接口，并触发前置postProcessBeforeInstantiation。注册多个实例时会依次执行回调，任何一个返回非null则直接执行BeanPostProcessor#postProcessAfterInitialization完成初始化。返回的bean直接返回容器，生命周期缩短。
* 后置postProcessAfterInstantiation会在实例化之后，依赖注入和初始化方法之前。注册多个接口只要其中一个返回false，即停止后续执行。 返回结果会影响后续执行流程，通过此定制化bean属性注入等操作。
* 优先回调postProcessProperties，spring-5.1之后新增回调接口 用以替代标注过时的postProcessPropertyValues方法。
* InstantiationAwareBeanPostProcessor设计主要给基础性框架使用，日常应用spring推荐使用适配器类InstantiationAwareBeanPostProcessorAdapter

