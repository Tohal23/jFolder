package com.app.jFolder.dto;

import com.app.jFolder.domain.FileDescriptor;
import com.app.jFolder.domain.FileVersion;
import com.app.jFolder.domain.Folder;

public class FileDescriptorDto {

    private Long id;
    private String name;
    private Folder folder;
    private Integer lastVersion;

    public FileDescriptorDto(FileDescriptor fileDescriptor) {
        this.id = fileDescriptor.getId();
        this.name = fileDescriptor.getName();
        this.folder = fileDescriptor.getFolder();
        this.lastVersion = fileDescriptor.getFileVersionSet().stream().sorted().findFirst().orElse(new FileVersion()).getNumber();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public Integer getLastVersion() {
        return lastVersion;
    }

    public void setLastVersion(Integer lastVersion) {
        this.lastVersion = lastVersion;
    }
}
