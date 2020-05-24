package com.app.jFolder.dataseeder;

import com.app.jFolder.domain.FileDescriptor;
import com.app.jFolder.domain.FileVersion;
import com.app.jFolder.domain.Folder;
import com.app.jFolder.domain.User;
import com.app.jFolder.repos.FileRepo;
import com.app.jFolder.repos.FileVersionRepo;
import com.app.jFolder.repos.FolderRepo;
import com.app.jFolder.repos.UserRepo;
import com.app.jFolder.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DataSeeder {

    @Value("${upload.path}")
    private String uploadPath;
    private final UserRepo userRepo;
    private final FolderRepo folderRepo;
    private final FileRepo fileRepo;
    private final FileVersionRepo fileVersionRepo;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepo userRepo, FolderRepo folderRepo, FileRepo fileRepo, FileVersionRepo fileVersionRepo) {
        this.userRepo = userRepo;
        this.folderRepo = folderRepo;
        this.fileRepo = fileRepo;
        this.fileVersionRepo = fileVersionRepo;
        this.passwordEncoder = new BCryptPasswordEncoder(8);
    }

    public void initAllDataBase() {
        initUser();
        initFolder();
        initFile();
        initFileVersion();
    }

    public void initUser() {
        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword(passwordEncoder.encode("123"));
        testUser.setEmail("krivonosenko98@gmail.com");
        testUser.setActive(true);
        testUser.setActivationCode(UUID.randomUUID().toString());
    }

    public void initFolder() {
        User testUser = userRepo.findByUsername("testUser");
        Folder firstFolderFirstLevel = folderRepo.save(new Folder(null, testUser, "FirstFolderFirstLevel",
                null, null));
        folderRepo.save(new Folder(null, testUser, "SecondFolderFirstLevel"
                , null, null));
        folderRepo.save(new Folder(firstFolderFirstLevel, testUser, "FirstFolderSecondLevel"
                , null, null));
    }

    public void initFile() {
        Folder firstFolderSecondLevel = folderRepo.getFolderByUserUsernameAndName("testUser"
                , "FirstFolderSecondLevel");
        Folder secondFolderFirstLevel = folderRepo.getFolderByUserUsernameAndName("testUser"
                , "SecondFolderFirstLevel");

        fileRepo.save(new FileDescriptor("123.txt", firstFolderSecondLevel
                , null));
        fileRepo.save(new FileDescriptor("321.pdf", secondFolderFirstLevel
                , null));
    }

    public void initFileVersion() {
        User testUser = userRepo.findByUsername("testUser");
        FileDescriptor txt123 = fileRepo.findByFolderUserAndNameAndFolder_Name(testUser, "123.txt"
                , "FirstFolderSecondLevel");
        FileDescriptor pdf123 = fileRepo.findByFolderUserAndNameAndFolder_Name(testUser, "321.pdf"
                , "SecondFolderFirstLevel");

        String pathFirstFolderSecondLevel = uploadPath+"/testUser/FirstFolderSecondLevel/";
        String pathSecondFolderFirstLevel = uploadPath+"/testUser/SecondFolderFirstLevel/";



        fileVersionRepo.save(new FileVersion(UUID.randomUUID().toString() + ".txt", pathFirstFolderSecondLevel
                , txt123, true, null));
        fileVersionRepo.save(new FileVersion(UUID.randomUUID().toString() + ".txt", pathFirstFolderSecondLevel
                , txt123, true, null));
        fileVersionRepo.save(new FileVersion(UUID.randomUUID().toString() + ".pdf", pathSecondFolderFirstLevel
                , pdf123, true, null));
    }

}
