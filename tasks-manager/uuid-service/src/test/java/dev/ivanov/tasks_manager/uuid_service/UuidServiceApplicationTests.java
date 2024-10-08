package dev.ivanov.tasks_manager.uuid_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(properties = {"spring.cloud.config.enabled=false"})
class UuidServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
