package spring.lh.annotation.aop2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)  // 可以标注在类上
@Retention(RetentionPolicy.RUNTIME)  // 保留到运行时
public @interface MyCustomAnnotation {
	String value() default "";
}
