package com.softwareag.pastebin.service;

import com.softwareag.pastebin.model.Paste;
import com.softwareag.pastebin.repo.PasteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BaseServiceImpl implements BaseService {

    private final PasteRepository pasteRepo;

    public BaseServiceImpl(PasteRepository pasteRepo) {
        this.pasteRepo = pasteRepo;
    }

    @Override
    public Paste findPasteByUUID(String uuid) {
        return pasteRepo.findByKey(UUID.fromString(uuid));
    }

    @Override
    public List<Paste> findPasteByIp(String ip) {
        return pasteRepo.findByIpOrderByUploadedDesc(ip);
    }

    @Override
    public List<Paste> findAll() {
        return pasteRepo.findAllByOrderByUploadedDesc();
    }
}
