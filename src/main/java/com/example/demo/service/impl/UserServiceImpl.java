package com.example.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.annotation.MyLog;
import com.example.demo.mapper.UserMapper;
import com.example.demo.pojo.User;
import com.example.demo.pojo.UserDTO;
import com.example.demo.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public void addUser(UserDTO userDTO) {
        save(BeanUtil.copyProperties(userDTO, User.class));
    }

    @Override
    public User getUserById(Integer id) {
        String key = "cache:user:" + id;
        String userString = stringRedisTemplate.opsForValue().get(key);
        User user = JSONUtil.toBean(userString, User.class);
        if (user.getId() == null) {
            user = lambdaQuery().eq(User::getId, id).one();
            stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(user));
        }
        return user;
    }

    @Override
    @MyLog
    public User getUserByName(String name) {
        User u =  query().eq("name", name).one();
        log.info("getUserByName service {}", u);
        return u;
    }

    @Override
    public void deleteUser(User user) {
        Integer id = user.getId();
        String name = user.getName();
        update().eq(name!= null, "name", name)
                .or()
                .eq(id != null, "id", id)
                .remove();
    }

    @Override
    public String getUserNameById(Integer id) {
        User one = lambdaQuery().select(User::getName).eq(User::getId, id).one();
        log.info("{}", one);
        return one.getName();
    }

    @Override
    public User updateUser(User user) {
        updateById(user);
        return user;
    }


}
