package com.app.jFolder.repos.custom;

import com.app.jFolder.domain.File;

public interface CustomFileRepository {

    File getFileByNameAndFolderUserOrderByNumberDesc(String name, Long userId);

}
