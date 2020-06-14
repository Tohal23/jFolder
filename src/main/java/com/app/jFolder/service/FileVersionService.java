package com.app.jFolder.service;

import com.app.jFolder.domain.FileDescriptor;
import com.app.jFolder.domain.FileVersion;
import com.app.jFolder.domain.User;
import com.app.jFolder.exception.fileVersion.FileVersionExistException;
import com.app.jFolder.exception.fileVersion.FileVersionExtensionException;
import com.app.jFolder.repos.FileRepo;
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
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Service
public class FileVersionService {

    @Value("${upload.path}")
    private String uploadPath;
    private final FileVersionRepo fileVersionRepo;
    private final FileRepo fileRepo;

    public FileVersionService(FileVersionRepo fileVersionRepo, FileRepo fileRepo) {
        this.fileVersionRepo = fileVersionRepo;
        this.fileRepo = fileRepo;
    }

    public boolean addNewVersion(String fileName, MultipartFile file_data, User user, String folderName, FileDescriptor file)
            throws IOException, NoSuchAlgorithmException, FileVersionExtensionException, FileVersionExistException {
        FileVersion fileVersion = new FileVersion();
        if (file == null) {
            file = fileRepo.findByFolderUserAndNameAndFolder_Name(user, fileName, folderName);
        }
        String newFileVersionExtension = StringUtils.getFilenameExtension(file_data.getOriginalFilename());

        if (!file.getName().split("\\.")[1].equals(newFileVersionExtension)) {
            throw new FileVersionExtensionException("You need upload file with correctly extension. ("+file.getName().split("\\.")[1]+")");
        }

        String fileHash = getFileHash(file_data);
        if (file.getFileVersionSet() == null) {
            String fileOriginalName = file_data.getOriginalFilename();
            String fileUploadPath = uploadPath+"/"+user.getUsername()+"/"+folderName
                    +"/"+StringUtils.stripFilenameExtension(fileName)+"/";
            Path uploadDir = Paths.get(fileUploadPath);

            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String uuidFileName = UUID.randomUUID().toString() + "." + newFileVersionExtension;
            file_data.transferTo(new File(fileUploadPath  + uuidFileName));
            Integer number = 0;
            fileVersion.setSystemName(uuidFileName);
            fileVersion.setFile(file);
            fileVersion.setPath(fileUploadPath);
            fileVersion.setNumber(number);
            fileVersion.setHash_file(fileHash);
            fileVersion.setDate(Instant.now());
            fileVersion.setDescription("");
            fileVersion.setLast(true);
            fileVersionRepo.save(fileVersion);
            return true;
        }

        Integer maxNumberVersion = file.getFileVersionSet().stream()
                    .sorted()
                    .map(FileVersion::getNumber)
                    .findFirst()
                    .orElse(0);


        Integer versionAnswer = checkHashVersion(fileHash, file);
        if (versionAnswer < 0) {
            String fileOriginalName = file_data.getOriginalFilename();
            String fileUploadPath = uploadPath+"/"+user.getUsername()+"/"+folderName
                    +"/"+StringUtils.stripFilenameExtension(fileName)+"/";
            Path uploadDir = Paths.get(fileUploadPath);

            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String uuidFileName = UUID.randomUUID().toString() + "." + newFileVersionExtension;
            file_data.transferTo(new File(fileUploadPath  + uuidFileName));
            Integer number = maxNumberVersion + 1;
            fileVersion.setSystemName(uuidFileName);
            fileVersion.setFile(file);
            fileVersion.setPath(fileUploadPath);
            fileVersion.setNumber(number);
            fileVersion.setHash_file(fileHash);
            fileVersion.setDate(Instant.now());
            fileVersion.setDescription("");
            fileVersion.setLast(true);
            file.getFileVersionSet().stream().forEach(fileVersion1 -> {
                fileVersion1.setLast(false);
                fileVersionRepo.save(fileVersion1);
            });
            fileVersionRepo.save(fileVersion);
        } else if (versionAnswer > 0) {
            fileVersion = fileVersionRepo.findByFileAndNumber(file, versionAnswer);
            fileVersion.setLast(true);
            file.getFileVersionSet().stream().forEach(fileVersion1 -> {
                fileVersion1.setLast(false);
                fileVersionRepo.save(fileVersion1);
            });
            fileVersionRepo.save(fileVersion);
            throw new FileVersionExistException("This file version already exist, this version number - " + versionAnswer);
        } else {
            throw new FileVersionExistException("This file version already exist, this version number - " + maxNumberVersion);
        }
        return true;
    }

    public boolean updateVersion(Long id, String description, Boolean last) {
        FileVersion fileVersion = fileVersionRepo.findById(id).get();

        fileVersion.setDescription(description);
        if (last) {
            setLastForVersionByNumber(fileVersion.getFile(), fileVersion.getNumber());
        }
        fileVersionRepo.save(fileVersion);
        return true;
    }

    public boolean deleteVersion(Long id) {
        FileVersion fileVersion = fileVersionRepo.findById(id).get();
        fileVersionRepo.delete(fileVersion);
        return true;
    }

    public boolean updateVersionsPath(FileDescriptor file, User user, String folderName) {
        for (FileVersion fileVersion : file.getFileVersionSet()) {
            fileVersion.setPath(uploadPath+"/"+user.getUsername()+"/"+folderName
                    +"/"+StringUtils.stripFilenameExtension(file.getName())+"/");
            fileVersionRepo.save(fileVersion);
        }
        return true;
    }

    public String getPathVersion(FileDescriptor file, Integer number) {
        FileVersion fileVersion = file.getFileVersionSet()
                .stream()
                .filter(fileVersion1 -> fileVersion1.getNumber().equals(number))
                .findFirst().get();
        return fileVersion.getPath()+fileVersion.getSystemName();
    }

    public String getPathLastVersion(FileDescriptor file) {
        FileVersion fileVersion = fileVersionRepo.findByFileAndLastIsTrue(file);
        return fileVersion.getPath()+fileVersion.getSystemName();
    }

    public boolean setLastForVersionByNumber(FileDescriptor file, Integer number) {
        FileVersion fileVersion = file.getFileVersionSet()
                .stream()
                .filter(fileVersion1 -> fileVersion1.getNumber().equals(number))
                .findFirst().get();

        file.getFileVersionSet().stream().forEach(fileVersion1 -> {
            fileVersion1.setLast(false);
            fileVersionRepo.save(fileVersion1);
        });

        fileVersion.setLast(true);
        fileVersionRepo.save(fileVersion);
        return true;
    }

    private Integer checkHashVersion(String fileHash, FileDescriptor file) {
        Set<FileVersion> fileVersions = file.getFileVersionSet();

        if (fileVersions.isEmpty()) {
            return -1;
        }

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
