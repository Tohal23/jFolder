package com.app.jFolder.controller;

import com.app.jFolder.domain.User;
import com.app.jFolder.service.FileService;
import org.apache.commons.io.IOUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("file")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
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
        String folderName = null;
        try {
            folderName = fileService.deleteFile(user, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/folders/"+folderName;
    }


    @RequestMapping(value = "/get-file/{fileName}", method = RequestMethod.GET)
    public void getFile(HttpServletResponse response,
                        @PathVariable String fileName,
                        @AuthenticationPrincipal User user) throws IOException {
        String path = fileService.getFilePath(user, fileName);
        Path path1 = Paths.get(path);

        response.setContentType(Files.probeContentType(path1));

        response.setHeader("Content-Disposition", "attachment;filename="+fileName);
        IOUtils.copy(new FileInputStream(path), response.getOutputStream());
    }

    @PostMapping("/rename/{fileName}")
    public String renameFile( Model model
            , @AuthenticationPrincipal User user
            , @RequestParam String newFileName
            , @PathVariable String fileName
    ) {
        String folderFile = fileService.renameFile(user, fileName, newFileName);

        if (folderFile != null) {
            return "redirect:/folders/"+folderFile;
        }
        return "redirect:/folders/root";
    }
}
