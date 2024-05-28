package com.trial.proj.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trial.proj.controller.InventoryController;
import com.trial.proj.model.InventoryItem;
import com.trial.proj.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

public class InventoryControllerTest {

    @InjectMocks
    InventoryController inventoryController;

    @Mock
    InventoryService inventoryService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();
    }

    @Test
    @DisplayName("Should return recommendation when phone recommendation intent is received")
    public void shouldReturnRecommendationWhenPhoneRecommendationIntentIsReceived() throws Exception {
        InventoryItem item = new InventoryItem();
        item.setCategory("Phone");
        item.setProd_name("iPhone 12");
        item.setPrice(999.99);

        when(inventoryService.getRandomPhone()).thenReturn(item);

        Map<String, Object> intent = new HashMap<>();
        intent.put("displayName", "phone_recommendation intent");

        Map<String, Object> queryResult = new HashMap<>();
        queryResult.put("intent", intent);

        Map<String, Object> request = new HashMap<>();
        request.put("queryResult", queryResult);

        mockMvc.perform(post("/api/inventory/dialogflow/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fulfillmentText", is("I recommend the Phone: iPhone 12. It costs $999.99.")));
    }

    @Test
    @DisplayName("Should return no phones message when no phones are available")
    public void shouldReturnNoPhonesMessageWhenNoPhonesAreAvailable() throws Exception {
        when(inventoryService.getRandomPhone()).thenReturn(null);

        Map<String, Object> intent = new HashMap<>();
        intent.put("displayName", "phone_recommendation intent");

        Map<String, Object> queryResult = new HashMap<>();
        queryResult.put("intent", intent);

        Map<String, Object> request = new HashMap<>();
        request.put("queryResult", queryResult);

        mockMvc.perform(post("/api/inventory/dialogflow/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fulfillmentText", is("Sorry, I couldn't find any phones in the inventory.")));
    }

    @Test
    @DisplayName("Should return empty response when intent is not phone recommendation")
    public void shouldReturnEmptyResponseWhenIntentIsNotPhoneRecommendation() throws Exception {
        Map<String, Object> intent = new HashMap<>();
        intent.put("displayName", "other_intent");

        Map<String, Object> queryResult = new HashMap<>();
        queryResult.put("intent", intent);

        Map<String, Object> request = new HashMap<>();
        request.put("queryResult", queryResult);

        mockMvc.perform(post("/api/inventory/dialogflow/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fulfillmentText").doesNotExist());
    }
}