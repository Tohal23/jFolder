package com.app.jFolder.service;

import com.app.jFolder.domain.FileDescriptor;
import com.app.jFolder.domain.FileVersion;
import com.app.jFolder.domain.User;
import com.app.jFolder.repos.FileVersionRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.UUID;

@Service
public class FileVersionService {

    @Value("${upload.path}")
    private String uploadPath;
    private final FileVersionRepo fileVersionRepo;

    public FileVersionService(FileVersionRepo fileVersionRepo) {
        this.fileVersionRepo = fileVersionRepo;
    }

    public boolean addNewVersion(FileDescriptor file, MultipartFile file_data, User user, String folderName) throws IOException, NoSuchAlgorithmException {
        FileVersion fileVersion = new FileVersion();
        FileVersion lastVersion = fileVersionRepo.findByFileAndLastIsTrue(file);
        String fileHash = getFileHash(file_data);

        Integer versionAnswer = checkHashVersion(fileHash, file);
        if (versionAnswer < 0) {
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
        } else if (versionAnswer > 0) {
            fileVersion = fileVersionRepo.findByFileAndNumber(file, versionAnswer);
            fileVersion.setLast(true);
            fileVersionRepo.save(fileVersion);
        }
        return true;
    }

    public String getPathVersion(FileDescriptor file, Integer number) {
        FileVersion fileVersion = fileVersionRepo.findByFileAndNumber(file, number);
        return fileVersion.getPath();
    }

    public String getPathLastVersion(FileDescriptor file) {
        FileVersion fileVersion = fileVersionRepo.findByFileAndLastIsTrue(file);
        return fileVersion.getPath();
    }

    public boolean setLastForVersionByNumber(FileDescriptor file, Integer number) {
        FileVersion fileVersion = fileVersionRepo.findByFileAndNumber(file, number);
        fileVersion.setLast(true);
        return true;
    }

    private Integer checkHashVersion(String fileHash, FileDescriptor file) {
        Set<FileVersion> fileVersions = file.getFileVersionSet();

        for (FileVersion fileVersion : fileVersions) {
            if (fileVersion.getHash_file().equals(fileHash) && fileVersion.isLast()) {
                return 0;
            } else if (fileVersion.getHash_file().equals(fileHash)) {
                return fileVersion.getNumber();
            }
        }
        return -1;
    }

    private String getFileHash(MultipartFile file_data) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(file_data.getBytes());
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest).toUpperCase();
    }

}
