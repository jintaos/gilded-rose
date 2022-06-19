package com.miw.gildedrose.integrationTests;

import com.miw.gildedrose.controllers.ItemController;
import com.miw.gildedrose.domain.Item;
import com.miw.gildedrose.services.InventoryService;
import com.miw.gildedrose.services.ItemService;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemControllerIT {
    @Autowired
    MockMvc mockMvc;


    @Test
    void when_getAvailableItems_is_OK() throws Exception {
        mockMvc.perform(get("/items/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(5)))
                .andExpect(jsonPath("$[0].name", Matchers.is("Google Pixel 6")))
                .andExpect(jsonPath("$[0].price", Matchers.is(750)));
    }

    @Test
    void when_getItem_is_Not_found() throws Exception {
        mockMvc.perform(get("/items/not exits"))
                .andExpect(status().isNotFound());
    }

    @Test
    void when_getItem_is_OK() throws Exception {
        Item item = new Item("Apple iPhone 13", "Apple iPhone 13, GSM Unlocked, 64GB", 1417);

        final String contentAsString = mockMvc.perform(get("/items/" + item.getName()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Assertions.assertThat(contentAsString).contains(item.getName());
        Assertions.assertThat(contentAsString).contains(String.valueOf(item.getPrice()));
    }


    @Test
    void when_price_is_surged() throws Exception {
        String itemName = "Apple iPhone 13";
        int originalPrice = 1417;
        int expectedSurgedPrice = (int)(1417 * 1.1);

        IntStream.range(1, 12).forEach(i->
        {
            try {
                mockMvc.perform(get("/items/" + itemName)).andExpect(status().isOk());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        final String contentAsString = mockMvc.perform(get("/items/" + itemName))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Assertions.assertThat(contentAsString).contains(itemName);
        Assertions.assertThat(contentAsString).contains(String.valueOf(expectedSurgedPrice));
    }
}