package com.app.jFolder.domain;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "file_version")
public class FileVersion implements Comparable<FileVersion> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "file_system_name")
    private String systemName;
    private Integer number;
    private String description;
    private Instant date;
    private String path;
    @ManyToOne
    private FileDescriptor file;
    private boolean last;
    private String hash_file;

    public FileVersion(String systemName, String path,
                       FileDescriptor file, boolean last,
                       String hash_file) {
        this.systemName = systemName;
        this.path = path;
        this.file = file;
        this.last = last;
        this.hash_file = hash_file;
    }

    public FileVersion() {
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    @Override
    public int compareTo(FileVersion o) {
        if (this.number > o.getNumber()) {
            return -1;
        } else if (this.number < o.getNumber()) {
            return 1;
        } else {
            return 0;
        }
//        return Integer.compare(this.number.compareTo(o.number), 0);
    }
}
