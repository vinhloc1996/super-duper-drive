package com.udacity.superduperdrive.cloudstorage.controller;

import com.udacity.superduperdrive.cloudstorage.model.User;
import com.udacity.superduperdrive.cloudstorage.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final FileService fileService;
    private final UserService userService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;

    public HomeController(FileService fileService, UserService userService, NoteService noteService, CredentialService credentialService, EncryptionService encryptionService) {
        this.fileService = fileService;
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @RequestMapping()
    public String homepageView(Authentication authentication, Model model) {
        if (!authentication.isAuthenticated())
            return "redirect:/login.html";

        String username = authentication.getName();
        User user = userService.getUser(username);
        if (user == null){
            return "login";
        }
        model.addAttribute("files", fileService.getFilesByUserId(user.getUserId()));
        model.addAttribute("notes", noteService.getNotesByUserId(user.getUserId()));
        model.addAttribute("credentials", credentialService.getCredentialsByUserId(user.getUserId()));
        model.addAttribute("encryptionService", encryptionService);
        return "home";
    }
}
