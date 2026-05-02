package com.example.springsecurityjwtcrud.repository;

import com.example.springsecurityjwtcrud.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwner_IdOrderByIdAsc(Long ownerId);
}
