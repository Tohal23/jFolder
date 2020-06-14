package com.app.jFolder.service;

import com.app.jFolder.domain.FileDescriptor;
import com.app.jFolder.domain.Folder;
import com.app.jFolder.domain.User;
import com.app.jFolder.dto.FileDescriptorDto;
import com.app.jFolder.repos.FolderRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FolderService {

    @Value("${upload.path}")
    private String uploadPath;
    private final FolderRepo folderRepo;

    public FolderService(FolderRepo folderRepo) {
        this.folderRepo = folderRepo;
    }

    public List<Folder> getWayToHeadInTree(User user, String folderName) {
        List<Folder> foldersTree = folderRepo.getFolderByUserUsername(user.getUsername());
        LinkedList<Folder> foldersWay = new LinkedList<>();

        Folder folder = foldersTree.stream().filter(name -> name.getName().equals(folderName)).findFirst().get();

        while (folder != null) {
            foldersWay.addFirst(folder);
            folder = folder.getParent();
        }
        return foldersWay;
    }

    public boolean addFolder(User user, String folderName, String parentFolder) {
        if (folderRepo.getFolderByUserUsernameAndName(user.getUsername(), folderName) == null && !folderName.isEmpty()) {
            Folder folder = new Folder();
            folder.setName(folderName.replaceAll(" ", ""));
            folder.setUser(user);
            if (!parentFolder.isEmpty()) {
                folder.setParent(folderRepo.getFolderByUserUsernameAndName(user.getUsername(), parentFolder));
            }
            folderRepo.save(folder);
            return true;
        } else {
            return false;
        }
    }

    public String getPath(User user, String folderName) {
        if (folderRepo.getFolderByUserUsernameAndName(user.getUsername(), folderName) != null) {
            return uploadPath+"/"+user.getUsername()+"/"+folderName+"/";
        }
        return null;
    }

    public String deleteFolder(User user, String folderName) throws IOException {
        Folder folder = folderRepo.getFolderByUserUsernameAndName(user.getUsername(), folderName);
        if (folder != null && folder.getParent() != null) {
            FileSystemUtils.deleteRecursively(Paths.get(getPath(user, folderName)));
            folderRepo.deleteById(folder.getId());
            return "redirect:/folders/"+folder.getParent().getName();
        }
        return null;
    }

    public boolean renameFolder(User user, String folderName, String newFolderName) throws IOException {
        Folder folder = folderRepo.getFolderByUserUsernameAndName(user.getUsername(), folderName);
        if (!newFolderName.isEmpty() && folder != null && folder.getParent() != null) {
            folder.setName(newFolderName);
            String pathFolder = getPath(user, folderName);
            Path source = Paths.get(pathFolder);
            Files.move(source, source.resolveSibling(newFolderName),
                    StandardCopyOption.REPLACE_EXISTING);
            folderRepo.save(folder);
            return true;
        }
        return false;
    }

    public List<Folder> getFoldersChild(User user, String folderName) {
        Folder folder = folderRepo.getFolderByUserUsernameAndName(user.getUsername(), folderName);
        List<Folder> foldersChild = new ArrayList<>(folder.getFolders());
        Collections.sort(foldersChild);
        return foldersChild;
    }

    public List<FileDescriptorDto> getFiles(User user, String folderName) {
        Folder folder = folderRepo.getFolderByUserUsernameAndName(user.getUsername(), folderName);
        List<FileDescriptor> files = new ArrayList<>(folder.getFiles());
        Collections.sort(files);
        return files.stream().map(FileDescriptorDto::new).collect(Collectors.toList());
    }

}
