package com.quizsystem.controller;

import com.quizsystem.dto.UserRegistrationDTO;
import com.quizsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller handling user authentication: login and registration.
 */
@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Display the login page.
     */
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    /**
     * Display the registration page.
     */
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new UserRegistrationDTO());
        return "auth/register";
    }

    /**
     * Process user registration.
     */
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserRegistrationDTO dto,
                               BindingResult result, RedirectAttributes redirectAttributes) {
        // Validation errors
        if (result.hasErrors()) {
            return "auth/register";
        }

        // Password confirmation
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.user", "Passwords do not match");
            return "auth/register";
        }

        // Attempt registration
        boolean registered = userService.registerUser(dto);
        if (!registered) {
            result.rejectValue("username", "error.user", "Username already exists");
            return "auth/register";
        }

        redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
        return "redirect:/login";
    }

    /**
     * Redirect root URL to dashboard (or login if not authenticated).
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }
}
