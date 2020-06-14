package com.app.jFolder.controller;

import com.app.jFolder.domain.User;
import com.app.jFolder.service.FileService;
import org.apache.commons.io.IOUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

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
                          ) throws IOException, NoSuchAlgorithmException {
        fileService.addFile(file_data, user, folderName);

        return "redirect:/folders/"+folderName;
    }

    @PostMapping("/delete/{folderName}/{fileName}")
    public String deleteFile(Model model,
                             @PathVariable String folderName,
                             @PathVariable String fileName,
                             @AuthenticationPrincipal User user) {
        try {
            folderName = fileService.deleteFile(user, folderName, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/folders/"+folderName;
    }

    @RequestMapping(value = "/get-file/{folderName}/{fileName}/{numberVersion}", method = RequestMethod.GET)
    public void getFileByNumber(HttpServletResponse response,
                        @PathVariable String folderName,
                        @PathVariable String fileName,
                        @PathVariable Integer numberVersion,
                        @AuthenticationPrincipal User user) throws IOException {
        String path = fileService.getFilePath(user, folderName, fileName, numberVersion);
        Path path1 = Paths.get(path);

        response.setContentType(Files.probeContentType(path1));

        response.setHeader("Content-Disposition", "attachment;filename="+ fileName.split("\\.")[0]
                +"-version("+numberVersion+")."
                + StringUtils.getFilenameExtension(path));
        IOUtils.copy(new FileInputStream(path), response.getOutputStream());
    }

    @RequestMapping(value = "/get-file/{folderName}/{fileName}/last", method = RequestMethod.GET)
    public void getFileLastVersion(HttpServletResponse response,
                                @PathVariable String folderName,
                                @PathVariable String fileName,
                                @AuthenticationPrincipal User user) throws IOException {
        String path = fileService.getFilePathLastVersion(user, folderName, fileName);
        Path path1 = Paths.get(path);

        response.setContentType(Files.probeContentType(path1));

        response.setHeader("Content-Disposition", "attachment;filename="+ fileName.split("\\.")[0]
                +"-last-version."
                + StringUtils.getFilenameExtension(path));
        IOUtils.copy(new FileInputStream(path), response.getOutputStream());
    }

    @PostMapping("/rename/{folderName}/{fileName}")
    public String renameFile( Model model
            , @AuthenticationPrincipal User user
            , @RequestParam String newFileName
            , @PathVariable String folderName
            , @PathVariable String fileName
    ) throws IOException {
        String folderFile = fileService.renameFile(user, folderName, fileName, newFileName);

        if (folderFile != null) {
            return "redirect:/folders/"+folderFile;
        }
        return "redirect:/folders/root";
    }
}
