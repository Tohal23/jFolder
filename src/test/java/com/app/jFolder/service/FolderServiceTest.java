package com.app.jFolder.service;

import com.app.jFolder.domain.Folder;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class FolderServiceTest {

    @Autowired
    private FolderService folderService;

    @Test
    void getWayToHeadInTreeTest() {
        LinkedList<Folder> allFolders = new LinkedList<>();
        LinkedList<Folder> resultTreeFolders = new LinkedList<>();

        Folder rootFolder = new Folder();
        rootFolder.setName("rootFolder");
        allFolders.add(rootFolder);
        resultTreeFolders.add(rootFolder);

        Folder firstLineFirstFolder = new Folder();
        firstLineFirstFolder.setName("firstLineFirstFolder");
        firstLineFirstFolder.setParent(rootFolder);
        allFolders.add(firstLineFirstFolder);

        Folder firstLineSecondFolder = new Folder();
        firstLineSecondFolder.setName("firstLineSecondFolder");
        firstLineSecondFolder.setParent(rootFolder);
        allFolders.add(firstLineSecondFolder);
        resultTreeFolders.add(firstLineSecondFolder);

        Folder firstLineThirdFolder = new Folder();
        firstLineThirdFolder.setName("firstLineThirdFolder");
        firstLineThirdFolder.setParent(rootFolder);
        allFolders.add(firstLineThirdFolder);

        Folder secondLineFirstFolder = new Folder();
        secondLineFirstFolder.setName("secondLineFirstFolder");
        secondLineFirstFolder.setParent(firstLineSecondFolder);
        allFolders.add(secondLineFirstFolder);
        allFolders.add(secondLineFirstFolder);

        //List<Folder> treeFolders = folderService.getWayToHeadInTree(allFolders, "firstLineSecondFolder");

        //Assert.assertArrayEquals(resultTreeFolders.toArray(), treeFolders.toArray());
    }
}