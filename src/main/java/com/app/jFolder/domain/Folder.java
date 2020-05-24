package com.app.jFolder.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "folder", indexes = {@Index(name = "idx_folder_name", columnList = "name")})
public class Folder implements Comparable<Folder> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Folder parent;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    private String name;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent", cascade = CascadeType.REMOVE)
    private Set<Folder> folders = new HashSet<>();
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "folder", cascade = CascadeType.REMOVE)
    private Set<FileDescriptor> files = new HashSet<>();

    public Folder(Folder parent, User user, String name, Set<Folder> folders, Set<FileDescriptor> files) {
        this.parent = parent;
        this.user = user;
        this.name = name;
        this.folders = folders;
        this.files = files;
    }

    public Folder() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Folder getParent() {
        return parent;
    }

    public void setParent(Folder parent) {
        this.parent = parent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Folder> getFolders() {
        return folders;
    }

    public void setFolders(Set<Folder> folders) {
        this.folders = folders;
    }

    public Set<FileDescriptor> getFiles() {
        return files;
    }

    public void setFiles(Set<FileDescriptor> files) {
        this.files = files;
    }

    @Override
    public int compareTo(Folder folder) {
        if (this.name.compareTo(folder.name) < 0) {
            return -1;
        } else if (this.name.compareTo(folder.name) > 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
