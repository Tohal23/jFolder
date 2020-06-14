package com.app.jFolder.service;

import com.app.jFolder.domain.FileDescriptor;
import com.app.jFolder.domain.FileVersion;
import com.app.jFolder.domain.Folder;
import com.app.jFolder.domain.User;
import com.app.jFolder.dto.FileVersionDto;
import com.app.jFolder.exception.user.UserNotAcceptException;
import com.app.jFolder.repos.FileRepo;
import com.app.jFolder.repos.FolderRepo;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public boolean addFile(MultipartFile file_data, User user, String folderName) throws IOException, NoSuchAlgorithmException {
        String fileOriginalName = file_data.getOriginalFilename();

        if (user.getActivationCode() != null) {
            throw new UserNotAcceptException("You need to accept your e-mail. "+user.getEmail());
        }

        if (!fileOriginalName.isEmpty()) {
            FileDescriptor file = fileRepo.findByFolderUserAndNameAndFolder_Name(user, fileOriginalName, folderName);
            if (file == null) {
                file = new FileDescriptor();
                file.setName(fileOriginalName);
                file.setFolder(folderRepo.getFolderByUserUsernameAndName(user.getUsername(), folderName));
                fileRepo.save(file);
            }
            return fileVersionService.addNewVersion(fileOriginalName, file_data, user, folderName, file);
        }
        return false;
    }

    public List<FileVersionDto> getFileVersions(User user, String folderName, String fileName) {
        FileDescriptor file = fileRepo.findByFolderUserAndNameAndFolder_Name(user, fileName, folderName);
        List<FileVersionDto> fileVersions = file.getFileVersionSet().stream().sorted().map(FileVersionDto::new).collect(Collectors.toList());
        Collections.sort(fileVersions);

        return fileVersions;
    }

    public String renameFile(User user, String folderName, String fileName, String newFileName) throws IOException {
        FileDescriptor file = fileRepo.findByFolderUserAndNameAndFolder_Name(user, newFileName, folderName);

        if (fileName != null && newFileName != null && file == null) {
            file = fileRepo.findByFolderUserAndNameAndFolder_Name(user, fileName, folderName);
            file.setName(newFileName);
            String pathFolder = folderService.getPath(user, folderName);
            Path source = Paths.get(pathFolder + "/" + StringUtils.stripFilenameExtension(fileName));
            Files.move(source, source.resolveSibling(StringUtils.stripFilenameExtension(newFileName)),
                    StandardCopyOption.REPLACE_EXISTING);
            fileRepo.save(file);
            if (fileVersionService.updateVersionsPath(file, user, folderName)) {
                return file.getFolder().getName();
            }
        }
        return null;
    }

    public String getFilePath(User user, String folderName, String fileName, Integer numberVersion) {
        FileDescriptor file = fileRepo.findByFolderUserAndNameAndFolder_Name(user, fileName, folderName);
        return fileVersionService.getPathVersion(file, numberVersion);
    }

    public String getFilePathLastVersion(User user, String folderName, String fileName) {
        FileDescriptor file = fileRepo.findByFolderUserAndNameAndFolder_Name(user, fileName, folderName);
        return fileVersionService.getPathLastVersion(file);
    }

    public String deleteFile(User user, String folderName, String fileName) throws IOException {
        FileDescriptor file = fileRepo.findByFolderUserAndNameAndFolder_Name(user, fileName, folderName);
        Set<FileVersion> fileVersionSet = file.getFileVersionSet();

        FileSystemUtils.deleteRecursively(Paths.get(folderService.getPath(user, folderName) + "/"
                + StringUtils.stripFilenameExtension(fileName)));
//
//        for (FileVersion fileVersion : fileVersionSet) {
//            String pathStr = fileVersion.getPath() + fileVersion.getSystemName();
//            Path path = Paths.get(pathStr);
//            Files.deleteIfExists(path);
//        }
//
//        Path path = Paths.get(folderService.getPath(user, folderName) + "/" + fileName);
//        Files.deleteIfExists(path);

        fileRepo.deleteById(file.getId());

        return folderName;
    }

}
