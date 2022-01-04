package com.example.demo.hello;

import com.example.demo.hello.dto.User;
import com.example.demo.hello.dto.UserVO;
import com.example.demo.hello.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Api
@RestController
public class HelloController {

    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate redisTemplate;

    @GetMapping("/info/redis")
    public Object getRedis(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @PostMapping("/update/redis")
    public Object updateRedis(String key, String value) {
        redisTemplate.opsForValue().set(key, value, 5, TimeUnit.MINUTES);
        return redisTemplate.opsForValue().get(key);
    }

    @GetMapping("/list/user")
    public List<UserVO> listUser() {
        return userService.findAll();
    }

    @PostMapping("/update/user")
    public String updateUserName(User user) {
        return userService.updateUserName(user);
    }

    @PostMapping("/insert/user")
    public String insertUser(@RequestBody User user) {
        return userService.insert(user);
    }
}