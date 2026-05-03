package com.example.springsecurityjwtcrud.repository;

import com.example.springsecurityjwtcrud.model.Item;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwner_IdOrderByIdAsc(Long ownerId);

    Optional<Item> findByIdAndOwner_Id(Long id, Long ownerId);
}
