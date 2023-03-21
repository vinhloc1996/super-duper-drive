package com.udacity.superduperdrive.cloudstorage.controller;

import com.udacity.superduperdrive.cloudstorage.model.User;
import com.udacity.superduperdrive.cloudstorage.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignupController {
    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String signupView() {
        return "signup";
    }

    @PostMapping()
    public String signupUser(@ModelAttribute User user, Model model) {
        if (userService.isUsernameExisted(user.getUsername())) {
            model.addAttribute("signupError", String.format("The username %s already exists.", user.getUsername()));
            return "signup";
        }

        int add = userService.createUser(user);
        if (add < 0) {
            model.addAttribute("signupError", "Something went wrong. Kindly refresh the page and try again.");
            return "signup";
        }

        model.addAttribute("signupSuccess", true);

        return "redirect:/login";
    }
}
