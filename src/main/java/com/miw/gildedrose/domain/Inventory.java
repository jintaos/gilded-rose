package com.miw.gildedrose.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The ID of inventory")
    private Long id;

    @OneToOne
    @Schema(description = "The item of inventory")
    private Item item;

    @Schema(description = "Current amount of inventory")
    private int amount;

    public Inventory(Item item, int amount) {
        this.item = item;
        this.amount = amount;
    }
}
