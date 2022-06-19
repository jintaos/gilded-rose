package com.miw.gildedrose.services;

import com.miw.gildedrose.domain.Item;
import com.miw.gildedrose.repositories.ItemRepository;
import com.miw.gildedrose.repositories.ItemViewedRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImpTest {

    @Mock
    ItemRepository itemRepository;
    @Mock
    ItemViewedRepository itemViewedRepository;

    @InjectMocks
    ItemServiceImp itemService;

    @Test
    void when_getItemByName_not_exit_item_should_return_optional_null() {
        when(itemRepository.findById(any(String.class)))
                .thenReturn(Optional.empty());

        Optional<Item> opt = itemService.GetItemByName("NotExit");

        Assertions.assertThat(opt).isEmpty();
    }

    @Test
    void when_getItemByName_exit_item_should_return_item() {
        String name = "Name";
        Item expected = new Item(name, "Desc", 100);
        when(itemRepository.findById(name))
                .thenReturn(Optional.of(expected));

        Optional<Item> opt = itemService.GetItemByName(name);

        Assertions.assertThat(opt).isPresent();
        Assertions.assertThat(opt.get()).isSameAs(expected);
    }

    @Test
    void when_getItemByName_and_surged_price_should_10_Percent_price_increase() {
        String name = "Name";
        int originalPrice = 200;
        Item expected = new Item(name, "Desc", originalPrice);
        when(itemRepository.findById(name))
                .thenReturn(Optional.of(expected));
        when(itemViewedRepository.countByItemNameAndViewAtAfter(eq(name), any(LocalDateTime.class)))
                .thenReturn(11);

        Optional<Item> opt = itemService.GetItemByName(name);

        Assertions.assertThat(opt).isPresent();
        Assertions.assertThat(opt.get().getPrice()).isEqualTo((int) (originalPrice * 1.1));
    }

    @Test
    void when_surgePricing_not_viewed_not_more_than_10_times() {
        String name = "Name";
        int originalPrice = 100;
        Item expected = new Item(name, "Desc", originalPrice);
        when(itemRepository.findById(name))
                .thenReturn(Optional.of(expected));
        when(itemViewedRepository.countByItemNameAndViewAtAfter(eq(name), any(LocalDateTime.class)))
                .thenReturn(10);

        Optional<Item> opt = itemService.GetItemByName(name);

        Assertions.assertThat(opt).isPresent();
        Item actualItem = opt.get();
        Assertions.assertThat(actualItem.getName()).isEqualTo(name);
        Assertions.assertThat(actualItem.getDescription()).isEqualTo(expected.getDescription());
        Assertions.assertThat(actualItem.getPrice()).isEqualTo(originalPrice);
    }

    @Test
    void when_surgePricing_not_viewed_more_than_10_times() {
        String name = "Name";
        int originalPrice = 100;
        Item expected = new Item(name, "Desc", originalPrice);
        when(itemRepository.findById(name))
                .thenReturn(Optional.of(expected));
        when(itemViewedRepository.countByItemNameAndViewAtAfter(eq(name), any(LocalDateTime.class)))
                .thenReturn(11);

        Optional<Item> opt = itemService.GetItemByName(name);

        Assertions.assertThat(opt).isPresent();
        Item actualItem = opt.get();
        Assertions.assertThat(actualItem.getName()).isEqualTo(name);
        Assertions.assertThat(actualItem.getDescription()).isEqualTo(expected.getDescription());
        Assertions.assertThat(actualItem.getPrice()).isEqualTo((int)(originalPrice * 1.1));
    }
}

