package ru.kata.spring.boot_security.demo.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminRESTController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public AdminRESTController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }



    @GetMapping("/getRoles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return new ResponseEntity<>(userService.listRoles(), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public User getUser(@PathVariable(name = "id") Integer id) {
        return userService.getUserById(id);
    }

    @GetMapping()
    public ResponseEntity<List<User>> allUsers() {
        List<User> users = userService.getAllUsers();
        return users != null && !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping()
    public ResponseEntity<User> newUser(@RequestBody User user) {
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable("id") Integer id) {
        userRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
