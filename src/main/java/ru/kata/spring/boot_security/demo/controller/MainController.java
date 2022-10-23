package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;
@Controller
@RequestMapping(value = "/admin")
public class MainController {

    private final UserServiceImpl userService;
    @Autowired
    public MainController (UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String allUsers (Model model) {
        model.addAttribute("users",userService.allUsers());
        return "admin";
    }

    @GetMapping(value = "/new")
    public String newUser (Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "new";
    }
    @GetMapping(value = "/{id}")
    public String show (Model model,@PathVariable(value = "id") long id) {
        model.addAttribute("user",userService.show(id));
        return "show";
    }
    @PostMapping()
    public String createUser (@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/admin";

    }
    @GetMapping(value = "/{id}/edit")
    public String edit (Model model, @PathVariable(value = "id") long id) {
        model.addAttribute("users",userService.show(id));
        return "edit";
    }
    @PatchMapping(value = "/{id}")
    public String update (@ModelAttribute("users") User user, @PathVariable(value = "id") long id) {
        userService.update(id, user);
        return "redirect:/admin";
    }
    @DeleteMapping(value = "/{id}")
    public String delete ( @PathVariable(value = "id") long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

}
