package com.app.jFolder.repos.custom.impl;

import com.app.jFolder.domain.Folder;
import com.app.jFolder.repos.custom.CustomFolderRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class CustomFolderRepositoryImp implements CustomFolderRepository {

    private final EntityManager em;

    public CustomFolderRepositoryImp(EntityManager em) {
        this.em = em;
    }

    public List<Folder>  getFolderByUserUsername(String userName) {
        return em.createQuery("select f from folder f join usr u on u.id = f.user_id where u.username = " + userName).getResultList();
    }

    @Override
    public Folder getFolderByUserUsernameAndName(String userName, String name) {
        return (Folder) em.createQuery("select f from folder f join usr u on u.id = f.user_id where u.username = " + userName + "and f.name = " + name).getResultStream().findFirst().get();
    }
}
