package com.softwareag.pastebin.controller;

import com.softwareag.pastebin.model.Paste;
import com.softwareag.pastebin.model.dto.PasteDTO;
import com.softwareag.pastebin.repo.PasteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;
//import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Controller
@Validated
public class HomeController {

    private static final String UUID_REGEX = "([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})";
    final PasteRepository pasteRepo;

    public HomeController(PasteRepository pasteRepo) {
        this.pasteRepo = pasteRepo;
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

        Paste paste = pasteRepo.findByKey(UUID.fromString(uuid));
        System.out.println("paste is "+ paste);
        if(paste == null)
            return "redirect:/";

        model.addAttribute("paste", paste);

        return "snippet";
    }

    @PostMapping("/submit")
    public ModelAndView submitForm(@ModelAttribute PasteDTO paste, HttpServletRequest request, ModelAndView modelAndView, RedirectAttributes redir) {
        String ip = getIpAddress();

        String baseUrl = request.getRequestURL().toString().replace("/submit", "");
        Paste newPaste = new Paste(paste.getDetails(), ip, LocalDateTime.now(), paste.getAttachment());
        newPaste = pasteRepo.saveAndFlush(newPaste);
        String url = baseUrl.concat("/").concat(newPaste.getKey().toString());
        redir.addFlashAttribute("uploaded", true);
        redir.addFlashAttribute("url", url);
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    private void addDefaultAttributes(Model model) {
        model.addAttribute("pasteDTO", new PasteDTO());
        List<Paste> all = pasteRepo.findAll();
        int maxSize = 6;
        model.addAttribute("history", all.subList(0, (all.size() > maxSize) ? maxSize - 1 : all.size()));
    }

    private String getIpAddress() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
            .getRequest();
        return request.getRemoteAddr();
    }
}
