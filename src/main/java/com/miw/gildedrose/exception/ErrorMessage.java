package com.miw.gildedrose.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {
    INVENTORY_NOT_FOUND("Inventory Not Found"),
    INSUFFICIENT_INVENTORY("Insufficient Inventory"),
    PURCHASE_AMOUNT_MUST_BIGGER_THAN_Zero("Purchase Amount Must Bigger Than 0");


    private final String message;
}
