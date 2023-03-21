package com.udacity.superduperdrive.cloudstorage.controller;

import com.udacity.superduperdrive.cloudstorage.helper.UtilityMethod;
import com.udacity.superduperdrive.cloudstorage.model.File;
import com.udacity.superduperdrive.cloudstorage.model.User;
import com.udacity.superduperdrive.cloudstorage.service.CredentialService;
import com.udacity.superduperdrive.cloudstorage.service.FileService;
import com.udacity.superduperdrive.cloudstorage.service.NoteService;
import com.udacity.superduperdrive.cloudstorage.service.UserService;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileController {
    private NoteService noteService;
    private UserService userService;
    private FileService fileService;
    private CredentialService credentialService;

    public FileController(NoteService noteService, UserService userService, FileService fileService, CredentialService credentialService) {
        this.noteService = noteService;
        this.userService = userService;
        this.fileService = fileService;
        this.credentialService = credentialService;
    }

    @PostMapping("/file/add")
    public String addFile(@RequestParam("fileUpload") MultipartFile fileUpload,
                          Authentication auth,
                          Model model) {
        try{
            User user = this.userService.getUser(auth.getName());
            if (UtilityMethod.IsStringNullOrEmpty(fileUpload.getOriginalFilename())) {
                model.addAttribute("fileError", "Please upload file");
            } else {
                if (!this.fileService.isFilenameExisted(fileUpload.getOriginalFilename(), user.getUserId())) {
                    this.fileService.createFile(fileUpload, user.getUserId());
                    model.addAttribute("fileSuccess", "Add new file successully");
                } else {
                    model.addAttribute("fileError", String.format("Filename %s is already existed. Kindly choose difference name", fileUpload.getOriginalFilename()));
                }
            }

            model.addAttribute("files", fileService.getFilesByUserId(user.getUserId()));
            model.addAttribute("notes", noteService.getNotesByUserId(user.getUserId()));
            model.addAttribute("credentials", credentialService.getCredentialsByUserId(user.getUserId()));
        }catch(Exception e){
            model.addAttribute("fileError", e.toString());
        }

        return "home";
    }

    @RequestMapping(value = "/file/detail/{fileid}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity detailFile(@PathVariable("fileid") Integer fileId) {
        File file = this.fileService.getFileById(fileId);
        String contentType = file.getContentType();
        String fileName = file.getFileName();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", fileName))
                .body(file.getFileData());
    }

    @RequestMapping("/file/delete/{fileid}")
    public String deleteFile(@PathVariable("fileid") Integer fileId,
                             Authentication auth,
                             Model model) {
        try {
            fileService.deleteFile(fileId);
            model.addAttribute("fileDeleteSuccess", "Delete file successfully.");
        } catch (Exception e) {
            model.addAttribute("fileError", e.toString());
        }

        User user = this.userService.getUser(auth.getName());
        model.addAttribute("files", fileService.getFilesByUserId(user.getUserId()));
        model.addAttribute("notes", noteService.getNotesByUserId(user.getUserId()));
        model.addAttribute("credentials", credentialService.getCredentialsByUserId(user.getUserId()));
        return "home";
    }
}