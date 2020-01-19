package com.app.jFolder.repos;

import com.app.jFolder.domain.Folder;

import java.util.List;

public interface CustomFolderRepository {

    List<Folder> getFolderByUserUsername(String userName);

    Folder getFolderByUserUsernameAndName(String userName, String name);
}
