package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.url=jdbc:h2:mem:shareit",
        "spring.datasource.username=test",
        "spring.datasource.password=test"})
public class ShareItServerAppTest {
    @Test
    void contextLoads() {

    }
}