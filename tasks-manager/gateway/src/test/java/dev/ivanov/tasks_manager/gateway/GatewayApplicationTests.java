package dev.ivanov.tasks_manager.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(properties = {"spring.cloud.config.enabled=false"})
class GatewayApplicationTests {

	@Test
	void contextLoads() {
	}

}
