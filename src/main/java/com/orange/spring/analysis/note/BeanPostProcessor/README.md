#### 接口定义
* BeanPostProcessor是spring非常重要的拓展接口，例如aop通过拓展接口生产代理bean等。
* ApplicationContext类型的容器会自动发现和注册该类型的bean，通过BeanPostProcessor可以对bean进行定制化，接口作用域是所有bean创建。
* 接口有两个方法：
    postProcessBeforeInitialization触发时机在bean实例化(Instantiation)之后，所有初始化(Initialization)动作(包括 InitializingBean#afterPrpertiesSet() 和 定制化init-method())以前。
    postProcessAfterInitialization触发时机在bean实例化(Instantiation)之后，所有初始化(Initialization)动作(包括 InitializingBean#afterPrpertiesSet() 和 定制化init-method())以后。同时还会在InstantiationAwareBeanPostProcessor#postProcessBeforeInstantiation 和 FactoryBean(bean工厂)获得bean时候调用。这个也是重要的点，放在往后的案例中阐述。

#### 在ApplicationContext容器中何时注册BeanPostProcessor接口时间点
在AbstractApplicationContext容器启动的refresh()会注册BeanPostProcessor，源码如下：
````
@Override
    public void refresh() throws BeansException, IllegalStateException {
        synchronized (this.startupShutdownMonitor) {
            // Prepare this context for refreshing.
            prepareRefresh();

            // Tell the subclass to refresh the internal bean factory.
            ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

            // Prepare the bean factory for use in this context.
            prepareBeanFactory(beanFactory);

            try {
                // Allows post-processing of the bean factory in context subclasses.
                postProcessBeanFactory(beanFactory);

                // Invoke factory processors registered as beans in the context.
                invokeBeanFactoryPostProcessors(beanFactory);

                // Register bean processors that intercept bean creation.
                // 注册BeanPostProcessor 在bean创建的时候进行拦截
               registerBeanPostProcessors(beanFactory);

                // Initialize message source for this context.
                initMessageSource();

                // Initialize event multicaster for this context.
                initApplicationEventMulticaster();

                // Initialize other special beans in specific context subclasses.
                onRefresh();

                // Check for listener beans and register them.
                registerListeners();

                // Instantiate all remaining (non-lazy-init) singletons.
                finishBeanFactoryInitialization(beanFactory);

                // Last step: publish corresponding event.
                finishRefresh();
            }

            catch (BeansException ex) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Exception encountered during context initialization - " +
                            "cancelling refresh attempt: " + ex);
                }

                // Destroy already created singletons to avoid dangling resources.
                destroyBeans();

                // Reset 'active' flag.
                cancelRefresh(ex);

                // Propagate exception to caller.
                throw ex;
            }

            finally {
                // Reset common introspection caches in Spring's core, since we
                // might not ever need metadata for singleton beans anymore...
                resetCommonCaches();
            }
        }
    }

        ......

    /**
     * Instantiate and invoke all registered BeanPostProcessor beans,
     * respecting explicit order if given.
     * <p>Must be called before any instantiation of application beans.
     */
    protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        PostProcessorRegistrationDelegate.registerBeanPostProcessors(beanFactory, this);
    }
````

