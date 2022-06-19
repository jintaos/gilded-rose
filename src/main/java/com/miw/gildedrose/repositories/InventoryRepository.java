package com.miw.gildedrose.repositories;

import com.miw.gildedrose.domain.Inventory;
import com.miw.gildedrose.domain.Item;
import org.springframework.data.repository.CrudRepository;

public interface InventoryRepository extends CrudRepository<Inventory, Long> {
    Inventory getFirstByItem(Item item);
}
