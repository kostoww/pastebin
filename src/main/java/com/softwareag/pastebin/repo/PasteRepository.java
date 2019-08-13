package com.softwareag.pastebin.repo;

import com.softwareag.pastebin.model.Paste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PasteRepository extends JpaRepository<Paste, Long> {
    Paste findByKey(UUID uuid);
}
