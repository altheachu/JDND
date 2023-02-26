package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
    @Before
    public void setup(){
        userController = new UserController();
        TestUtils.injectObject(userController,"userRepository",userRepository);
        TestUtils.injectObject(userController,"cartRepository",cartRepository);
        TestUtils.injectObject(userController,"bCryptPasswordEncoder",bCryptPasswordEncoder);
    }
    @Test
    public void create_user_happy_path(){
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("hashedPassword");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername(UserControllerTest.getUsername(1));
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");
        final ResponseEntity<User> response = userController.createUser(request);
        assertEquals(200,response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(u.getId(),0);
        assertEquals(u.getUsername(),UserControllerTest.getUsername(1));
        assertEquals(u.getPassword(),"hashedPassword");
    }

    @Test
    public void create_user_fail_path(){
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("hashedPassword");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername(UserControllerTest.getUsername(4));
        request.setPassword("00000");
        request.setConfirmPassword("00000");
        final ResponseEntity<User> response = userController.createUser(request);
        assertEquals(400,response.getStatusCodeValue());
    }
    @Test
    public void find_user_by_id_happy_path(){
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername(UserControllerTest.getUsername(2));
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");
        final ResponseEntity<User> response = userController.createUser(request);

        when(userRepository.findById(response.getBody().getId())).thenReturn(Optional.of(response.getBody()));
        final ResponseEntity<User> queryResponse = userController.findById(response.getBody().getId());
        assertEquals(200,queryResponse.getStatusCodeValue());
        assertEquals(response.getBody().getUsername(),queryResponse.getBody().getUsername());
    }

    @Test
    public void find_user_by_username_happy_path(){
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername(UserControllerTest.getUsername(3));
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");
        final ResponseEntity<User> response = userController.createUser(request);

        when(userRepository.findByUsername(response.getBody().getUsername())).thenReturn(response.getBody());
        final ResponseEntity<User> queryResponse = userController.findByUserName(response.getBody().getUsername());
        assertEquals(200,queryResponse.getStatusCodeValue());
        assertEquals(response.getBody().getUsername(),queryResponse.getBody().getUsername());
    }

    private static String getUsername(Integer i){
        return i.toString();
    }
}
