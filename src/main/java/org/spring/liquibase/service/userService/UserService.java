package org.spring.liquibase.service.userService;

import org.spring.liquibase.entity.User;

import java.util.Optional;

public interface UserService {
    User saveUser(User user);

    User getUserById(long userId);

    void deleteUser(long userId);

    User updateUser(long userId, User user);

    User getUserByUsername(String username);
}
