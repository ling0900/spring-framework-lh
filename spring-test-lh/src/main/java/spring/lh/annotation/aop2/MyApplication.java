package spring.lh.annotation.aop2;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MyApplication {

	public static void main(String[] args) {
		// 启动 Spring 容器
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

		// 获取被注解的 Bean
		MyService myService = context.getBean(MyService.class);
		myService.performTask();
	}
}
