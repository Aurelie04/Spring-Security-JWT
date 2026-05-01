package com.example.springsecurityjwtcrud.service;

import com.example.springsecurityjwtcrud.model.Item;
import com.example.springsecurityjwtcrud.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item create(Item item) {
        return itemRepository.save(item);
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }
}
