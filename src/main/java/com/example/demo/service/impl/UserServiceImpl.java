package com.example.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.annotation.MyLog;
import com.example.demo.constant.UserConstant;
import com.example.demo.mapper.UserMapper;
import com.example.demo.pojo.User;
import com.example.demo.pojo.UserDTO;
import com.example.demo.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public void addUser(UserDTO userDTO) {

        User user = BeanUtil.copyProperties(userDTO, User.class);
        save(user);
        stringRedisTemplate.opsForValue().set(UserConstant.USER_ID_KEY + user.getId(),
                JSONUtil.toJsonStr(user), UserConstant.USER_ID_TTL, TimeUnit.MINUTES);
    }

    @Override
    public User getUserById(Integer id) {
        String key = UserConstant.USER_ID_KEY + id;
        String userString = stringRedisTemplate.opsForValue().get(key);
        User user = null;
        if (StrUtil.isNotBlank(userString)) {
            user = JSONUtil.toBean(userString, User.class);
        } else {
            String lockKey = "cache:lock:" + id;
            boolean isLock = lock(lockKey);
            try {
                if (!isLock) {
                    Thread.sleep(100);
                    return getUserById(id);
                } else {
                    user = lambdaQuery().eq(User::getId, id).one();
                    stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(user == null ? "" : user),
                            UserConstant.USER_ID_TTL, TimeUnit.MINUTES);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                unLock(lockKey);
            }
        }
        return user;
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        lock.lock();
        try {
            updateById(user);
            stringRedisTemplate.delete(UserConstant.USER_ID_KEY + user.getId());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<User> getList() {
        String key = "cache:user:list";
        String userListString  = stringRedisTemplate.opsForValue().get(key);

        List<User> list = null;
        if (StrUtil.isNotBlank(userListString)) {
            list = JSONUtil.toList(userListString, User.class);
        } else {
            list = list();
            stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(list == null ? "" : list));
        }
        return list;
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

    private boolean lock(String key) {
        Boolean b = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(b);
    }

    public void unLock(String key) {
        stringRedisTemplate.delete(key);
    }


}
