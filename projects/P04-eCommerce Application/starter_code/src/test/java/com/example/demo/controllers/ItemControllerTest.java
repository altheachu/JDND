package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup(){
        itemController = new ItemController();
        TestUtils.injectObject(itemController,"itemRepository",itemRepository);
    }

    @Test
    public void find_items_happy_path(){
        List<Item> inputItems = ItemControllerTest.getItems();
        when(itemRepository.findAll()).thenReturn(inputItems);
        final ResponseEntity<List<Item>> itemResult = itemController.getItems();
        assertEquals(200,itemResult.getStatusCodeValue());
        assertArrayEquals(inputItems.toArray(new Item[0]), itemResult.getBody().toArray(new Item[0]));
    }

    @Test
    public void find_item_by_id_happy_path(){
        List<Item> inputItems = ItemControllerTest.getItems();
        for(int i = 0; i < 2; i++){
            when(itemRepository.findById(Long.parseLong(i+""))).thenReturn(Optional.of(inputItems.get(i)));
            final ResponseEntity<Item> itemResult = itemController.getItemById(Long.parseLong(i+""));
            assertEquals(200,itemResult.getStatusCodeValue());
            Item item =  itemResult.getBody();
            assertEquals(inputItems.get(i).getId(),item.getId());
            assertEquals(inputItems.get(i).getName(),item.getName());
            assertEquals(inputItems.get(i).getPrice(),item.getPrice());
            assertEquals(inputItems.get(i).getDescription(),item.getDescription());
        }
    }

    @Test
    public void find_item_by_name_happy_path(){
        List<Item> inputItems = ItemControllerTest.getItems();
        for(int i = 0; i < 2; i++){
            final int idx = i;
            List<Item> filteredInputItems = inputItems.stream().filter(t->t.getName().equals(inputItems.get(idx).getName())).collect(Collectors.toList());
            when(itemRepository.findByName(inputItems.get(i).getName())).thenReturn(filteredInputItems);
            final ResponseEntity<List<Item>> itemResult = itemController.getItemsByName(inputItems.get(i).getName());
            assertEquals(200,itemResult.getStatusCodeValue());
            List<Item> items =  itemResult.getBody();
            items.forEach(t->{
                assertEquals(inputItems.get(idx).getName(),t.getName());
            });
        }
    }

    @Test
    public void find_item_by_name_fail_path(){
        List<Item> inputItems = ItemControllerTest.getItems();
        for(int i = 0; i < 2; i++){
            final int idx = i;
            List<Item> filteredInputItems = inputItems.stream().filter(t->t.equals("test")).collect(Collectors.toList());
            when(itemRepository.findByName(inputItems.get(i).getName())).thenReturn(filteredInputItems);
            final ResponseEntity<List<Item>> itemResult = itemController.getItemsByName(inputItems.get(i).getName());
            assertEquals(404,itemResult.getStatusCodeValue());
        }
    }

    private static List<Item> getItems(){
        List<Item> items = Arrays.asList(
                new Item(Long.parseLong("1"),"Round Widget", BigDecimal.valueOf(2.99),"A widget that is round"),
                new Item(Long.parseLong("2"),"Square Widget", BigDecimal.valueOf(1.99),"A widget that is square"));
        return items;
    }
}
