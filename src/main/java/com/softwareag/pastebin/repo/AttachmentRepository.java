package com.softwareag.pastebin.repo;

import com.softwareag.pastebin.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    Attachment findByName(UUID filename);
}
