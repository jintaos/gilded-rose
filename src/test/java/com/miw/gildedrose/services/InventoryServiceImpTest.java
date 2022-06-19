package com.miw.gildedrose.services;

import com.miw.gildedrose.domain.Inventory;
import com.miw.gildedrose.domain.Item;
import com.miw.gildedrose.domain.Order;
import com.miw.gildedrose.exception.ErrorMessage;
import com.miw.gildedrose.repositories.InventoryRepository;
import com.miw.gildedrose.repositories.ItemViewedRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceImpTest {
    @Mock
    InventoryRepository inventoryRepository;
    @Mock
    ItemService itemService;
    @Mock
    ItemViewedRepository itemViewedRepository;
    @InjectMocks
    InventoryServiceImp inventoryService;

    static List<Item> createItems() {
        return List.of(
                new Item("Google Pixel 6", "Google Pixel 6, 5G Android Phone", 750),
                new Item("Samsung Galaxy A03s", "Samsung Galaxy A03s Black 32GB", 169),
                new Item("Samsung Galaxy S22", "Samsung Galaxy S22 Ultra 5G Black", 154),
                new Item("Apple iPhone 8", "Apple iPhone 8, GSM Unlocked, 64GB", 217),
                new Item("Apple iPhone 13", "Apple iPhone 13, GSM Unlocked, 64GB", 1417)
        );
    }

    static List<Inventory> createInventories(List<Item> itemes) {
        return itemes.stream()
                .map(item -> new Inventory(item, 100))
                .collect(Collectors.toList());
    }

    public static List<Inventory> createInventories() {
        return createInventories(createItems());
    }

    @Test
    void when_getAvailableItems_returns_all() {
        List<Item> items = createItems();
        List<Inventory> inventories = createInventories(items);
        when(inventoryRepository.findAll()).thenReturn(inventories);
        when(itemService.SurgePricing(any(Item.class))).thenAnswer(a -> a.getArgument(0));

        List<Item> availableItems = inventoryService.getAvailableItems();

        Assertions.assertThat(availableItems.size()).isEqualTo(items.size());
    }

    @Test
    void when_getAvailableItems_returns_amount_bigger_than_0() {
        List<Item> items = createItems();
        List<Inventory> inventories = createInventories(items);
        inventories.get(0).setAmount(0);
        when(inventoryRepository.findAll()).thenReturn(inventories);
        when(itemService.SurgePricing(any(Item.class))).thenAnswer(a -> a.getArgument(0));

        List<Item> availableItems = inventoryService.getAvailableItems();

        Assertions.assertThat(availableItems.size()).isEqualTo(items.size() - 1);
    }

    @Test
    void when_getAll_returns_all_one_inventory_is_0() {
        List<Item> items = createItems();
        List<Inventory> inventories = createInventories(items);
        inventories.get(0).setAmount(0);
        when(inventoryRepository.findAll()).thenReturn(inventories);

        List<Inventory> availableItems = inventoryService.getAll();

        Assertions.assertThat(availableItems.size()).isEqualTo(inventories.size());
    }

    @Test
    void when_purchase_success() {
        Item item = new Item("Name", "Desc", 100);
        Inventory inventory = new Inventory(item, 200);
        Order order = new Order(item, 100);

        when(inventoryRepository.getFirstByItem(item))
                .thenReturn(inventory);

        inventoryService.purchase(order);

        verify(inventoryRepository, times(1)).save(any(Inventory.class));
    }

    @Test
    void when_purchase_throws_PURCHASE_AMOUNT_MUST_BIGGER_THAN_0() {
        Item item = new Item("Name", "Desc", 100);
        Order order = new Order(item, -300);

        RuntimeException ex = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> inventoryService.purchase(order));

        Assertions.assertThat(ex.getMessage()).isEqualTo(ErrorMessage.PURCHASE_AMOUNT_MUST_BIGGER_THAN_Zero.getMessage());
    }

    @Test
    void when_purchase_throws_INSUFFICIENT_INVENTORY() {
        Item item = new Item("Name", "Desc", 100);
        Inventory inventory = new Inventory(item, 200);
        Order order = new Order(item, 299);

        when(inventoryRepository.getFirstByItem(item)).thenReturn(inventory);
        RuntimeException ex = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> inventoryService.purchase(order));

        Assertions.assertThat(ex.getMessage()).isEqualTo(ErrorMessage.INSUFFICIENT_INVENTORY.getMessage());
    }

    @Test
    void when_purchase_throws_INVENTORY_NOT_FOUND() {
        Item item = new Item("Name", "Desc", 100);
        Order order = new Order(item, 300);

        when(inventoryRepository.getFirstByItem(item))
                .thenReturn(null);
        RuntimeException ex = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> inventoryService.purchase(order));

        Assertions.assertThat(ex.getMessage()).isEqualTo(ErrorMessage.INVENTORY_NOT_FOUND.getMessage());
    }
}