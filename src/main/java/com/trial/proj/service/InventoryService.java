package com.trial.proj.service;

import com.trial.proj.model.InventoryItem;
import com.trial.proj.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public InventoryItem createItem(InventoryItem item) {
        return inventoryRepository.save(item);
    }

    public Optional<InventoryItem> getItemById(Integer id) {
        return inventoryRepository.findById(id);
    }

    public List<InventoryItem> getAllItems() {
        return inventoryRepository.findAll();
    }

    public InventoryItem updateItem(Integer id, InventoryItem itemDetails) {
        InventoryItem item = inventoryRepository.findById(id).orElseThrow();
        item.setCategory(itemDetails.getCategory());
        item.setProd_name(itemDetails.getProd_name());
        item.setDescription(itemDetails.getDescription());
        item.setPrice(itemDetails.getPrice());
        return inventoryRepository.save(item);
    }

    public void deleteItem(Integer id) {
        InventoryItem item = inventoryRepository.findById(id).orElseThrow();
        inventoryRepository.delete(item);
    }

    public InventoryItem getRandomItemByCategory(String category) {
        List<InventoryItem> items = inventoryRepository.findByCategory(category);
        if (items.isEmpty()) {
            return null;
        }
        Random random = new Random();
        return items.get(random.nextInt(items.size()));
    }

    public InventoryItem getRandomPhone() {
        List<InventoryItem> phones = inventoryRepository.findByCategory("phone");
        if (phones.isEmpty()) {
            return null;
        }
        Random rand = new Random();
        return phones.get(rand.nextInt(phones.size()));
    }

}
