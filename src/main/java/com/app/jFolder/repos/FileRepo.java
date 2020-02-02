package com.app.jFolder.repos;

import com.app.jFolder.domain.File;
import com.app.jFolder.domain.User;
import com.app.jFolder.repos.custom.CustomFileRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepo extends JpaRepository<File, Long>, CustomFileRepository {
    File findByFolderUserAndName(User user, String name);
}