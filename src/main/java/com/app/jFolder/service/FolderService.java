package com.app.jFolder.service;

import com.app.jFolder.domain.Folder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class FolderService {

    public List<Folder> getWayToHeadInTree(List<Folder> foldersTree, String folderName) {
        LinkedList<Folder> foldersWay = new LinkedList<>();

        Folder folder = foldersTree.stream().filter(name -> name.getName().equals(folderName)).findFirst().get();

        while (folder != null) {
            foldersWay.addFirst(folder);
            folder = folder.getParent();
        }

        return foldersWay;
    }

}
