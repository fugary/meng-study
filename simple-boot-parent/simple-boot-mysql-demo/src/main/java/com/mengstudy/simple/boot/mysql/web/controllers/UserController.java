package com.mengstudy.simple.boot.mysql.web.controllers;

import com.mengstudy.simple.boot.mysql.entity.User;
import com.mengstudy.simple.boot.mysql.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created on 2020/4/8 13:28 .<br>
 *
 * @author gary.fu
 */
@RequestMapping("/users")
@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping
    public List<User> users() {
        return userMapper.selectList(null);
    }

    @GetMapping("/{id}")
    public User get(@PathVariable("id") Long id) {
        return userMapper.selectById(id);
    }

    @PostMapping
    public User save(User user) {
        if (user.getId() == null) {
            userMapper.insert(user);
        } else {
            userMapper.update(user, null);
        }
        return user;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        userMapper.deleteById(id);
        return "success";
    }
}
