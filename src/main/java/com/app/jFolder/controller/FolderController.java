package com.app.jFolder.controller;

import com.app.jFolder.domain.File;
import com.app.jFolder.domain.Folder;
import com.app.jFolder.domain.User;
import com.app.jFolder.repos.FolderRepo;
import com.app.jFolder.service.FolderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("folders")
public class FolderController {

    private final FolderRepo folderRepo;
    private final FolderService folderService;

    public FolderController(FolderRepo folderRepo, FolderService folderService) {
        this.folderRepo = folderRepo;
        this.folderService = folderService;
    }

    @GetMapping("/{folderName}")
    public String folderPage(Model model
            , @AuthenticationPrincipal User user
            , @PathVariable String folderName
    ) {
        Folder folder = folderRepo.getFolderByUserUsernameAndName(user.getUsername(), folderName);
        List<Folder> foldersTree;
        List<Folder> foldersChild = new ArrayList<>(folder.getFolders());
        Collections.sort(foldersChild);

        List<File> files = new ArrayList<>(folder.getFiles());
        Collections.sort(files);

        if (model.getAttribute("foldersTree") == null) {
            foldersTree = folderRepo.getFolderByUserUsername(user.getUsername());
            model.addAttribute("foldersTree", foldersTree);
        } else {
            foldersTree = (List<Folder>) model.getAttribute("foldersTree");
            assert foldersTree != null;
        }

        model.addAttribute("foldersWay", folderService.getWayToHeadInTree(foldersTree, folderName));
        model.addAttribute("foldersChild", foldersChild);
        model.addAttribute("folderName", folderName);
        model.addAttribute("files", files);

        return "startPage";
    }

    @PostMapping("/rename/{folderName}")
    public String renameFolder(Model model
            , @AuthenticationPrincipal User user
            , @RequestParam String newFolderName
            , @PathVariable String folderName
    ) {
        Folder folder = folderRepo.getFolderByUserUsernameAndName(user.getUsername(), folderName);
        if (!newFolderName.isEmpty() && folder != null && folder.getParent() != null) {
            folder.setName(newFolderName);
            folderRepo.save(folder);
            model.addAttribute("folderName", newFolderName);
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
        if (folderRepo.getFolderByUserUsernameAndName(user.getUsername(), folderName) == null && !folderName.isEmpty()) {
            Folder folder = new Folder();
            folder.setName(folderName.replaceAll(" ", ""));
            folder.setUser(user);
            if (!parentFolder.isEmpty()) {
                folder.setParent(folderRepo.getFolderByUserUsernameAndName(user.getUsername(), parentFolder));
            }
            folderRepo.save(folder);
            //model.addAttribute("folders", folder.getFolders());
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
        Folder folder = folderRepo.getFolderByUserUsernameAndName(user.getUsername(), folderName);
        if (folder != null && folder.getParent() != null) {
            folderRepo.deleteById(folder.getId());
            return "redirect:/folders/"+folder.getParent().getName();
        }
        return "redirect:/folders/"+folderName;
    }
}