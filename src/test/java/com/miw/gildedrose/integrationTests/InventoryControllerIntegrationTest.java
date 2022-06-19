package com.miw.gildedrose.integrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miw.gildedrose.domain.Inventory;
import com.miw.gildedrose.domain.Item;
import com.miw.gildedrose.domain.Order;
import com.miw.gildedrose.exception.ErrorMessage;
import com.miw.gildedrose.services.InventoryServiceImpTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InventoryControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void when_getInventories_is_OK() throws Exception {
        mockMvc.perform(get("/Inventories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(5)))
                .andExpect(jsonPath("$[0].item.name", Matchers.is("Google Pixel 6")))
                .andExpect(jsonPath("$[0].amount", Matchers.is(100)));
    }

    @Test
    void when_purchase_is_Created() throws Exception {
        Item item = new Item("Google Pixel 6", "", 50);
        Order order = new Order(item, 50);
        mockMvc.perform(post("/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(order)))
                .andExpect(status().isCreated());
    }


    @Test
    void when_purchase_throw_INVENTORY_NOT_FOUND() throws Exception {
        Item item = new Item("Google Pixel", "", 50);
        Order order = new Order(item, 50);
        mockMvc.perform(post("/purchase")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(order)))
                .andExpect(status().isExpectationFailed())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString(
                        "{\"status\":\"EXPECTATION_FAILED\",\"message\":\"" +
                                ErrorMessage.INVENTORY_NOT_FOUND.getMessage() + "\",\"timeStamp\":")));
    }

    @Test
    void when_purchase_throw_PURCHASE_AMOUNT_MUST_BIGGER_THAN_Zero() throws Exception {
        Item item = new Item("Google Pixel 6", "", 50);
        Order order = new Order(item, 0);
        mockMvc.perform(post("/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(order)))
                .andExpect(status().isExpectationFailed())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString(
                        "{\"status\":\"EXPECTATION_FAILED\",\"message\":\"" +
                                ErrorMessage.PURCHASE_AMOUNT_MUST_BIGGER_THAN_Zero.getMessage() + "\",\"timeStamp\":")));
    }

    @Test
    void when_purchase_throw_INSUFFICIENT_INVENTORY() throws Exception {
        Item item = new Item("Google Pixel 6", "", 50);
        Order order = new Order(item, 150);
        mockMvc.perform(post("/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(order)))
                .andExpect(status().isExpectationFailed())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString(
                        "{\"status\":\"EXPECTATION_FAILED\",\"message\":\"" +
                                ErrorMessage.INSUFFICIENT_INVENTORY.getMessage() + "\",\"timeStamp\":")));
    }
}