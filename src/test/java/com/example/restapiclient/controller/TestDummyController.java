package com.example.restapiclient.controller;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Dummy test")
public class TestDummyController {

    @Test
    @DisplayName("Test info")
    public void testInfo(TestInfo testInfo1){
        System.out.println(testInfo1.getDisplayName());
        System.out.println(testInfo1.getTestClass());
        System.out.println(testInfo1.getTestMethod());
//        assertNull(testInfo1,"Info should be null");
    }
}
