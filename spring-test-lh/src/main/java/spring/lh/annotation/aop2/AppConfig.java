package spring.lh.annotation.aop2;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "spring.lh.annotation.aop2")  // 扫描注解所在的包
public class AppConfig {
	// 其他配置可以放在这里
}
