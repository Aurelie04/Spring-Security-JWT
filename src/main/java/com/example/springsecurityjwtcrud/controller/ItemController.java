package com.example.springsecurityjwtcrud.controller;

import com.example.springsecurityjwtcrud.dto.ItemUpdateRequest;
import com.example.springsecurityjwtcrud.model.Item;
import com.example.springsecurityjwtcrud.security.AuthUserDetails;
import com.example.springsecurityjwtcrud.service.ItemService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PutMapping("/{id}")
    public Item update(
            @AuthenticationPrincipal AuthUserDetails principal,
            @PathVariable Long id,
            @Valid @RequestBody ItemUpdateRequest body) {
        return itemService.update(principal.getUser(), id, body.name(), body.description());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUserDetails principal, @PathVariable Long id) {
        itemService.delete(principal.getUser(), id);
    }
}
