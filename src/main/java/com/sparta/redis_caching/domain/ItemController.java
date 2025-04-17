package com.sparta.redis_caching.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@RequestBody ItemDto itemDto) {
        return itemService.create(itemDto);
    }

    @GetMapping
    public List<ItemDto> findAll() {
        return itemService.readAll();
    }

    @GetMapping("/{id}")
    public ItemDto readOne(@PathVariable("id") Long id) {
        return itemService.readOne(id);
    }

    @PutMapping("/{id}")
    public ItemDto update(@PathVariable("id") Long id, @RequestBody ItemDto itemDto) {
        return itemService.update(id, itemDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        itemService.delete(id);
    }
}
