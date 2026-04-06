package com.quizsystem.controller;

import com.quizsystem.model.Category;
import com.quizsystem.service.QuizService;
import com.quizsystem.service.ResultService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Controller for displaying per-category leaderboards.
 * Shows top scores ranked by score (descending) and time taken (ascending).
 */
@Controller
public class LeaderboardController {

    private final QuizService quizService;
    private final ResultService resultService;

    public LeaderboardController(QuizService quizService, ResultService resultService) {
        this.quizService = quizService;
        this.resultService = resultService;
    }

    /**
     * Display leaderboard page with all categories.
     * Default shows the first category's leaderboard.
     */
    @GetMapping("/leaderboard")
    public String leaderboard(Model model) {
        var categories = quizService.getAllCategories();
        model.addAttribute("categories", categories);

        if (!categories.isEmpty()) {
            Long firstCategoryId = categories.get(0).getId();
            model.addAttribute("selectedCategoryId", firstCategoryId);
            model.addAttribute("results", resultService.getLeaderboard(firstCategoryId));
        }
        return "leaderboard";
    }

    /**
     * Display leaderboard for a specific category.
     */
    @GetMapping("/leaderboard/{categoryId}")
    public String leaderboardByCategory(@PathVariable Long categoryId, Model model) {
        model.addAttribute("categories", quizService.getAllCategories());
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("results", resultService.getLeaderboard(categoryId));

        Category category = quizService.getCategoryById(categoryId).orElse(null);
        model.addAttribute("selectedCategory", category);
        return "leaderboard";
    }
}
