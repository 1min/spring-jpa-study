package org.example.init.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    public void editItem(Item item) {
        itemRepository.save(item);
    }

    public void deleteItem(Integer id) {
        itemRepository.deleteById(id);
    }
}
