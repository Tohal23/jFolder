package com.app.jFolder.dto;

import com.app.jFolder.domain.FileVersion;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class FileVersionDto implements Comparable<FileVersionDto> {

    private Long id;
    private String systemName;
    private Integer number;
    private String description;
    private String date;
    private boolean last;

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                    .withLocale( Locale.UK )
                    .withZone( ZoneId.systemDefault() );

    public FileVersionDto(FileVersion fileVersion) {
        this.id = fileVersion.getId();
        this.systemName = fileVersion.getSystemName();
        this.number = fileVersion.getNumber();
        this.description = fileVersion.getDescription();
        this.date = formatter.format(fileVersion.getDate());
        this.last = fileVersion.isLast();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    @Override
    public int compareTo(FileVersionDto o) {
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
