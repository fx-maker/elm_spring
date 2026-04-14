package com.neusoft;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
//开启Spring Cloud Feign的支持功能
@EnableFeignClients
public class MyApplication {
	//RestTemplate不需要了	
	// 向容器中添加RestTemplate实例
//	@Bean
//	public RestTemplate getRestTemplate() {
//		return new RestTemplate();
//	}

	public static void main(String[] args) {
		SpringApplication.run(MyApplication.class, args);
	}
}
