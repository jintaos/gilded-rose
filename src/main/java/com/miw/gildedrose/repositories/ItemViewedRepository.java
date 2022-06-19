package com.miw.gildedrose.repositories;

import com.miw.gildedrose.domain.Item;
import com.miw.gildedrose.domain.ItemViewed;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ItemViewedRepository extends CrudRepository<ItemViewed, Long> {

    int countByItemNameAndViewAtAfter(String name, LocalDateTime after);
}
