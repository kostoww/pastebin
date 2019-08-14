package com.softwareag.pastebin.service;

import com.softwareag.pastebin.model.Paste;

import java.util.List;

public interface BaseService {

    Paste findPasteByUUID(String uuid);

    List<Paste> findPasteByIp(String ip);

    List<Paste> findAll();
}
