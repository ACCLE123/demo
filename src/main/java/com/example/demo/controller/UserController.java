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

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 新增用户
     * @param userDTO
     */
    @PostMapping
    public void addUser(@RequestBody UserDTO userDTO) {
        userService.addUser(userDTO);
    }

    /**
     * 获取全部用户
     * @return
     */
    @GetMapping
    public List<User> list() {
        List<User> list = userService.getList();
        return list;
    }
    /**
     * 通过id 获取用户
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    /**
     * 修改用户
     * @param user
     * @return
     */
    @PutMapping
    public void updateUser(@RequestBody User user) {
        userService.updateUser(user);
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


}
