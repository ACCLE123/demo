package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.pojo.User;
import com.example.demo.pojo.UserDTO;

import java.util.List;

public interface UserService extends IService<User> {
    void addUser(UserDTO userDTO);

    User getUserById(Integer id);

    User getUserByName(String name);

    void deleteUser(User user);

    String getUserNameById(Integer id);

    void updateUser(User user);

    List<User> getList();
}
