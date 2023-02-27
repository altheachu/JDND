package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setup(){
        orderController = new OrderController();
        TestUtils.injectObject(orderController,"userRepository", userRepository);
        TestUtils.injectObject(orderController,"orderRepository", orderRepository);
    }

    @Test
    public void submit_order_happy_path(){
        String username = "testUsername";
        User u = OrderControllerTest.getUser(username);
        when(userRepository.findByUsername(username)).thenReturn(u);
        final ResponseEntity<UserOrder> orderResult = orderController.submit(username);
        assertEquals(200, orderResult.getStatusCodeValue());
        UserOrder order = orderResult.getBody();
        assertEquals(u.getUsername(), order.getUser().getUsername());
        assertEquals(u.getCart().getTotal(), order.getTotal());
        assertArrayEquals(u.getCart().getItems().toArray(new Item[0]),order.getItems().toArray(new Item[0]));
    }

    @Test
    public void submit_order_fail_path(){
        String username = "notExistingUsername";
        when(userRepository.findByUsername(username)).thenReturn(null);
        final ResponseEntity<UserOrder> orderResult = orderController.submit(username);
        assertEquals(404, orderResult.getStatusCodeValue());
    }

    @Test
    public void find_order_happy_path(){

        String username = "testUsername";
        User u = OrderControllerTest.getUser(username);
        when(userRepository.findByUsername(username)).thenReturn(u);
        when(userRepository.findByUsername(username)).thenReturn(u);
        final ResponseEntity<UserOrder> orderResult = orderController.submit(username);
        UserOrder order = orderResult.getBody();
        List<UserOrder> orders = OrderControllerTest.getOrders(order);
        when(orderRepository.findByUser(u)).thenReturn(orders);
        final ResponseEntity<List<UserOrder>> orderListResult = orderController.getOrdersForUser(username);
        List<UserOrder> resultOrders = orderListResult.getBody();
        assertArrayEquals(orders.toArray(new UserOrder[0]),resultOrders.toArray(new UserOrder[0]));
    }

    @Test
    public void find_order_fail_path(){
        String username = "notExistingUsername";
        when(userRepository.findByUsername(username)).thenReturn(null);
        final ResponseEntity<List<UserOrder>> orderResult = orderController.getOrdersForUser(username);
        assertEquals(404, orderResult.getStatusCodeValue());
    }
    private static User getUser(String username){
        User u = new User();
        u.setId(1);
        u.setUsername(username);
        u.setPassword("testPassword");

        Cart cart = new Cart();
        cart.setId((long)1);
        cart.setUser(u);

        List<Item> items = Arrays.asList(
                new Item(Long.parseLong("1"),"Round Widget", BigDecimal.valueOf(2.99),"A widget that is round")
        );
        cart.setItems(items);
        cart.setTotal(BigDecimal.valueOf(2.99));
        u.setCart(cart);
        return u;
    }

    private static List<UserOrder> getOrders(UserOrder userOrder){
        return Arrays.asList(userOrder);
    }

}
