package com.softwareag.pastebin.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Attachment {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "uuid")
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID name;

    private String originalName;

    private long downloaded;


    public Attachment() {
    }

    public Attachment(String newName, String originalName) {
        this.name = UUID.fromString(newName);
        this.originalName = originalName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attachment that = (Attachment) o;
        return id == that.id &&
                Objects.equals(originalName, that.originalName) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, originalName, name);
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "id=" + id +
                ", name=" + name +
                ", downloaded=" + downloaded +
                ", originalName=" + originalName +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UUID getName() {
        return name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public void setName(UUID name) {
        this.name = name;
    }

    public long getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(long downloaded) {
        this.downloaded = downloaded;
    }
}
