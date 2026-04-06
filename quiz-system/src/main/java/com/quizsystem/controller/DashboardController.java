package com.quizsystem.controller;

import com.quizsystem.model.Category;
import com.quizsystem.model.Result;
import com.quizsystem.model.User;
import com.quizsystem.service.QuizService;
import com.quizsystem.service.ResultService;
import com.quizsystem.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Controller for the main dashboard.
 * Routes to admin or student dashboard based on user role.
 */
@Controller
public class DashboardController {

    private final UserService userService;
    private final QuizService quizService;
    private final ResultService resultService;

    public DashboardController(UserService userService, QuizService quizService, ResultService resultService) {
        this.userService = userService;
        this.quizService = quizService;
        this.resultService = resultService;
    }

    /**
     * Main dashboard - routes based on role.
     */
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        // Check if user is admin
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return adminDashboard(model);
        }

        return studentDashboard(user, model);
    }

    /**
     * Admin dashboard with system statistics.
     */
    private String adminDashboard(Model model) {
        model.addAttribute("totalUsers", userService.countUsers());
        model.addAttribute("totalCategories", quizService.countCategories());
        model.addAttribute("totalQuizzes", quizService.countQuizzes());
        model.addAttribute("totalAttempts", resultService.countAttempts());
        model.addAttribute("recentResults", resultService.getAllResults());
        return "admin/dashboard";
    }

    /**
     * Student dashboard with categories and recent scores.
     */
    private String studentDashboard(User user, Model model) {
        List<Category> categories = quizService.getAllCategories();
        List<Result> recentResults = resultService.getUserResults(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("categories", categories);
        model.addAttribute("recentResults", recentResults);
        return "dashboard";
    }
}
