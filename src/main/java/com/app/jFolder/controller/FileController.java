package com.app.jFolder.controller;

import com.app.jFolder.domain.File;
import com.app.jFolder.domain.Folder;
import com.app.jFolder.domain.User;
import com.app.jFolder.repos.FileRepo;
import com.app.jFolder.service.FileService;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("file")
public class FileController {

    private final FileService fileService;
    private final FileRepo fileRepo;

    public FileController(FileService fileService, FileRepo fileRepo) {
        this.fileService = fileService;
        this.fileRepo = fileRepo;
    }

    @PostMapping("/add/{folderName}")
    public String addFile(Model model,
                          @PathVariable String folderName,
                          @AuthenticationPrincipal User user,
                          @RequestParam("file") MultipartFile file_data
                          ) throws IOException {

        fileService.addFile(file_data, user, folderName);

        return "redirect:/folders/"+folderName;
    }

    @PostMapping("/delete/{fileName}")
    public String deleteFile(Model model,
                             @PathVariable String fileName,
                             @AuthenticationPrincipal User user) {
        File file = fileRepo.findByFolderUserAndName(user, fileName);
        fileRepo.deleteById(file.getId());
        return "redirect:/folders/"+file.getFolder().getName();
    }

    @GetMapping(
            value = "/get-file/{fileName}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public @ResponseBody byte[] getFile(Model model,
                                        @PathVariable String fileName,
                                        @AuthenticationPrincipal User user) throws IOException {
        File file = fileRepo.findByFolderUserAndName(user, fileName);
        String path = file.getPath() + file.getSystemName();
        InputStream in =  new FileInputStream(path);
        return IOUtils.toByteArray(in);
    }

    @PostMapping("/rename/{fileName}")
    public String renameFile( Model model
            , @AuthenticationPrincipal User user
            , @RequestParam String newFileName
            , @PathVariable String fileName
    ) {
        File file = fileRepo.findByFolderUserAndName(user, newFileName);

        if (fileName != null && newFileName != null && file == null) {
            file = fileRepo.findByFolderUserAndName(user, fileName);
            file.setName(newFileName);
            fileRepo.save(file);
            model.addAttribute("folderName", newFileName);
            return "redirect:/folders/"+file.getFolder().getName();
        }
        return "";
    }

}
