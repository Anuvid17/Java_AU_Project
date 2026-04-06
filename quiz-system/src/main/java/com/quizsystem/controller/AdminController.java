package com.quizsystem.controller;

import com.quizsystem.model.*;
import com.quizsystem.service.QuestionService;
import com.quizsystem.service.QuizService;
import com.quizsystem.service.ResultService;
import com.quizsystem.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Admin controller for managing categories, quizzes, questions, and viewing scores.
 * All routes require ADMIN role.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final QuizService quizService;
    private final QuestionService questionService;
    private final ResultService resultService;
    private final UserService userService;

    public AdminController(QuizService quizService, QuestionService questionService,
                           ResultService resultService, UserService userService) {
        this.quizService = quizService;
        this.questionService = questionService;
        this.resultService = resultService;
        this.userService = userService;
    }

    // ===== Category Management =====

    /** List all categories */
    @GetMapping("/categories")
    public String listCategories(Model model) {
        model.addAttribute("categories", quizService.getAllCategories());
        return "admin/categories";
    }

    /** Show add category form */
    @GetMapping("/categories/add")
    public String addCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("isEdit", false);
        return "admin/category-form";
    }

    /** Show edit category form */
    @GetMapping("/categories/edit/{id}")
    public String editCategoryForm(@PathVariable Long id, Model model) {
        Category category = quizService.getCategoryById(id).orElse(null);
        if (category == null) return "redirect:/admin/categories";
        model.addAttribute("category", category);
        model.addAttribute("isEdit", true);
        return "admin/category-form";
    }

    /** Save category (add or update) */
    @PostMapping("/categories/save")
    public String saveCategory(@ModelAttribute Category category, RedirectAttributes redirectAttributes) {
        quizService.saveCategory(category);
        redirectAttributes.addFlashAttribute("success", "Category saved successfully!");
        return "redirect:/admin/categories";
    }

    /** Delete category */
    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        quizService.deleteCategory(id);
        redirectAttributes.addFlashAttribute("success", "Category deleted successfully!");
        return "redirect:/admin/categories";
    }

    // ===== Quiz Management =====

    /** List all quizzes */
    @GetMapping("/quizzes")
    public String listQuizzes(Model model) {
        model.addAttribute("quizzes", quizService.getAllQuizzes());
        return "admin/quizzes";
    }

    /** Show add quiz form */
    @GetMapping("/quizzes/add")
    public String addQuizForm(Model model) {
        model.addAttribute("quiz", new Quiz());
        model.addAttribute("categories", quizService.getAllCategories());
        model.addAttribute("isEdit", false);
        return "admin/quiz-form";
    }

    /** Show edit quiz form */
    @GetMapping("/quizzes/edit/{id}")
    public String editQuizForm(@PathVariable Long id, Model model) {
        Quiz quiz = quizService.getQuizById(id).orElse(null);
        if (quiz == null) return "redirect:/admin/quizzes";
        model.addAttribute("quiz", quiz);
        model.addAttribute("categories", quizService.getAllCategories());
        model.addAttribute("isEdit", true);
        return "admin/quiz-form";
    }

    /** Save quiz (add or update) */
    @PostMapping("/quizzes/save")
    public String saveQuiz(@ModelAttribute Quiz quiz, @RequestParam Long categoryId,
                           RedirectAttributes redirectAttributes) {
        Category category = quizService.getCategoryById(categoryId).orElse(null);
        if (category == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid category!");
            return "redirect:/admin/quizzes";
        }
        quiz.setCategory(category);
        quizService.saveQuiz(quiz);
        redirectAttributes.addFlashAttribute("success", "Quiz saved successfully!");
        return "redirect:/admin/quizzes";
    }

    /** Delete quiz */
    @GetMapping("/quizzes/delete/{id}")
    public String deleteQuiz(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        quizService.deleteQuiz(id);
        redirectAttributes.addFlashAttribute("success", "Quiz deleted successfully!");
        return "redirect:/admin/quizzes";
    }

    // ===== Question Management =====

    /** List all questions (optionally filtered by quiz) */
    @GetMapping("/questions")
    public String listQuestions(@RequestParam(required = false) Long quizId, Model model) {
        if (quizId != null) {
            model.addAttribute("questions", questionService.getQuestionsByQuizId(quizId));
            model.addAttribute("selectedQuiz", quizService.getQuizById(quizId).orElse(null));
        } else {
            model.addAttribute("questions", questionService.getAllQuestions());
        }
        model.addAttribute("quizzes", quizService.getAllQuizzes());
        return "admin/questions";
    }

    /** Show add question form */
    @GetMapping("/questions/add")
    public String addQuestionForm(@RequestParam(required = false) Long quizId, Model model) {
        model.addAttribute("quizzes", quizService.getAllQuizzes());
        model.addAttribute("selectedQuizId", quizId);
        return "admin/question-form";
    }

    /** Save a new question with options */
    @PostMapping("/questions/save")
    public String saveQuestion(@RequestParam Long quizId,
                               @RequestParam String questionText,
                               @RequestParam String option1,
                               @RequestParam String option2,
                               @RequestParam String option3,
                               @RequestParam String option4,
                               @RequestParam int correctOption,
                               RedirectAttributes redirectAttributes) {
        String[] options = {option1, option2, option3, option4};
        int[] correct = {correctOption};
        questionService.addQuestionWithOptions(quizId, questionText, options, correct);
        redirectAttributes.addFlashAttribute("success", "Question added successfully!");
        return "redirect:/admin/questions?quizId=" + quizId;
    }

    /** Delete a question */
    @GetMapping("/questions/delete/{id}")
    public String deleteQuestion(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        questionService.deleteQuestion(id);
        redirectAttributes.addFlashAttribute("success", "Question deleted successfully!");
        return "redirect:/admin/questions";
    }

    // ===== Scores View =====

    /** View all user scores */
    @GetMapping("/scores")
    public String viewScores(Model model) {
        model.addAttribute("results", resultService.getAllResults());
        return "admin/scores";
    }
}
