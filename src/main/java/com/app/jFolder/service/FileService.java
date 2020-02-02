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
            com.app.jFolder.domain.File file = new com.app.jFolder.domain.File();

            String fileOriginalName = file_data.getOriginalFilename();
            String fileUploadPath = uploadPath+"/"+user.getUsername()+"/"+folderName+"/";
            Path uploadDir = Paths.get(fileUploadPath);

            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            if (fileRepo.findByFolderUserAndName(user, fileOriginalName) != null) {
                Integer numberFile = fileRepo.getFileByNameAndFolderUserOrderByNumberDesc(fileOriginalName, user.getId()).getNumber() + 1;
               file.setNumber(numberFile);
               fileOriginalName = fileOriginalName + "("+numberFile+")";

            } else {
                file.setNumber(0);
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + fileOriginalName;
            file_data.transferTo(new File(fileUploadPath + resultFilename));

            file.setName(fileOriginalName);
            file.setSystemName(uuidFile + "." + fileOriginalName);
            file.setPath(fileUploadPath);
            file.setFolder(folderRepo.getFolderByUserUsernameAndName(user.getUsername(), folderName));
            fileRepo.save(file);
            return true;
            }
        return false;
    }
}
