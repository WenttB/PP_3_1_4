package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    public void saveUser (User user);
    public List<User> allUsers();
    public void update (long id,User upUser);
    public User show (long id);
    public void delete (long id);
}
