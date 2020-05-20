package com.app.jFolder.service;

import com.app.jFolder.domain.FileDescriptor;
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
import java.util.Objects;
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
            FileDescriptor file = new FileDescriptor();

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

            file.setName(fileOriginalName.replaceAll(" ", ""));
            file.setSystemName(uuidFile + "." + fileOriginalName);
            file.setPath(fileUploadPath);
            file.setFolder(folderRepo.getFolderByUserUsernameAndName(user.getUsername(), folderName));
            fileRepo.save(file);
            return true;
            }
        return false;
    }

    public String renameFile(User user, String fileName, String newFileName) {
        FileDescriptor file = fileRepo.findByFolderUserAndName(user, newFileName);

        if (fileName != null && newFileName != null && file == null) {
            file = fileRepo.findByFolderUserAndName(user, fileName);
            file.setName(newFileName);
            fileRepo.save(file);
            return file.getFolder().getName();
        }
        return null;
    }

    public String getFilePath(User user, String fileName) {
        FileDescriptor file = fileRepo.findByFolderUserAndName(user, fileName);
        return file.getPath() + file.getSystemName();
    }

    public String deleteFile(User user, String fileName) throws IOException {
        FileDescriptor file = fileRepo.findByFolderUserAndName(user, fileName);
        String folderName = file.getFolder().getName();
        Path path = Paths.get(file.getPath()+file.getSystemName());

        fileRepo.deleteById(file.getId());

        Files.deleteIfExists(path);

        return folderName;
    }

}
