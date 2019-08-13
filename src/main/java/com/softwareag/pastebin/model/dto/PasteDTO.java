package com.softwareag.pastebin.model.dto;

public class PasteDTO {
    private String details;
    private String attachment;
    private boolean doExpire;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public boolean isDoExpire() {
        return doExpire;
    }

    public void setDoExpire(boolean doExpire) {
        this.doExpire = doExpire;
    }
}
