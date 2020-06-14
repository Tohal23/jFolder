package com.app.jFolder.controller;

import com.app.jFolder.domain.User;
import com.app.jFolder.service.FileService;
import com.app.jFolder.service.FileVersionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping("file/versions")
public class FileVersionController {

    private final FileService fileService;
    private final FileVersionService fileVersionService;

    public FileVersionController(FileService fileService, FileVersionService fileVersionService) {
        this.fileService = fileService;
        this.fileVersionService = fileVersionService;
    }

    @GetMapping("/{folderName}/{fileName}")
    public String fileVersionsPage(Model model
            , @AuthenticationPrincipal User user
            , @PathVariable String fileName
            , @PathVariable String folderName
    ) {
        model.addAttribute("versions", fileService.getFileVersions(user, folderName, fileName));
        model.addAttribute("fileName", fileName);
        model.addAttribute("folderName", folderName);
        return "filePage";
    }

    @PostMapping("add/{folderName}/{fileName}")
    public String addVersion(Model model
            , @AuthenticationPrincipal User user
            , @RequestParam("file") MultipartFile file_data
            , @PathVariable String fileName
            , @PathVariable String folderName) throws IOException, NoSuchAlgorithmException {
        fileVersionService.addNewVersion(fileName, file_data, user, folderName, null);
        model.addAttribute("versions", fileService.getFileVersions(user, folderName, fileName));
        model.addAttribute("fileName", fileName);
        model.addAttribute("folderName", folderName);
        return "redirect:/file/versions/"+folderName+"/"+fileName;
    }

    @PostMapping("update/{folderName}/{fileName}")
    public String changeVersion(Model model
            , @AuthenticationPrincipal User user
            , @PathVariable String fileName
            , @PathVariable String folderName
            , @RequestParam Long id
            , @RequestParam String description
            , @RequestParam Boolean last
    ) {
        fileVersionService.updateVersion(id, description, last);
        model.addAttribute("versions", fileService.getFileVersions(user, folderName, fileName));
        model.addAttribute("fileName", fileName);
        model.addAttribute("folderName", folderName);
        return "redirect:/file/versions/"+folderName+"/"+fileName;
    }

    @PostMapping("delete/{folderName}/{fileName}")
    public String deleteVersion(Model model
            , @AuthenticationPrincipal User user
            , @PathVariable String fileName
            , @PathVariable String folderName
            , @RequestParam Long id
    ) {
        fileVersionService.deleteVersion(id);
        model.addAttribute("versions", fileService.getFileVersions(user, folderName, fileName));
        model.addAttribute("fileName", fileName);
        model.addAttribute("folderName", folderName);
        return "redirect:/file/versions/"+folderName+"/"+fileName;
    }
}
