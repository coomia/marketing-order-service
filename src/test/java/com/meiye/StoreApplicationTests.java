package com.meiye;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StoreApplicationTests {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	public void contextLoads() {
		logger.trace("I am trace log.");
		logger.debug("I am debug log.");
		logger.warn("I am warn log.");
		logger.error("I am error log.");
	}

}
