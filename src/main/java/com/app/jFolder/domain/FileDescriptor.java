package com.app.jFolder.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "file", indexes = {@Index(name = "idx_file_name", columnList = "file_name")})
public class FileDescriptor implements Comparable<FileDescriptor>{
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "file_name")
    private String name;
    @ManyToOne
    private Folder folder;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "file")
    private Set<FileVersion> fileVersionSet;

    public FileDescriptor(String name, Folder folder, Set<FileVersion> fileVersionSet) {
        this.name = name;
        this.folder = folder;
        this.fileVersionSet = fileVersionSet;
    }

    public FileDescriptor() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<FileVersion> getFileVersionSet() {
        return fileVersionSet;
    }

    public void setFileVersionSet(Set<FileVersion> fileVersionSet) {
        this.fileVersionSet = fileVersionSet;
    }

    @Override
    public int compareTo(FileDescriptor file) {
        return Integer.compare(this.name.compareTo(file.name), 0);
    }
}
