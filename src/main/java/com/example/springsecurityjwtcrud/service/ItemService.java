package com.example.springsecurityjwtcrud.service;

import com.example.springsecurityjwtcrud.model.AppUser;
import com.example.springsecurityjwtcrud.model.Item;
import com.example.springsecurityjwtcrud.repository.ItemRepository;
import java.util.List;
import org.springframework.stereotype.Service;

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
}
