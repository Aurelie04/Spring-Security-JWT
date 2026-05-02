package com.example.springsecurityjwtcrud.controller;

import com.example.springsecurityjwtcrud.model.Item;
import com.example.springsecurityjwtcrud.security.AuthUserDetails;
import com.example.springsecurityjwtcrud.service.ItemService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Item create(@AuthenticationPrincipal AuthUserDetails principal, @Valid @RequestBody Item item) {
        return itemService.create(principal.getUser(), item);
    }

    @GetMapping
    public List<Item> getAll(@AuthenticationPrincipal AuthUserDetails principal) {
        return itemService.findAllForOwner(principal.getUser());
    }
}
