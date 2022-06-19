package com.miw.gildedrose.services;

import com.miw.gildedrose.domain.Item;
import com.miw.gildedrose.domain.ItemViewed;
import com.miw.gildedrose.repositories.ItemRepository;
import com.miw.gildedrose.repositories.ItemViewedRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ItemServiceImp implements ItemService {
    private static final int SurgePricingViewedThreshold = 10;
    private final ItemRepository itemRepository;
    private final ItemViewedRepository itemViewedRepository;

    @Override
    public Optional<Item> GetItemByName(String name) {
        Optional<Item> itemOptional = itemRepository.findById(name);

        if (itemOptional.isPresent()) {
            // surge pricing
            itemOptional = Optional.of(SurgePricing(itemOptional.get()));
            // insert item viewed for surge pricing
            itemViewedRepository.save(new ItemViewed(name, LocalDateTime.now()));
        }
        return itemOptional;
    }

    @Override
    public Item SurgePricing(@NonNull Item item) {
        int viewedCount = itemViewedRepository.countByItemNameAndViewAtAfter(item.getName(), LocalDateTime.now().minusHours(1));
        return viewedCount > SurgePricingViewedThreshold
                ? new Item(item.getName(), item.getDescription(), (int) (item.getPrice() * 1.1))
                : item;
    }
}
