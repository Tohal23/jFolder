package com.app.jFolder.controller;

import com.app.jFolder.domain.User;
import com.app.jFolder.service.FolderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("folders")
public class FolderController {

    private final FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @GetMapping("/{folderName}")
    public String folderPage(Model model
            , @AuthenticationPrincipal User user
            , @PathVariable String folderName
    ) {
        model.addAttribute("foldersWay", folderService.getWayToHeadInTree(user, folderName));
        model.addAttribute("foldersChild", folderService.getFoldersChild(user, folderName));
        model.addAttribute("folderName", folderName);
        model.addAttribute("files", folderService.getFiles(user, folderName));

        return "startPage";
    }

    @PostMapping("/rename/{folderName}")
    public String renameFolder(Model model
            , @AuthenticationPrincipal User user
            , @RequestParam String newFolderName
            , @PathVariable String folderName
    ) {
        boolean isRenameFolder = folderService.renameFolder(user, folderName, newFolderName);

        if (isRenameFolder) {
            model.addAttribute("folderName", folderName);
            return "redirect:/folders/"+newFolderName;
        }
        return "redirect:/folders/root";
    }

    @PostMapping("/{parentFolder}")
    public String addFolder(Model model
            , @AuthenticationPrincipal User user
            , @RequestParam String folderName
            , @PathVariable String parentFolder
    ) {
        boolean isCreateNewFolder = folderService.addFolder(user, folderName, parentFolder);

        if (isCreateNewFolder) {
            model.addAttribute("folderName", folderName);
            return "redirect:/folders/"+folderName;
        } else {
            return "redirect:/folders/"+parentFolder;
        }
    }

    @PostMapping("/delete/{folderName}")
    public String deleteFolder(Model model
            , @AuthenticationPrincipal User user
            , @PathVariable String folderName) {
        String parentFolderName = folderService.deleteFolder(user, folderName);
        if (parentFolderName != null) {
            return parentFolderName;
        }
        return folderName;
    }
}