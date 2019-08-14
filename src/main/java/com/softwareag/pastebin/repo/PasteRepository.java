package com.softwareag.pastebin.repo;

import com.softwareag.pastebin.model.Attachment;
import com.softwareag.pastebin.model.Paste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PasteRepository extends JpaRepository<Paste, Long> {

    Paste findByKey(UUID uuid);

    List<Paste> findFirst6ByUploadedBeforeOrderByUploadedDesc(LocalDateTime beforeDate);
    List<Paste> findAllByOrderByUploadedDesc();

    List<Paste> findByIpOrderByUploadedDesc(String ip);
}
