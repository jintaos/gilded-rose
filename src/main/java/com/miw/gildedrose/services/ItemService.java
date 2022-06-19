package com.miw.gildedrose.services;

import com.miw.gildedrose.domain.Item;

import java.util.Optional;

public interface ItemService {
    Optional<Item>  GetItemByName(String name);
    Item SurgePricing(Item item);
}