package com.softwareag.pastebin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class CurlController {

    @PostMapping(value = "/api/upload", produces ="text/plain")
    public ResponseEntity<String> uploadFile(@RequestBody String body) {
        System.out.println(body);
        return ResponseEntity.ok("ok");
    }

}
