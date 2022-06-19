package com.miw.gildedrose.controllers;

import com.miw.gildedrose.domain.Item;
import com.miw.gildedrose.services.InventoryService;
import com.miw.gildedrose.services.ItemService;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(ItemController.class)
class ItemControllerTest {
    @MockBean
    ItemService itemService;
    @MockBean
    InventoryService inventoryService;
    @Autowired
    MockMvc mockMvc;


    @Test
    void when_getAvailableItems_is_OK() throws Exception {
        List<Item> items = List.of(
                new Item("Google Pixel 6", "Google Pixel 6, 5G Android Phone", 750),
                new Item("Samsung Galaxy A03s", "Samsung Galaxy A03s Black 32GB", 169),
                new Item("Samsung Galaxy S22", "Samsung Galaxy S22 Ultra 5G Black", 154),
                new Item("Apple iPhone 8", "Apple iPhone 8, GSM Unlocked, 64GB", 217),
                new Item("Apple iPhone 13", "Apple iPhone 13, GSM Unlocked, 64GB", 1417)
        );
        when(inventoryService.getAvailableItems()).thenReturn(items);

        mockMvc.perform(get("/items/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(items.size())))
                .andExpect(jsonPath("$[0].name", Matchers.is(items.get(0).getName())));
    }

    @Test
    void when_getItem_is_Not_found() throws Exception {
        when(itemService.GetItemByName("not exits")).thenReturn(Optional.empty());
        mockMvc.perform(get("/items/not exits"))
                .andExpect(status().isNotFound());
    }

    @Test
    void when_getItem_is_OK() throws Exception {
        Item item = new Item("Apple iPhone 13", "Apple iPhone 13, GSM Unlocked, 64GB", 1417);
        when(itemService.GetItemByName(item.getName())).thenReturn(Optional.of(item));

        final String contentAsString = mockMvc.perform(get("/items/" + item.getName()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Assertions.assertThat(contentAsString).contains(item.getName());
    }
}