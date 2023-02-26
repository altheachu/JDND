package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CartControllerTest {

    private CartController cartController;
    private CartRepository cartRepository = mock(CartRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup(){
        cartController = new CartController();
        TestUtils.injectObject(cartController,"cartRepository",cartRepository);
        TestUtils.injectObject(cartController,"userRepository",userRepository);
        TestUtils.injectObject(cartController,"itemRepository",itemRepository);
    }

    @Test
    public void add_cart_happy_path(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("testUsername");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(5);
        User user = CartControllerTest.getUser(modifyCartRequest.getUsername());
        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(user);
        when(itemRepository.findById(modifyCartRequest.getItemId())).thenReturn(Optional.of(CartControllerTest.getItem(modifyCartRequest.getItemId())));
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertEquals(200,response.getStatusCodeValue());
        Cart c = response.getBody().getUser().getCart();
        assertEquals(modifyCartRequest.getUsername(), c.getUser().getUsername());
        List<Item> itemList = c.getUser().getCart().getItems();
        for(int i = 0; i < itemList.size(); i++){
            assertEquals(modifyCartRequest.getItemId(), (long) itemList.get(i).getId());
        }
        assertEquals(modifyCartRequest.getQuantity(), c.getUser().getCart().getItems().size());
    }

    @Test
    public void add_cart_fail_path(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("testUsername");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(5);
        when(userRepository.findByUsername("testUsername")).thenReturn(null);
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertEquals(404,response.getStatusCodeValue());
    }

    @Test
    public void add_cart_fail_path_2(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("testUsername");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(5);
        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(CartControllerTest.getUser(modifyCartRequest.getUsername()));
        when(itemRepository.findById(modifyCartRequest.getItemId())).thenReturn(Optional.ofNullable(null));
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertEquals(404,response.getStatusCodeValue());
    }

    @Test
    public void remove_cart_happy_path(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("testUsername");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(5);
        User user = CartControllerTest.getUser(modifyCartRequest.getUsername());
        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(user);
        when(itemRepository.findById(modifyCartRequest.getItemId())).thenReturn(Optional.of(CartControllerTest.getItem(modifyCartRequest.getItemId())));
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        Cart c = response.getBody().getUser().getCart();

        ModifyCartRequest modifyCartRequest2 = new ModifyCartRequest();
        modifyCartRequest2.setUsername(modifyCartRequest.getUsername());
        modifyCartRequest2.setItemId(1);
        modifyCartRequest2.setQuantity(2);
        ResponseEntity<Cart> response2 = cartController.removeFromcart(modifyCartRequest2);
        Cart c2 = response2.getBody().getUser().getCart();

        assertEquals(modifyCartRequest.getUsername(), c2.getUser().getUsername());
        List<Item> itemList = c2.getUser().getCart().getItems();
        for(int i = 0; i < itemList.size(); i++){
            assertEquals(modifyCartRequest.getItemId(), (long) itemList.get(i).getId());
        }
        assertEquals(modifyCartRequest.getQuantity()-modifyCartRequest2.getQuantity(), c.getUser().getCart().getItems().size());
    }

    @Test
    public void remove_cart_fail_path(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("testUsername");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(5);
        when(userRepository.findByUsername("testUsername")).thenReturn(null);
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        assertEquals(404,response.getStatusCodeValue());
    }

    @Test
    public void remove_cart_fail_path_2(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("testUsername");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(2);
        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(CartControllerTest.getUser(modifyCartRequest.getUsername()));
        when(itemRepository.findById(modifyCartRequest.getItemId())).thenReturn(Optional.ofNullable(null));
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        assertEquals(404,response.getStatusCodeValue());
    }
    private static User getUser(String username){
        User u = new User();
        u.setId(1);
        u.setUsername(username);
        u.setPassword("1234");
        u.setCart(new Cart(u));
        return u;
    }

    private static Item getItem(long id){
        Item i = new Item();
        i.setId(Long.parseLong("1"));
        i.setName("Round Widget");
        i.setDescription("A widget that is round");
        i.setPrice(BigDecimal.valueOf(2.99));
        return i;
    }
}
