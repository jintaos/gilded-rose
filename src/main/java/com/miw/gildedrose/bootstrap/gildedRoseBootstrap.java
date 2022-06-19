package com.miw.gildedrose.bootstrap;

import com.miw.gildedrose.domain.Inventory;
import com.miw.gildedrose.domain.Item;
import com.miw.gildedrose.repositories.InventoryRepository;
import com.miw.gildedrose.repositories.ItemRepository;
import com.miw.gildedrose.repositories.ItemViewedRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class gildedRoseBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private final ItemRepository itemRepository;
    private final ItemViewedRepository itemViewedRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        List<Item> items = List.of(
                new Item("Google Pixel 6", "Google Pixel 6, 5G Android Phone", 750),
                new Item("Samsung Galaxy A03s", "Samsung Galaxy A03s Black 32GB", 169),
                new Item("Samsung Galaxy S22", "Samsung Galaxy S22 Ultra 5G Black", 154),
                new Item("Apple iPhone 8", "Apple iPhone 8, GSM Unlocked, 64GB", 217),
                new Item("Apple iPhone 13", "Apple iPhone 13, GSM Unlocked, 64GB", 1417)

        );

        // When app started, all inventories of items are 100
        for (Item item : items) {
            String itemName = item.getName();

            itemRepository.findById(itemName).ifPresentOrElse(
                    i -> {
                    },
                    () -> {
                        log.info("Save item: {}", itemName);
                        itemRepository.save(item);
                    });

            int amount = 100;
            Optional.ofNullable(inventoryRepository.getFirstByItem(item)).ifPresentOrElse(
                    inventory -> {
                        inventory.setAmount(amount);
                        inventoryRepository.save(inventory);
                    }, () -> {
                        Inventory inventory = new Inventory(item, amount);
                        inventoryRepository.save(inventory);
                    });
        }

        // clean item viewed history
        itemViewedRepository.deleteAll();
    }
}
