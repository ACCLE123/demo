package com.example.demo.controller;

import com.example.demo.pojo.User;
import com.example.demo.pojo.UserDTO;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
//    @CachePut(cacheNames = "userCache", key = "#userDTO.name")
    public void addUser(@RequestBody UserDTO userDTO) {
        userService.addUser(userDTO);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @GetMapping
    @Cacheable(cacheNames = "userCache", key = "#name")
    public User getUserByName(@RequestParam String name) {
        log.info("getUserByName: {}", name);
        return userService.getUserByName(name);
    }

    @DeleteMapping
    @CacheEvict(cacheNames = "userCache", key = "#user.name")
    public void deleteUser(@RequestBody User user) {
        log.info("deleteUser {}", user);
        userService.deleteUser(user);
    }

    @GetMapping("/name/{id}")
    public String getUserNameById(@PathVariable Integer id) {
        log.info("getUserNameById: {}", id);
        return userService.getUserNameById(id);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("update {}", user);
        return userService.updateUser(user);
    }
}
