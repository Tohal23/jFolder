package com.app.jFolder.controller;

import com.app.jFolder.domain.Folder;
import com.app.jFolder.domain.User;
import com.app.jFolder.repos.FolderRepo;
import com.app.jFolder.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("registration")
public class RegistrationController {
    private final UserService userService;
    private final FolderRepo folderRepo;

    public RegistrationController(UserService userService, FolderRepo folderRepo) {
        this.userService = userService;
        this.folderRepo = folderRepo;
    }

    @GetMapping
    public String getRegistrationPage(Model model) {
        return "registrationPage";
    }

    @PostMapping
    public String registration(@Valid User user
            , BindingResult bindingResult
            , Model model) {
        if (user.getPassword() != null && !user.getPassword().equals(user.getPassword2())) {
            model.addAttribute("passwordError", "Passwords are different!");
            return "registrationPage";
        }

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errors);

            return "registrationPage";
        }
        if (!userService.addUser(user)) {
            model.addAttribute("message", "User exists!");
            return "registrationPage";
        }

        Folder rootFolder = new Folder();
        rootFolder.setUser(user);
        rootFolder.setName("root");
        folderRepo.save(rootFolder);
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
