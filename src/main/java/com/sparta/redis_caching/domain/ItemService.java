package com.sparta.redis_caching.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ItemService {
    private final ItemRepository itemRepository;

    // 생성하고 id를 키로
    // Write-Through
    @CachePut(cacheNames = "itemCache", key = "#result.id")
    public ItemDto create(ItemDto dto) {
        return ItemDto.fromEntity(itemRepository.save(
                Item.builder()
                        .name(dto.getName())
                        .description(dto.getDescription())
                        .price(dto.getPrice())
                        .build()
        ));
    }

    // itemAllCache::readAll
    @Cacheable(cacheNames = "itemAllCache", key = "methodName")
    public List<ItemDto> readAll() {
        return itemRepository.findAll()
                .stream()
                .map(ItemDto::fromEntity)
                .toList();
    }

    // 이 메서드의 결과는 캐싱이 가능하다 -> Cache-Aside
    // cacheNames: 적용할 캐시 규칙을 지정하기 위한 이름, 이 메서드로 인해서 만들어질 캐시를 지칭하는 이름
    // key: 캐시 데이터를 구분하기 위해 활용하는 값
    @Cacheable(cacheNames = "itemCache", key = "args[0]") // itemCache::1
    public ItemDto readOne(Long id) {
        return itemRepository.findById(id)
                .map(ItemDto::fromEntity)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    // 업데이트가 되었으면 캐시에 먼저 저장하고 DB에 저장
    @CachePut(cacheNames = "itemCache", key = "args[0]")
    // 그리고, readAll 의 캐시를 지워준다.
    @CacheEvict(cacheNames = "itemAllCache", allEntries = true)
    public ItemDto update(Long id, ItemDto dto) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setPrice(dto.getPrice());
        return ItemDto.fromEntity(itemRepository.save(item));
    }

    public void delete(Long id) {
        itemRepository.deleteById(id);
    }
}
