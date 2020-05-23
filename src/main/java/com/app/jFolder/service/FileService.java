package com.app.jFolder.service;

import com.app.jFolder.domain.FileDescriptor;
import com.app.jFolder.domain.FileVersion;
import com.app.jFolder.domain.Folder;
import com.app.jFolder.domain.User;
import com.app.jFolder.repos.FileRepo;
import com.app.jFolder.repos.FolderRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

@Service
public class FileService {

    private final FileRepo fileRepo;
    private final FolderRepo folderRepo;
    private final FileVersionService fileVersionService;
    private final FolderService folderService;

    public FileService(FileRepo fileRepo, FolderRepo folderRepo, FileVersionService fileVersionService, FolderService folderService) {
        this.fileRepo = fileRepo;
        this.folderRepo = folderRepo;
        this.fileVersionService = fileVersionService;
        this.folderService = folderService;
    }

    public boolean addFile(MultipartFile file_data, User user, String folderName) throws IOException {
        String fileOriginalName = file_data.getOriginalFilename();
        if (!fileOriginalName.isEmpty()) {
            FileDescriptor file = fileRepo.findByFolderUserAndNameAndFolder_Name(user, fileOriginalName, folderName);
            if (file == null) {
                file = new FileDescriptor();
                file.setName(fileOriginalName);
                file.setFolder(folderRepo.getFolderByUserUsernameAndName(user.getUsername(), folderName));
                fileRepo.save(file);
            }
            return fileVersionService.addNewVersion(file, file_data, user, folderName);
        }
        return false;
    }

    public String renameFile(User user, String folderName, String fileName, String newFileName) {
        FileDescriptor file = fileRepo.findByFolderUserAndNameAndFolder_Name(user, newFileName, folderName);

        if (fileName != null && newFileName != null && file == null) {
            file = fileRepo.findByFolderUserAndNameAndFolder_Name(user, fileName, folderName);
            file.setName(newFileName);
            fileRepo.save(file);
            return file.getFolder().getName();
        }
        return null;
    }

    public String getFilePath(User user, String folderName, String fileName, Integer numberVersion) {
        FileDescriptor file = fileRepo.findByFolderUserAndNameAndFolder_Name(user, folderName, fileName);
        return fileVersionService.getPathVersion(file, numberVersion);
    }

    public String deleteFile(User user, String folderName, String fileName) throws IOException {
        FileDescriptor file = fileRepo.findByFolderUserAndNameAndFolder_Name(user, folderName, fileName);
        Set<FileVersion> fileVersionSet = file.getFileVersionSet();

        for (FileVersion fileVersion : fileVersionSet) {
            String pathStr = fileVersion.getPath();
            Path path = Paths.get(pathStr);
            Files.deleteIfExists(path);
        }

        Path path = Paths.get(folderService.getPath(user, folderName) + "/" + fileName);
        Files.deleteIfExists(path);

        fileRepo.deleteById(file.getId());

        return folderName;
    }

}
