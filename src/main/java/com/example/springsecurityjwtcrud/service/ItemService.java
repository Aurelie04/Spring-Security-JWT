package com.example.springsecurityjwtcrud.service;

import com.example.springsecurityjwtcrud.model.AppUser;
import com.example.springsecurityjwtcrud.model.Item;
import com.example.springsecurityjwtcrud.repository.ItemRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item create(AppUser owner, Item item) {
        item.setId(null);
        item.setOwner(owner);
        return itemRepository.save(item);
    }

    public List<Item> findAllForOwner(AppUser owner) {
        return itemRepository.findByOwner_IdOrderByIdAsc(owner.getId());
    }

    @Transactional
    public Item update(AppUser owner, Long id, String name, String description) {
        Item existing =
                itemRepository
                        .findByIdAndOwner_Id(id, owner.getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
        existing.setName(name.strip());
        if (description == null || description.isBlank()) {
            existing.setDescription(null);
        } else {
            existing.setDescription(description.strip());
        }
        return itemRepository.save(existing);
    }

    @Transactional
    public void delete(AppUser owner, Long id) {
        Item existing =
                itemRepository
                        .findByIdAndOwner_Id(id, owner.getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
        itemRepository.delete(existing);
    }
}
