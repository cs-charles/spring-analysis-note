#### 接口定义
* 在Spring中,关于bean定义,其Java建模模型是接口BeanDefinition, 其变种有RootBeanDefinition，ChildBeanDefinition，还有GenericBeanDefinition，AnnotatedGenericBeanDefinition,ScannedGenericBeanDefinition等等。这些概念模型抽象了不同的关注点。关于这些概念模型，除了有概念，也有相应的Java建模模型，甚至还有通用的实现部分AbstractBeanDefinition。但事实上，关于BeanDefinition，还有一个概念也很重要，这就是MergedBeanDefinition
* MergedBeanDefinitionPostProcessor接口基础BeanPostProcessor，主要提供void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName)方法
* 一个MergedBeanDefinition其实就是一个"合并了的BeanDefinition"；从ChildrenBeanDefinition，最终以RootBeanDefinition的类型存在。
*扩展：常见的BeanDefinition
   1. RootBeanDefinition:
        代表一个xml,java Config来的BeanDefinition
   2. ChildBeanDefinition:
        可以让子BeanDefinition定义拥有从父母哪里继承配置的能力
   3. GenericBeanDefinition:
        spring2.5后注册bean首选的是GenericBeanDefinition。GenericBeanDefinition允许动态的设置父bean.GenericBeanDefinition可以作为RootBeanDefinition与ChildBeanDefinition的替代品。
   4. AnnotatedBeanDefinition接口：
        表示注解类型BeanDefinition。有两个重要的属性，AnnotationMetadata，MethodMetadata分别表示BeanDefinition的注解元信息和方法元信息
        实现了此接口的BeanDefinition可以获取到注解元数据和方法元数据。
   5. AnnotatedGenericBeanDefinition类:
        表示@Configuration注解注释的BeanDefinition类
   6. ScannedGenericBeanDefinition类:
        表示@Component、@Service、@Controller等注解注释的Bean类
   ![image]('https://github.com/dscxieyong/spring-analysis-note/blob/master/src/main/java/com/orange/spring/analysis/note/beanDefinition.png')
#### 触发时间
该方法是bean在合并Bean定义之后调用

#### spring实践
Spring框架工具AutowiredAnnotationBeanPostProcessor的实现代码片段，该工具实现了MergedBeanDefinitionPostProcessor,它在一个bean的postProcessMergedBeanDefinition()阶段，如上代码所示，获取该bean的依赖注入元数据(哪些方法使用了@Autowired,@Inject,@Value等等)，随后用于该bean属性填充中依赖注入执行阶段的输入。

#### 总结
综上可见，一个MergedBeanDefinition是这样一个载体:
根据原始BeanDefinition及其可能存在的双亲BeanDefinition中的bean定义信息"合并"而得来的一个RootBeanDefinition；
每个Bean的创建需要的是一个MergedBeanDefinition，也就是需要基于原始BeanDefinition及其双亲BeanDefinition信息得到一个信息"合并"之后的BeanDefinition；
Spring框架同时提供了一个机会给框架其他部分，或者开发人员用于在bean创建过程中，MergedBeanDefinition生成之后，bean属性填充之前，对该bean和该MergedBeanDefinition做一次回调，相应的回调接口是MergedBeanDefinitionPostProcessor。
MergedBeanDefinition没有相应的Spring建模，它是处于一个内部使用目的合并自其它BeanDefinition对象，其具体对象所使用的实现类类型是RootBeanDefinition。
