package com.app.jFolder.domain;

import javax.persistence.*;

@Entity
@Table(name = "file_version")
public class FileVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "file_system_name")
    private String systemName;
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer number;
    private String path;
    @ManyToOne
    private FileDescriptor file;
    private boolean last;
    private String hash_file;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public FileDescriptor getFile() {
        return file;
    }

    public void setFile(FileDescriptor file) {
        this.file = file;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public String getHash_file() {
        return hash_file;
    }

    public void setHash_file(String hash_file) {
        this.hash_file = hash_file;
    }
}
