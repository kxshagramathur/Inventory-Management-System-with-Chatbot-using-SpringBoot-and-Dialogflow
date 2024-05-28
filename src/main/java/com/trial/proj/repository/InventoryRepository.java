package com.trial.proj.repository;

import com.trial.proj.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryItem, Integer>{

    List<InventoryItem> findByCategory(String category);
}
