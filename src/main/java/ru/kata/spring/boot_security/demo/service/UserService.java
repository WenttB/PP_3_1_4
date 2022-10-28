package ru.kata.spring.boot_security.demo.service;



import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();
    void saveUser(User user);
    void removeUserById(Integer id);
    User getUserById(Integer id);
    User findByUsername(String username);
    List<Role> listRoles();
}
