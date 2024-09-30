package spring.lh.annotation.aop2;

import org.springframework.stereotype.Service;

@MyCustomAnnotation(value = "This is a test service")
@Service
public class MyService {

	public void performTask() {
		System.out.println("Task is being performed");
	}
}
