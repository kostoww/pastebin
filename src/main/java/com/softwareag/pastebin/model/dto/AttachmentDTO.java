package com.softwareag.pastebin.model.dto;

import com.softwareag.pastebin.model.Attachment;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.TimeZone;

public class AttachmentDTO {
    private String uuid;
    private long downloaded;
    private String originalName;
    private String url;
    private String fileSize;
    private LocalDateTime lastModified;
    private String content;

    public AttachmentDTO(Attachment attachment, String url, String fileSize, long lastModified) {
        this.uuid = attachment.getName().toString();
        this.downloaded = attachment.getDownloaded();
        this.originalName = attachment.getOriginalName();
        this.url = url;
        this.fileSize = fileSize;
        this.lastModified = LocalDateTime.ofInstant(Instant.ofEpochMilli(lastModified),
                TimeZone.getDefault().toZoneId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttachmentDTO that = (AttachmentDTO) o;
        return downloaded == that.downloaded &&
                Objects.equals(uuid, that.uuid) &&
                Objects.equals(originalName, that.originalName) &&
                Objects.equals(url, that.url) &&
                Objects.equals(fileSize, that.fileSize) &&
                Objects.equals(lastModified, that.lastModified) &&
                Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, downloaded, originalName, url, fileSize, lastModified, content);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(long downloaded) {
        this.downloaded = downloaded;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
