package com.martinachov.hexagonal.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martinachov.hexagonal.infrastructure.adapters.input.rest.data.request.ProductRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
public class MemoryOptimizationIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Test
    public void testMemoryOptimizedOperations() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        Runtime runtime = Runtime.getRuntime();
        runtime.gc(); // Force garbage collection
        long initialMemory = runtime.totalMemory() - runtime.freeMemory();
        
        // Perform many operations to test memory efficiency
        for (int i = 0; i < 50; i++) {
            ProductRequest request = ProductRequest.builder()
                    .name("Memory Test Product " + i)
                    .description("Testing memory optimization " + i)
                    .build();
            
            // Create product
            String response = mockMvc.perform(post("/v1/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value("Memory Test Product " + i))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            
            // Extract ID and retrieve product
            Long productId = objectMapper.readTree(response).get("id").asLong();
            
            mockMvc.perform(get("/v1/products/" + productId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(productId))
                    .andExpect(jsonPath("$.name").value("Memory Test Product " + i));
        }
        
        // Check memory usage after operations
        runtime.gc();
        long finalMemory = runtime.totalMemory() - runtime.freeMemory();
        long memoryIncrease = finalMemory - initialMemory;
        
        System.out.println("Initial memory: " + initialMemory + " bytes");
        System.out.println("Final memory: " + finalMemory + " bytes");
        System.out.println("Memory increase: " + memoryIncrease + " bytes");
        
        // Memory increase should be reasonable (less than 20MB for 50 operations)
        assert memoryIncrease < 20 * 1024 * 1024 : "Memory increase is too high: " + memoryIncrease + " bytes";
    }

    @Test 
    public void testActuatorEndpoints() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Test health endpoint
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
        
        // Test metrics endpoint  
        mockMvc.perform(get("/actuator/metrics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.names").isArray());
        
        // Test JVM memory metrics
        mockMvc.perform(get("/actuator/metrics/jvm.memory.used"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("jvm.memory.used"));
    }
}