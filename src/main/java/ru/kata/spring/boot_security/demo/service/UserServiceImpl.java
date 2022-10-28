package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.exceptions.UserException;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.List;


@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
 private final RoleServiceImpl roleService;
 private final UserRepository userRepository;
 private final RoleRepository roleRepository;

 private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

 @Autowired
 public UserServiceImpl(RoleServiceImpl roleService, UserRepository userRepository, RoleRepository roleRepository) {
  this.roleService = roleService;
  this.userRepository = userRepository;

  this.roleRepository = roleRepository;
 }

 public List<User> getAllUsers() {
  return userRepository.findAll();
 }

 @Transactional
 public void saveUser(User user) {
  if (userRepository.findByUsername(user.getUsername()) != null &&
          userRepository.findByUsername(user.getUsername()).getId() != user.getId()) {
   throw new UserException(user);
  }
  if (user.getId() != null &&
          userRepository.findById(user.getId()).get().getPassword().equals(user.getPassword())) {
   user.setPassword(userRepository.findById(user.getId()).get().getPassword());
  } else {
   user.setPassword(passwordEncoder.encode(user.getPassword()));
  }
  userRepository.save(user);
 }


@Transactional
 public void removeUserById(Integer id) {
  userRepository.deleteById(id);
 }

 public User getUserById(Integer id) {
  return userRepository.getById(id);
 }
@Transactional
 public User findByUsername(String username) {
  return userRepository.findByUsername(username);
 }

 public List<Role> listRoles() {
  return roleService.getAllRoles();
 }



 //Добавление пользователя для теста функционала
 @PostConstruct
 public User createTestAdmin() {
  if (userRepository.findByUsername("admin") == null) {
   User user = new User("admin", "adminov", 22);
   user.setId(1);
   user.setPassword(passwordEncoder.encode("admin"));
   user.addRole(new Role(user.getId(), "ROLE_ADMIN"));
   user.addRole(new Role(user.getId(), "ROLE_USER"));
   return userRepository.save(user);
  }
  return null;
 }
}