```
public static void registerBeanPostProcessors(
            ConfigurableListableBeanFactory beanFactory, AbstractApplicationContext applicationContext) {

        /**
         * 此时bean的定义加载已经完成，但是还没有实例化。 获得所有BeanPostProcessor bean对应的beanName
         */  
        String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true, false);

        /**
         * 注册日志BeanPostProcessor， 检测注册的bean是否为spring基础服务类并打印日志
         */ 
        // Register BeanPostProcessorChecker that logs an info message when
        // a bean is created during BeanPostProcessor instantiation, i.e. when
        // a bean is not eligible for getting processed by all BeanPostProcessors.
        int beanProcessorTargetCount = beanFactory.getBeanPostProcessorCount() + 1 + postProcessorNames.length;
        beanFactory.addBeanPostProcessor(new BeanPostProcessorChecker(beanFactory, beanProcessorTargetCount));

        /**
         * 当有注册多个BeanPostProcessor接口时会按顺序进行，即 实现PriorityOrdered->实现Ordered->普通类型接口->内部系统MergedBeanDefinitionPostProcessor
         */ 
        // Separate between BeanPostProcessors that implement PriorityOrdered,
        // Ordered, and the rest.
        List<BeanPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
        List<BeanPostProcessor> internalPostProcessors = new ArrayList<>();
        List<String> orderedPostProcessorNames = new ArrayList<>();
        List<String> nonOrderedPostProcessorNames = new ArrayList<>();
        for (String ppName : postProcessorNames) {
            if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {

               /**
                 * 通过beanFactory.getBean(), 将会提前初始化BeanPostProcessor类型实例
                 */ 
                BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
                priorityOrderedPostProcessors.add(pp);
                if (pp instanceof MergedBeanDefinitionPostProcessor) {
                    internalPostProcessors.add(pp);
                }
            }
            else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
                orderedPostProcessorNames.add(ppName);
            }
            else {
                nonOrderedPostProcessorNames.add(ppName);
            }
        }

        // First, register the BeanPostProcessors that implement PriorityOrdered.
        sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
        registerBeanPostProcessors(beanFactory, priorityOrderedPostProcessors);

        // Next, register the BeanPostProcessors that implement Ordered.
        List<BeanPostProcessor> orderedPostProcessors = new ArrayList<>();
        for (String ppName : orderedPostProcessorNames) {
            BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
            orderedPostProcessors.add(pp);
            if (pp instanceof MergedBeanDefinitionPostProcessor) {
                internalPostProcessors.add(pp);
            }
        }
        sortPostProcessors(orderedPostProcessors, beanFactory);
        registerBeanPostProcessors(beanFactory, orderedPostProcessors);

        // Now, register all regular BeanPostProcessors.
        List<BeanPostProcessor> nonOrderedPostProcessors = new ArrayList<>();
        for (String ppName : nonOrderedPostProcessorNames) {
            BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
            nonOrderedPostProcessors.add(pp);
            if (pp instanceof MergedBeanDefinitionPostProcessor) {
                internalPostProcessors.add(pp);
            }
        }
        registerBeanPostProcessors(beanFactory, nonOrderedPostProcessors);

        // Finally, re-register all internal BeanPostProcessors.
        sortPostProcessors(internalPostProcessors, beanFactory);
        registerBeanPostProcessors(beanFactory, internalPostProcessors);

        /**
         * ApplicationListener探测器，当bean实现ApplicationListener接口时自动注册监听
         */
        // Re-register post-processor for detecting inner beans as ApplicationListeners,
        // moving it to the end of the processor chain (for picking up proxies etc).
        beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(applicationContext));
    }

    /**
     * 按照给定Ordered的顺序排序
     */
    private static void sortPostProcessors(List<?> postProcessors, ConfigurableListableBeanFactory beanFactory) {
        Comparator<Object> comparatorToUse = null;
        if (beanFactory instanceof DefaultListableBeanFactory) {
            comparatorToUse = ((DefaultListableBeanFactory) beanFactory).getDependencyComparator();
        }
        if (comparatorToUse == null) {
            comparatorToUse = OrderComparator.INSTANCE;
        }
        postProcessors.sort(comparatorToUse);
    }

    /**
     * Register the given BeanPostProcessor beans.
     */
    private static void registerBeanPostProcessors(
            ConfigurableListableBeanFactory beanFactory, List<BeanPostProcessor> postProcessors) {

        for (BeanPostProcessor postProcessor : postProcessors) {
            beanFactory.addBeanPostProcessor(postProcessor);
        }
    }
```
```
/** BeanPostProcessors to apply in createBean. */
    private final List<BeanPostProcessor> beanPostProcessors = new CopyOnWriteArrayList<>();

    /** Indicates whether any InstantiationAwareBeanPostProcessors have been registered. */
    private volatile boolean hasInstantiationAwareBeanPostProcessors;

    /** Indicates whether any DestructionAwareBeanPostProcessors have been registered. */
    private volatile boolean hasDestructionAwareBeanPostProcessors;


    /**
     * 所有的BeanPostProcessor接口实例保存于CopyOnWriteArrayList中
     * 1、一个实例只注册一次
     * 2、通过hasInstantiationAwareBeanPostProcessors设置，判定是否存在InstantiationAwareBeanPostProcessor接口实例，为后续执行做判断
     * 3、通过hasDestructionAwareBeanPostProcessors设置，判定是否存在DestructionAwareBeanPostProcessor接口实例，为后续执行做判断
     */
    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        Assert.notNull(beanPostProcessor, "BeanPostProcessor must not be null");
        // Remove from old position, if any
        this.beanPostProcessors.remove(beanPostProcessor);
        // Track whether it is instantiation/destruction aware
        if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
            this.hasInstantiationAwareBeanPostProcessors = true;
        }
        if (beanPostProcessor instanceof DestructionAwareBeanPostProcessor) {
            this.hasDestructionAwareBeanPostProcessors = true;
        }
        // Add to end of list
        this.beanPostProcessors.add(beanPostProcessor);
    }
```
总结如下：
* BeanPostProcessor注册的入口在AbstractApplicationContext容器启动的refresh()的AbstractApplicationContext#registerBeanPostProcessors。
* AbstractApplicationContext进而委派PostProcessorRegistrationDelegate#registerBeanPostProcessors实现注册。
* 当有注册多个BeanPostProcessor接口时会按顺序进行，即 实现PriorityOrdered->实现Ordered->普通类型接口->内部系统MergedBeanDefinitionPostProcessor。而同种类型也会进行排序，按顺序注册。
* BeanPostProcessor类型实例会通过BeanFactory#getBean()提前初始化。


2、触发BeanPostProcessor方法

总结如下：
(1.1)、由此可见BeanPostProcessor的一个触发入口在AbstractAutowireCapableBeanFactory#createBean，最终至AbstractAutowireCapableBeanFactory#initializeBean。
(1.2)、AbstractAutowireCapableBeanFactory#initializeBean不但会触发BeanPostProcessor回掉方法，并且还会按顺序执行bean生命周期：
factory类型接口(BeanNameAware#setBeanName)
factory类型接口(BeanClassLoaderAware#setBeanClassLoader)
factory类型接口(BeanFactoryAware#setBeanFactory)
BeanPostProcessor#postProcessBeforeInitialization
InitializingBean和自定义的init-method
BeanPostProcessor#postProcessAfterInitialization


#### 何时apply对应的回调方法
入口一：AbstractAutowireCapableBeanFactory#createBean 中
入口二：FactoryBeanRegistrySupport

  

