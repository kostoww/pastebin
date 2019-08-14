package com.softwareag.pastebin.controller;

import com.softwareag.pastebin.model.Attachment;
import com.softwareag.pastebin.model.Paste;
import com.softwareag.pastebin.model.dto.AttachmentDTO;
import com.softwareag.pastebin.model.dto.PasteDTO;
import com.softwareag.pastebin.repo.PasteRepository;
import com.softwareag.pastebin.service.BaseService;
import com.softwareag.pastebin.service.StorageService;
import com.softwareag.pastebin.util.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;
//import javax.validation.constraints.Pattern;
import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Controller
@Validated
public class HomeController {


    private static final String UUID_REGEX = "([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})";

    private final PasteRepository pasteRepo;
    private final BaseService baseService;
    private final StorageService storageService;

    public HomeController(PasteRepository pasteRepo, BaseService baseService, StorageService storageService) {
        this.pasteRepo = pasteRepo;
        this.baseService = baseService;
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String getIndex(Model model) {
        model.addAttribute("paste", new PasteDTO());
        addDefaultAttributes(model);
        return "index";
    }

    @GetMapping("/{uuid}")
    public String getById(@Pattern(regexp = UUID_REGEX ) @PathVariable String uuid, Model model) {
        addDefaultAttributes(model); //
        Paste paste = baseService.findPasteByUUID(uuid);

        if(paste == null)
            return "redirect:/";

        List<AttachmentDTO> attachments = new LinkedList<>();

        for(Attachment attachment : paste.getAttachment()) {
            Path filePath = storageService.load(attachment.getName().toString());
            File file = filePath.toFile();
            String url = MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                    "serveFile", filePath.getFileName().toString()).build().toString();
            AttachmentDTO attachmentDTO = new AttachmentDTO(attachment, url, Utils.readableFileSize(file.length()), file.lastModified());
            attachmentDTO.setContent(Utils.readFile(filePath));
            attachments.add(attachmentDTO);
        }

        model.addAttribute("attachmentsDTO", attachments.size() > 0 ? attachments : null);
        model.addAttribute("paste", paste);
        model.addAttribute("renderAttachments", attachments.size() > 0);
        model.addAttribute("renderSnippet", (paste.getDetails() != null && !paste.getDetails().isEmpty()));

        return "snippet";
    }

    @GetMapping("/by/{ip}")
    public String getByIp(@PathVariable String ip, Model model) {
        List<Paste> pastes = baseService.findPasteByIp(ip);
        if(pastes == null || pastes.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("pastes", pastes);
        model.addAttribute("ip", ip);
        return "list";
    }

    @GetMapping("/all")
    public String getAll(Model model) {
        addDefaultAttributes(model);
        List<Paste> pastes = baseService.findAll();

        model.addAttribute("pastes", pastes);
        return "list";
    }

    @GetMapping("/my")
    public String getAllForClient(Model model) {
        String ip = Utils.getIpAddress();
        List<Paste> pastes = baseService.findPasteByIp(ip);
        model.addAttribute("pastes", pastes);
        model.addAttribute("ip", ip);
        return "list";
    }

    @PostMapping("/submit")
    public ModelAndView submitForm(@RequestParam(name = "file", required = false) MultipartFile[] files,
                                   RedirectAttributes redirectAttributes,
                                   @ModelAttribute PasteDTO paste, HttpServletRequest request, ModelAndView modelAndView, RedirectAttributes redir) {

        String ip = Utils.getIpAddress();

        String baseUrl = request.getRequestURL().toString().replace("/submit", "");


        List<Attachment> attachments = new LinkedList<>();
        if(files != null && files.length > 0 ) {

            for (MultipartFile file : files) {
                if(!file.isEmpty()) {
                    String originalName = file.getOriginalFilename();
                    String newName = storageService.store(file);
                    attachments.add(new Attachment(newName, originalName));
                } else if (paste.getDetails().isEmpty()) {
                    modelAndView.setViewName("redirect:/");
                    return modelAndView;
                }
            }
        }

        Paste newPaste = new Paste(paste.getDetails(), ip, LocalDateTime.now(), attachments);
        newPaste = pasteRepo.saveAndFlush(newPaste);
        String url = baseUrl.concat("/").concat(newPaste.getKey().toString());
        redir.addFlashAttribute("uploaded", true);
        redir.addFlashAttribute("url", url);

        redir.addFlashAttribute("files", attachments);
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    private void addDefaultAttributes(Model model) {
        model.addAttribute("pasteDTO", new PasteDTO());
        List<Paste> lastSeven = pasteRepo.findFirst6ByUploadedBeforeOrderByUploadedDesc(LocalDateTime.now());
        model.addAttribute("history", lastSeven);
    }


}
