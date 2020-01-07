package com.app.jFolder.controller;

import com.app.jFolder.domain.User;
import com.app.jFolder.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("registration")
public class RegistrationController {
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getRegistrationPage(Model model) {
        return "registrationPage";
    }

    @PostMapping
    public String registration(Model model,User user) {
        if (!userService.addUser(user)) {
            model.addAttribute("message", "User exists!");
            return "registrationPage";
        }

        return "redirect:/";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activeUser(code);

        if (isActivated) {
            model.addAttribute("message", "User activated!");
        } else {
            model.addAttribute("message", "Activation code is not found!");
        }

        return "loginPage";
    }
}
