package com.app.jFolder.service;

import com.app.jFolder.domain.User;
import com.app.jFolder.repos.FileRepo;
import com.app.jFolder.repos.FolderRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    private final FileRepo fileRepo;
    private final FolderRepo folderRepo;
    @Value("${upload.path}")
    private String uploadPath;

    public FileService(FileRepo fileRepo, FolderRepo folderRepo) {
        this.fileRepo = fileRepo;
        this.folderRepo = folderRepo;
    }

    public boolean addFile(MultipartFile file_data, User user, String folderName) throws IOException {
        if (file_data != null && !file_data.getOriginalFilename().isEmpty()) {
            String fileOriginalName = file_data.getOriginalFilename();
            String fileUploadPath = uploadPath+"/"+user.getUsername()+"/"+folderName+"/";
            Path uploadDir = Paths.get(fileUploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            if (fileRepo.findByName(file_data.getOriginalFilename()) != null) {
               //Integer numberFile = fileRepo.f
            } else {

            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + fileOriginalName;
            file_data.transferTo(new File(fileUploadPath + resultFilename));

            com.app.jFolder.domain.File file = new com.app.jFolder.domain.File();
            file.setName(fileOriginalName);
            file.setSystemName(uuidFile + "." + fileOriginalName);
            file.setPath(fileUploadPath);
            //file.setNumber();
            file.setFolder(folderRepo.getFolderByUserUsernameAndName(user.getUsername(), folderName));
            fileRepo.save(file);
            return true;
            }
        return false;
    }
}
