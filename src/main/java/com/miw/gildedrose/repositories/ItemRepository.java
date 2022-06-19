package com.miw.gildedrose.repositories;

import com.miw.gildedrose.domain.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, String> {
}
