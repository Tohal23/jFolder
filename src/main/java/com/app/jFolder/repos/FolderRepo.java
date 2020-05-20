package com.app.jFolder.repos;

import com.app.jFolder.domain.Folder;
import com.app.jFolder.repos.custom.CustomFolderRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepo extends JpaRepository<Folder, Long>, CustomFolderRepository {

}