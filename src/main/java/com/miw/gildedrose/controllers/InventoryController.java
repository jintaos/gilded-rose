package com.miw.gildedrose.controllers;

import com.miw.gildedrose.domain.Inventory;
import com.miw.gildedrose.domain.Order;
import com.miw.gildedrose.services.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@AllArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping(path = "/Inventories")
    @Operation(
            summary = "List current inventories",
            security = {
                    @SecurityRequirement(name = "permissions", scopes = {""}),
                    @SecurityRequirement(name = "roles", scopes = {""})
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found"),
                    @ApiResponse(responseCode = "405", description = "Method Not Allowed"),
                    @ApiResponse(responseCode = "422", description = "Validation Error"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
                    @ApiResponse(responseCode = "503", description = "Service Unavailable"),
            })
    public ResponseEntity<List<Inventory>> getInventories(
            @Parameter(name = "Authorization", required = true) @RequestHeader(value = "Authorization", required = false) String Authorization) {
        return new ResponseEntity<>(inventoryService.getAll(), HttpStatus.OK);
    }

    @PostMapping(path = "/purchase")
    @Operation(
            summary = "Submit an order",
            security = {
                    @SecurityRequirement(name = "permissions", scopes = {""}),
                    @SecurityRequirement(name = "roles", scopes = {""})
            },
            responses = {
                    @ApiResponse(responseCode = "201", description = "Order submitted"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found"),
                    @ApiResponse(responseCode = "405", description = "Method Not Allowed"),
                    @ApiResponse(responseCode = "422", description = "Validation Error"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
                    @ApiResponse(responseCode = "503", description = "Service Unavailable"),
            })
    public ResponseEntity<?> purchase(
            @Parameter(name = "Authorization", required = true) @RequestHeader(value = "Authorization", required = false) String authorization,
            @NotNull @RequestBody() Order order) {
        inventoryService.purchase(order);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
