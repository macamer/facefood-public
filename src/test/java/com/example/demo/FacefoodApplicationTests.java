package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.demo.service.AzureBlobService;

@SpringBootTest
class FacefoodApplicationTests {
	
	@MockBean
    private AzureBlobService azureBlobService;

	@Test
	void contextLoads() {
	}

}
