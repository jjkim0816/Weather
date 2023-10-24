package com.zerobase.weather;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WeatherApplicationTests {

	@Test
	void equalTest() {
		// given
		// when
		// then
		assertEquals("111", "111");
	}
	
	@Test
	void nullTest() {
		// given
		// when
		// then
		assertNull(null);
	}
	
	@Test
	void trueTest() {
		// given
		// when
		// then
		assertTrue(Objects.equals(1,1));
	}
}
