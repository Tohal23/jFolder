package com.app.jFolder.repos.custom.impl;

import com.app.jFolder.domain.File;
import com.app.jFolder.repos.custom.CustomFileRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Repository
public class CustomFileRepositoryImp implements CustomFileRepository {

    private final EntityManager em;

    public CustomFileRepositoryImp(EntityManager em) {
        this.em = em;
    }

    @Override
    public File getFileByNameAndFolderUserOrderByNumberDesc (String name, Long userId) {
        Query query = em
                .createQuery( "select f.* " +
                                       "from file f " +
                                       "join folder f1" +
                                       "  on f1.id = f.folder_id" +
                                      "where f.file_name = :file_name " +
                                      "  and f1.idUser = :id_user " +
                                      "order by f.number desc");
        query.setParameter("file_name", name);
        query.setParameter("id_user", userId);
        return (File) query.getResultStream().findFirst().get();
    }
}
