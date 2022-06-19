package com.miw.gildedrose.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miw.gildedrose.domain.Inventory;
import com.miw.gildedrose.domain.Order;
import com.miw.gildedrose.exception.ErrorMessage;
import com.miw.gildedrose.services.InventoryService;
import com.miw.gildedrose.services.InventoryServiceImpTest;
import com.miw.gildedrose.services.ItemService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(InventoryController.class)
class InventoryControllerTest {
    @MockBean
    ItemService itemService;
    @MockBean
    InventoryService inventoryService;
    @Autowired
    MockMvc mockMvc;

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void when_getInventories_is_OK() throws Exception {
        List<Inventory> inventories = InventoryServiceImpTest.createInventories();

        when(inventoryService.getAll()).thenReturn(inventories);

        mockMvc.perform(get("/Inventories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(inventories.size())))
                .andExpect(jsonPath("$[0].amount", Matchers.is(inventories.get(0).getAmount())));
    }

    @Test
    void when_purchase_is_Created() throws Exception {
        List<Inventory> inventories = InventoryServiceImpTest.createInventories();
        Order order = new Order(inventories.get(0).getItem(), 50);
        mockMvc.perform(post("/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(order)))
                .andExpect(status().isCreated());
    }

    @Test
    void when_purchase_throw_Created() throws Exception {
        List<Inventory> inventories = InventoryServiceImpTest.createInventories();
        Order order = new Order(inventories.get(0).getItem(), 150);

        for (ErrorMessage error : ErrorMessage.values() ) {
            doThrow(new RuntimeException(error.getMessage()))
                    .when(inventoryService).purchase(order);

            mockMvc.perform(post("/purchase")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(order)))
                    .andExpect(status().isExpectationFailed())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().string(containsString(
                            "{\"status\":\"EXPECTATION_FAILED\",\"message\":\"" +
                                    error.getMessage() + "\",\"timeStamp\":")));
        }
    }
}