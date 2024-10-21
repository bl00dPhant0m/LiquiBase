package org.spring.liquibase.controller;

import lombok.RequiredArgsConstructor;
import org.spring.liquibase.entity.User;
import org.spring.liquibase.service.userService.UserService;
import org.spring.liquibase.service.userService.UserServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserServiceImpl userService;

    @PostMapping("/add")
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/id")
    public void deleteUserById(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }

    @PatchMapping("/{id}")
    public User updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }


}
