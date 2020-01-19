package com.app.jFolder.repos;

import com.app.jFolder.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepo extends JpaRepository<File, Long> {

}
