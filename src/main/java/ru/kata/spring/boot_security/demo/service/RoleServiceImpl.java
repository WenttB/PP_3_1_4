package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;


import javax.transaction.Transactional;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findRoleByName(name);
    }

    @Override
    @Transactional
    public void saveRole(Role role) {
        roleRepository.save(role);
    }
}
