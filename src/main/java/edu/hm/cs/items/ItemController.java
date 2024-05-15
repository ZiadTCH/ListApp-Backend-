package edu.hm.cs.items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
class ItemController {

    @Autowired
    private ItemRepository repository;

    @GetMapping("/items")
    List<Item> allItems() {
        return repository.findAll();
    }

    @PostMapping("/item")
    Item newItem(@RequestBody Item newItem) {
        return repository.save(newItem);
    }

    @GetMapping("/item/{id}")
    Item getItem(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
    }

    @DeleteMapping("/item/{id}")
    void deleteItem(@PathVariable Long id){
        if (repository.existsById(id)){
            repository.deleteById(id);
        } else {
            throw new ItemNotFoundException(id);
        }
    }

    @PutMapping("/item/{id}")
    public Item editItem(@RequestBody Item newItem, @PathVariable Long id) {
        return repository.findById(id)
                .map(item -> {
                    item.setName(newItem.getName());
                    item.setDescription(newItem.getDescription());
                    return repository.save(item);
                })
                .orElseGet(() -> {
                    newItem.setId(id);
                    return repository.save(newItem);
                });
    }

}
