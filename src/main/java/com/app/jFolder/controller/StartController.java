package com.app.jFolder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StartController {
    @GetMapping("/")
    public String rootFolderPage(Model model) {
        return "redirect:/folders/root";
    }

}
