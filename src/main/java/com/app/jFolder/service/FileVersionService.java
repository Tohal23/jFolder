package com.app.jFolder.service;

import com.app.jFolder.domain.FileDescriptor;
import com.app.jFolder.domain.FileVersion;
import com.app.jFolder.domain.User;
import com.app.jFolder.repos.FileVersionRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileVersionService {

    @Value("${upload.path}")
    private String uploadPath;
    private final FileVersionRepo fileVersionRepo;

    public FileVersionService(FileVersionRepo fileVersionRepo) {
        this.fileVersionRepo = fileVersionRepo;
    }

    public boolean addNewVersion(FileDescriptor file, MultipartFile file_data, User user, String folderName) throws IOException {
        FileVersion fileVersion = new FileVersion();
        FileVersion lastVersion = fileVersionRepo.findByFileAndLastIsTrue(file);

        String fileOriginalName = file_data.getOriginalFilename();
        String fileUploadPath = uploadPath+"/"+user.getUsername()+"/"+folderName
                +"/"+StringUtils.stripFilenameExtension(fileOriginalName)+"/";
        Path uploadDir = Paths.get(fileUploadPath);

        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        String uuidFileName = UUID.randomUUID().toString() + "." + StringUtils.getFilenameExtension(fileOriginalName);
        file_data.transferTo(new File(fileUploadPath  + uuidFileName));

        fileVersion.setSystemName(uuidFileName);
        fileVersion.setFile(file);
        fileVersion.setPath(fileUploadPath);
        fileVersion.setNumber(lastVersion != null? lastVersion.getNumber() : 0);
        fileVersion.setLast(true);
        fileVersionRepo.save(fileVersion);

        return true;
    }

    public String getPathVersion(FileDescriptor file, Integer number) {
        FileVersion fileVersion = fileVersionRepo.findByFileAndNumber(file, number);
        return fileVersion.getPath();
    }

}
