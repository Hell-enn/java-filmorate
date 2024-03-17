package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmorateApplicationController;

@SpringBootTest
class FilmorateApplicationTests {

	private static FilmorateApplicationController filmorateApplicationController;

	@BeforeAll
	static void createContext() {
		filmorateApplicationController = new FilmorateApplicationController();
	}

	@Test
	void contextLoads() {
		Assertions.assertEquals("FilmoRate", filmorateApplicationController.welcomePage());
	}

}
