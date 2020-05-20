package com.app.jFolder.service;

import com.app.jFolder.domain.FileDescriptor;
import com.app.jFolder.domain.Folder;
import com.app.jFolder.domain.User;
import com.app.jFolder.repos.FileRepo;
import com.app.jFolder.repos.FolderRepo;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class FileDescriptorServiceTest {

    @Autowired
    private FileService fileService;

    @MockBean
    private FileRepo fileRepo;

    @MockBean
    private FolderRepo folderRepo;

    @Test
    void addFileTest() throws IOException {
        AtomicBoolean resultAdd = new AtomicBoolean(false);

        MockMultipartFile file = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
        User user = new User();

        user.setEmail("someEmail@mail.com");
        user.setUsername("admin");

        Mockito.doReturn(new Folder()).when(folderRepo).getFolderByUserUsernameAndName("admin", "root");

        resultAdd.set(fileService.addFile(file, user, "root"));
        Assert.assertTrue(resultAdd.get());

        Mockito.verify(fileRepo, Mockito.times(1)).save(ArgumentMatchers.any(FileDescriptor.class));
    }
}