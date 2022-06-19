package com.miw.gildedrose.services;

import com.miw.gildedrose.domain.Inventory;
import com.miw.gildedrose.domain.Item;
import com.miw.gildedrose.domain.Order;

import java.util.List;

public interface InventoryService {

    List<Item> getAvailableItems();

    List<Inventory> getAll();

    void purchase(Order order);
}
