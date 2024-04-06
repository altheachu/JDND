package com.example.demo.controllers;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.example.demo.exception.OperationAbsentItemsException;
import com.example.demo.model.requests.BatchDeleteItemsRequest;
import com.example.demo.utils.ExceptionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

@RestController
@RequestMapping("/api/item")
public class ItemController {

	@Autowired
	private ItemRepository itemRepository;
	
	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		return ResponseEntity.ok(itemRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		return ResponseEntity.of(itemRepository.findById(id));
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		List<Item> items = itemRepository.findByName(name);
		return items == null || items.isEmpty() ? ResponseEntity.notFound().build()
				: ResponseEntity.ok(items);
			
	}

	@DeleteMapping("/deleteByIds")
	public ResponseEntity<Void> deleteItems(@RequestBody BatchDeleteItemsRequest request){
		List<Long> existIds = itemRepository.findAll().stream().map(Item::getId).collect(Collectors.toList());
		List<Long> ids = request.getIds();
		List<Long> absentIds = ids.stream().filter(t->!existIds.contains(t)).collect(Collectors.toList());

		if(!absentIds.isEmpty()){
			throw new OperationAbsentItemsException(absentIds, "/api/item/deleteByIds");
		}
		ids.forEach(t->itemRepository.deleteById(t));
		return ResponseEntity.noContent().build();
	}


}
