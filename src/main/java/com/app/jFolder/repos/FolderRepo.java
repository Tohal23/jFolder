package com.app.jFolder.repos;

import com.app.jFolder.domain.Folder;
import com.app.jFolder.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FolderRepo extends JpaRepository<Folder, Long> {

    @Query("select f                     " +
            " from folder f              " +
            " join user u                " +
            "   on u.id = f.user_id      " +
            "where u.username = :username")
    List<Folder> findByUsername(@Param("username") String username);

    @Query("")
    List<Folder> findParentsByFolder(@Param("folder_id") Long folder_id);

}
