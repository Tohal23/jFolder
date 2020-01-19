package com.app.jFolder.controller;

import com.app.jFolder.domain.User;
import com.app.jFolder.service.FileService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

@Controller
@RequestMapping("file")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/add/{folderNames}")
    public String addFile(Model model,
                          @PathVariable String folderName,
                          @AuthenticationPrincipal User user,
                          @RequestParam("file") MultipartFile file_data
                          ) throws IOException {
        if (!fileService.addFile(file_data, user, folderName)) {
            model.addAttribute("exceptions", new ArrayList<String>().add("File didn't create."));
        }

        return "startPage";
    }

}
