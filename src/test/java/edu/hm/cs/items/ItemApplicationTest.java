package edu.hm.cs.items;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class ItemApplicationTest {

    @Autowired
    private ItemController controller;

    private void initDB() {
        controller.newItem(new Item("Item with ID 1", "Item description"));
        controller.newItem(new Item("Item with ID 2", "Item description"));
        controller.newItem(new Item("Item with ID 3", "Item description"));
        controller.newItem(new Item("Item with ID 4", "Item description"));
    }
    @Test
    void testAllItemsDBEmpty() {
        List<Item> all = controller.allItems();
        Assertions.assertEquals(0, all.size());
    }
    @Test
    void testAllItemsDBNotEmpty() {
        initDB();
        List<Item> all = controller.allItems();
        Assertions.assertEquals(4, all.size());
    }
    @Test
    void testNewItemDBEmpty() {
        Item result = controller.newItem(new Item("Item", "Item description"));
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("Item", result.getName());
        Assertions.assertEquals("Item description", result.getDescription());
    }

    @Test
    void testNewItemDBNotEmpty() {
        initDB();
        Item result = controller.newItem(new Item("Item", "Item description"));
        Assertions.assertEquals(5, result.getId());
        Assertions.assertEquals("Item", result.getName());
        Assertions.assertEquals("Item description", result.getDescription());
    }

    @Test
    void DeleteItemDBNotEmpty() {
        initDB();
        controller.deleteItem(1L);
        List<Item> all = controller.allItems();
        Assertions.assertEquals(3, all.size());
        Assertions.assertThrows(ItemNotFoundException.class, () -> controller.getItem(1L));

    }

    @Test
    void DeleteItemDBEmpty() {
        initDB();
        Assertions.assertThrows(ItemNotFoundException.class, () -> controller.getItem(6L));
    }

    @Test
    void EditItemDBNotEmpty() {
        initDB();
        Item updatedItem = new Item("Geändertes Item", "Geänderte Beschreibung");
        Item result = controller.editItem(updatedItem, 1L); // Aktualisiert das Item mit ID 1
        Assertions.assertEquals("Geändertes Item", result.getName());
        Assertions.assertEquals("Geänderte Beschreibung", result.getDescription());
    }

    @Test
    void EditItemDBEmpty() {
        initDB();
        Item newItem = controller.newItem(new Item("Neues Item", "Beschreibung des neuen Items"));
        Item result = controller.editItem(newItem, 5L); // Versucht, ein nicht vorhandenes Item zu bearbeiten
        Assertions.assertEquals(5, result.getId());
        Assertions.assertEquals("Neues Item", result.getName());
        Assertions.assertEquals("Beschreibung des neuen Items", result.getDescription());
    }
}