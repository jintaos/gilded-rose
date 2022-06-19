package com.miw.gildedrose.services;

import com.miw.gildedrose.domain.Inventory;
import com.miw.gildedrose.domain.Item;
import com.miw.gildedrose.domain.ItemViewed;
import com.miw.gildedrose.domain.Order;
import com.miw.gildedrose.exception.ErrorMessage;
import com.miw.gildedrose.repositories.InventoryRepository;
import com.miw.gildedrose.repositories.ItemViewedRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@AllArgsConstructor
public class InventoryServiceImp implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ItemService itemService;
    private final ItemViewedRepository itemViewedRepository;

    @Override
    public List<Item> getAvailableItems() {
        List<Item> items = StreamSupport.stream(inventoryRepository.findAll().spliterator(), false)
                .filter(inventory -> inventory.getAmount() > 0)
                .map(Inventory::getItem)
                .map(itemService::SurgePricing)
                .collect(Collectors.toList());

        for (Item item : items) {
            // insert item viewed for surge pricing
            ItemViewed itemViewed = new ItemViewed(item.getName(), LocalDateTime.now());
            itemViewedRepository.save(itemViewed);
        }
        return items;
    }

    @Override
    public List<Inventory> getAll() {
        return StreamSupport.stream(inventoryRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public void purchase(Order order) {
        if (order.getAmount() < 1)
        {
            throw new RuntimeException(ErrorMessage.PURCHASE_AMOUNT_MUST_BIGGER_THAN_Zero.getMessage());
        }

        Optional.ofNullable(inventoryRepository.getFirstByItem(order.getItem()))
                .ifPresentOrElse(
                        inventory -> {
                            if (inventory.getAmount() > order.getAmount()) {
                                inventory.setAmount(inventory.getAmount() - order.getAmount());
                                inventoryRepository.save(inventory);
                            } else {
                                throw new RuntimeException(ErrorMessage.INSUFFICIENT_INVENTORY.getMessage());
                            }
                        },
                        () -> {
                            throw new RuntimeException(ErrorMessage.INVENTORY_NOT_FOUND.getMessage());
                        }

                );
    }
}
