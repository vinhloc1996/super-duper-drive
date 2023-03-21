package com.udacity.superduperdrive.cloudstorage.controller;

import com.udacity.superduperdrive.cloudstorage.helper.UtilityMethod;
import com.udacity.superduperdrive.cloudstorage.model.Credential;
import com.udacity.superduperdrive.cloudstorage.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CredentialController {
    private NoteService noteService;
    private UserService userService;
    private FileService fileService;
    private CredentialService credentialService;
    private final EncryptionService encryptionService;

    public CredentialController(NoteService noteService, UserService userService, FileService fileService, CredentialService credentialService, EncryptionService encryptionService) {
        this.noteService = noteService;
        this.userService = userService;
        this.fileService = fileService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @PostMapping("/credential/add")
    public String addCredential(@RequestParam("credentialId") Integer credentialId,
                                   @RequestParam("url") String credentialUrl,
                                   @RequestParam("username") String credentialUsername,
                                   @RequestParam("password") String credentialPassword,
                                   Authentication auth,
                                   Model model) {
        Integer userId = userService.getUserIdByUserName(auth.getName());
        if (userId == null || userId <= 0)
        {
            model.addAttribute("credentialError", "Could not find the user id. Kindly logout and login again.");
            return "home";
        }

        try {
            if (credentialId == null || credentialId <= 0) {
                this.credentialService.createCredential(credentialUrl,
                        credentialUsername,
                        credentialPassword,
                        userId);
                model.addAttribute("credentialSuccess", "Add credential successfully.");
            } else {
                this.credentialService.updateCredential(credentialId,
                        credentialUsername,
                        credentialUrl,
                        credentialPassword);
                model.addAttribute("credentialSuccess", "Update credential successfully.");
            }
        } catch(Exception e) {
            model.addAttribute("credentialError", e.toString());
        }

        model.addAttribute("files", this.fileService.getFilesByUserId(userId));
        model.addAttribute("notes", this.noteService.getNotesByUserId(userId));
        model.addAttribute("credentials", this.credentialService.getCredentialsByUserId(userId));
        model.addAttribute("encryptionService", encryptionService);
        return "home";
    }

    @GetMapping(value = "/credential/detail/{credentialId}")
    public Credential getCredential(@PathVariable Integer credentialId) {
        return credentialService.getCredentialById(credentialId);
    }

    @GetMapping(value = "/credential/show-password/{credentialId}")
    @ResponseBody
    public String decodePassword(@PathVariable Integer credentialId){
        Credential credential = credentialService.getCredentialById(credentialId);

        String decryptedPassword = UtilityMethod.DecryptPasswordWithSalt(credential.getPassword(), credential.getKey());
        return decryptedPassword;
    }

    @RequestMapping("/credential/delete/{credentialId}")
    public String deleteCredential(@PathVariable("credentialId") Integer credentialId,
                                   Authentication auth,
                                   Model model) {
        Integer userId = userService.getUserIdByUserName(auth.getName());
        if (userId == null || userId <= 0)
        {
            model.addAttribute("credentialError", "Could not find the user id. Kindly logout and login again.");
            return "home";
        }
        try {
            this.credentialService.deleteCredential(credentialId);
            model.addAttribute("credentialSuccess", "Delete credential successfully.");
        } catch (Exception e) {
            model.addAttribute("credentialError", e.toString());
        }

        model.addAttribute("files", this.fileService.getFilesByUserId(userId));
        model.addAttribute("notes", this.noteService.getNotesByUserId(userId));
        model.addAttribute("credentials", this.credentialService.getCredentialsByUserId(userId));
        return "home";
    }
}
