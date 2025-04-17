package com.sparta.redis_caching;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.*;

@SpringBootApplication
// 스프링 부트 3.3.1 부터 Page 객체 경고 방지
@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
public class RedisCachingApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisCachingApplication.class, args);
	}

}
