package com.softwareag.pastebin.controller;

import com.softwareag.pastebin.model.Attachment;
import com.softwareag.pastebin.model.Paste;
import com.softwareag.pastebin.repo.AttachmentRepository;
import com.softwareag.pastebin.repo.PasteRepository;
import com.softwareag.pastebin.service.StorageService;
import com.softwareag.pastebin.storage.StorageFileNotFoundException;
import com.softwareag.pastebin.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Path;
import java.util.UUID;

@Controller
public class FileUploadController {
    private static String NL = "\n";
    private final StorageService storageService;
    private final AttachmentRepository attachmentRepo;

    @Autowired
    public FileUploadController(StorageService storageService, AttachmentRepository attachmentRepo) {
        this.storageService = storageService;
        this.attachmentRepo = attachmentRepo;
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Attachment attachment = attachmentRepo.findByName(UUID.fromString(filename));

        Resource file = storageService.loadAsResource(attachment.getName().toString());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + attachment.getOriginalName() + "\"").body(file);

    }

    @GetMapping(value = "/rawFile/{uuid}", produces = "text/html")
    @ResponseBody
    public String rawFile(@PathVariable String uuid) {
        //TODO: Security check
        Path file = storageService.load(uuid);
        String body = String.format(
                "<html>" + NL +
                "<head>" + NL +
                "</head>" + NL +
                "<body>" + NL +
                "<pre>" + NL +
                "%s" + NL +
                "</pre>" + NL +
                "</body>" + NL +
                "</html>", Utils.readFile(file));
        return body;
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}