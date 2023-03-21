package com.udacity.superduperdrive.cloudstorage.controller;


import com.udacity.superduperdrive.cloudstorage.model.Note;
import com.udacity.superduperdrive.cloudstorage.service.CredentialService;
import com.udacity.superduperdrive.cloudstorage.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class NoteController {
    private NoteService noteService;
    private UserService userService;
    private FileService fileService;
    private CredentialService credentialService;

    public NoteController(NoteService noteService, UserService userService, FileService fileService, CredentialService credentialService) {
        this.noteService = noteService;
        this.userService = userService;
        this.fileService = fileService;
        this.credentialService = credentialService;
    }

    @PostMapping("/note/add")
    public String uploadFile(@RequestParam("noteTitle") String noteTitle,
                             @RequestParam("noteDescription") String noteDescription,
                             @RequestParam("noteId") Integer noteId,
                             Authentication auth,
                             Model model) {
        Integer userId = userService.getUserIdByUserName(auth.getName());
        if (userId == null || userId <= 0)
        {
            model.addAttribute("noteError", "Could not find the user id. Kindly logout and login again.");
            return "home";
        }
        try {
            if (noteId == null || noteId <= 0) {
                this.noteService.createNote(noteTitle, noteDescription, userId);
                model.addAttribute("noteSuccess", "Add note successfully.");
            } else {
                this.noteService.updateNote(noteId, noteTitle, noteDescription);
                model.addAttribute("noteSuccess", "Update note successfully.");
            }
        } catch(Exception e) {
            model.addAttribute("noteError", e.toString());
        }
        model.addAttribute("files", this.fileService.getFilesByUserId(userId));
        model.addAttribute("notes", this.noteService.getNotesByUserId(userId));
        model.addAttribute("credentials", this.credentialService.getCredentialsByUserId(userId));

        return "home";
    }

    @GetMapping(value = "/note/detail/{noteId}")
    public Note getNote(@PathVariable Integer noteId) {
        return noteService.getNote(noteId);
    }

    @RequestMapping("/note/delete/{noteId}")
    public String deleteNote(@PathVariable("noteId") Integer noteId,
                             Authentication auth,
                             Model model) {
        try {
            noteService.deleteNote(noteId);
            model.addAttribute("noteSuccess", "Note successfully deleted.");
        } catch (Exception e) {
            model.addAttribute("noteError", e.toString());
        }

        Integer userId = userService.getUserIdByUserName(auth.getName());
        if (userId == null || userId <= 0)
        {
            model.addAttribute("noteError", "Could not find the user id. Kindly logout and login again.");
            return "home";
        }

        model.addAttribute("files", this.fileService.getFilesByUserId(userId));
        model.addAttribute("notes", this.noteService.getNotesByUserId(userId));
        model.addAttribute("credentials", this.credentialService.getCredentialsByUserId(userId));
        return "home";
    }
}
