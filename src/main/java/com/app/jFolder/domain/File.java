package com.app.jFolder.domain;

import javax.persistence.*;

@Entity
@Table(name = "file", indexes = {@Index(name = "idx_file_name", columnList = "file_name")})
public class File implements Comparable<File>{
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "file_name")
    private String name;
    @Column(name = "file_system_name")
    private String systemName;
    private Integer number;
    @ManyToOne
    private Folder folder;
    private String path;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public int compareTo(File file) {
        if (this.name.compareTo(file.name) < 0) {
            return -1;
        } else if (this.name.compareTo(file.name) > 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
