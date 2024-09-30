package spring.lh.annotation.aop2;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class MyCustomAnnotationProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		// 检查类上是否有 MyCustomAnnotation 注解
		if (bean.getClass().isAnnotationPresent(MyCustomAnnotation.class)) {
			MyCustomAnnotation annotation = bean.getClass().getAnnotation(MyCustomAnnotation.class);
			String value = annotation.value();
			// 输出日志或其他操作
			System.out.println("Processing @MyCustomAnnotation for bean: " + beanName + ", with value: " + value);
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		// After initialization, can also process if needed
		return bean;
	}
}
