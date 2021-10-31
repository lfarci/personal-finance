package be.loganfarci.financial.service.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

@SpringBootTest
class ApplicationTests {

	@Autowired
	MessageSource messageSource;

	@Test
	void contextLoads() {
	}
}
