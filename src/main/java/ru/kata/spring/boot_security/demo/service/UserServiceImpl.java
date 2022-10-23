package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService{
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername (String username) {
        return  userRepository.findByUsername(username);
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("Пользователя с именем '%s' не существует",username));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Transactional
    public void saveUser (User user) {
        user.setUsername(user.getUsername());
        user.setSurname(user.getSurname());
        user.setEmail(user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(new Role(user.getId(), "ROLE_USER")));
        userRepository.save(user);
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }
    @Transactional
    public void update (long id,User upUser) {
        User updateUser = show(id);
        updateUser.setUsername(upUser.getUsername());
        updateUser.setSurname(upUser.getSurname());
        updateUser.setEmail(upUser.getEmail());
        updateUser.setPassword(passwordEncoder.encode(upUser.getPassword()));
    }
    public User show (long id) {

        return userRepository.getById(id);
    }
    @Transactional
    public void delete (long id) {
        userRepository.deleteById(id);
    }
    @PostConstruct
    public User createTestAdmin() {
        if (userRepository.findByUsername("admin") == null) {
            User user = new User("admin", "test", "test@mail.com");
            user.setId(1);
            user.setPassword(passwordEncoder.encode("admin"));
            user.setRoles(Collections.singleton(new Role (user.getId(), "ROLE_ADMIN")));
            return userRepository.save(user);
        }
        return null;
    }



}
