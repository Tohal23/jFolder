package com.app.jFolder.service;

import com.app.jFolder.domain.User;
import com.app.jFolder.repos.FileRepo;
import com.app.jFolder.repos.FolderRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
            String file_uploadPath = uploadPath+"/"+user.getUsername()+"/"+folderName;

            File uploadDir = new File(file_uploadPath);

            if (!uploadDir.exists()) {
                if (uploadDir.mkdir()) {

                    String uuidFile = UUID.randomUUID().toString();
                    String resultFilename = uuidFile + "." + file_data.getOriginalFilename();

                    file_data.transferTo(new File(file_uploadPath + "/" + resultFilename));

                    com.app.jFolder.domain.File file = new com.app.jFolder.domain.File();
                    file.setName(file_data.getOriginalFilename());
                    file.setPath(file_uploadPath);
                    file.setFolder(folderRepo.getFolderByUserUsernameAndName(user.getUsername(), folderName));
                    return true;
                }
            }
        }
        return false;
    }
}
