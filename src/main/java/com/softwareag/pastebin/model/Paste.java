package com.softwareag.pastebin.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.type.UUIDCharType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Paste {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "uuid")
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID key;

    @Lob
    private String details;
    private String ip;
    private LocalDateTime uploaded;
    private String attachment;

    public Paste() {
    }

    public Paste(String text, String ip, LocalDateTime uploaded, String attachment) {
        this.details = text;
        this.ip = ip;
        this.uploaded = uploaded;
        this.attachment = attachment;
        this.key = UUID.randomUUID();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paste paste = (Paste) o;
        return id == paste.id &&
                Objects.equals(key, paste.key) &&
                Objects.equals(details, paste.details) &&
                Objects.equals(ip, paste.ip) &&
                Objects.equals(uploaded, paste.uploaded) &&
                Objects.equals(attachment, paste.attachment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, key, details, ip, uploaded, attachment);
    }

    @Override
    public String toString() {
        return "Paste{" +
                "id=" + id +
                ", key=" + key +
                ", text='" + details + '\'' +
                ", ip='" + ip + '\'' +
                ", uploaded=" + uploaded +
                ", attachment='" + attachment + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UUID getKey() {
        return key;
    }

    public void setKey(UUID key) {
        this.key = key;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public LocalDateTime getUploaded() {
        return uploaded;
    }

    public void setUploaded(LocalDateTime uploaded) {
        this.uploaded = uploaded;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}
