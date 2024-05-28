package com.trial.proj.controller;


import com.trial.proj.model.InventoryItem;
import com.trial.proj.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/items")
    public InventoryItem createItem(@RequestBody InventoryItem item) {
        return inventoryService.createItem(item);
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<InventoryItem> getItemById(@PathVariable Integer id) {
        Optional<InventoryItem> item = inventoryService.getItemById(id);
        return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/items")
    public List<InventoryItem> getAllItems() {
        return inventoryService.getAllItems();
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<InventoryItem> updateItem(@PathVariable Integer id, @RequestBody InventoryItem itemDetails) {
        try {
            InventoryItem updatedItem = inventoryService.updateItem(id, itemDetails);
            return ResponseEntity.ok(updatedItem);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Integer id) {
        try {
            inventoryService.deleteItem(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/dialogflow/webhook")
    public Map<String, Object> handleDialogflowWebhook(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> queryResult = (Map<String, Object>) request.get("queryResult");
        Map<String, Object> intent = (Map<String, Object>) queryResult.get("intent");
        String intentName = (String) intent.get("displayName");

        if ("phone_recommendation intent".equals(intentName)) {
            InventoryItem item = inventoryService.getRandomPhone();
            if (item != null) {
                String fulfillmentText = String.format("I recommend the %s: %s. It costs $%.2f.",
                        item.getCategory(), item.getProd_name(), item.getPrice());
                response.put("fulfillmentText", fulfillmentText);
            } else {
                response.put("fulfillmentText", "Sorry, I couldn't find any phones in the inventory.");
            }
        }

        return response;
    }
}
