package com.app.jFolder.repos;

import com.app.jFolder.domain.FileDescriptor;
import com.app.jFolder.domain.User;
import com.app.jFolder.repos.custom.CustomFileRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepo extends JpaRepository<FileDescriptor, Long>, CustomFileRepository {
    FileDescriptor findByFolderUserAndName(User user, String name);
}