package com.miw.gildedrose.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item  {
    @Id
    @Schema(description = "The name of item")
    public String name;
    @Lob
    @Schema(description = "The description of item")
    public String description;
    @Schema(description = "The price of item")
    public int price;

}
