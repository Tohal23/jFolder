package com.app.jFolder.service;

import com.app.jFolder.domain.Folder;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.*;
import java.util.List;

@Service
public class FolderService {

    public List<Folder> getWayToHeadInTree(List<Folder> foldersTree, String folderName) {
        ArrayList<Folder> foldersWayRev = new ArrayList<>();
        ArrayList<Folder> foldersWay = new ArrayList<>();

        Folder folder = foldersTree.stream().filter(name -> name.getName().equals(folderName)).findFirst().get();

        while (folder != null) {
            foldersWayRev.add(folder);
            folder = folder.getParent();
        }

        for (int i = foldersWayRev.size()-1; i >= 0 ; i--) {
            foldersWay.add(foldersWayRev.get(i));
        }

        return foldersWay;
    }

}
