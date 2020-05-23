package com.app.jFolder.repos;

import com.app.jFolder.domain.FileDescriptor;
import com.app.jFolder.domain.FileVersion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileVersionRepo extends JpaRepository<FileVersion, Long> {

    FileVersion findByFileAndLastIsTrue(FileDescriptor file);

    FileVersion findByFileAndNumber(FileDescriptor file, Integer number);

}
