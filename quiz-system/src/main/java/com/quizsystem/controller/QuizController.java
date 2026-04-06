package com.quizsystem.controller;

import com.quizsystem.model.*;
import com.quizsystem.service.QuizService;
import com.quizsystem.service.ResultService;
import com.quizsystem.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

/**
 * Controller for quiz-taking flow: category selection, quiz attempt, submission, and results.
 */
@Controller
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;
    private final ResultService resultService;
    private final UserService userService;

    public QuizController(QuizService quizService, ResultService resultService, UserService userService) {
        this.quizService = quizService;
        this.resultService = resultService;
        this.userService = userService;
    }

    /**
     * Display all quiz categories.
     */
    @GetMapping("/categories")
    public String showCategories(Model model) {
        model.addAttribute("categories", quizService.getAllCategories());
        return "quiz/categories";
    }

    /**
     * Display quizzes within a category.
     */
    @GetMapping("/category/{categoryId}")
    public String showQuizzes(@PathVariable Long categoryId, Authentication authentication, Model model) {
        Category category = quizService.getCategoryById(categoryId).orElse(null);
        if (category == null) {
            return "redirect:/quiz/categories";
        }

        User user = userService.findByUsername(authentication.getName());
        List<Quiz> quizzes = quizService.getQuizzesByCategory(categoryId);

        // Check which quizzes the user has already attempted
        Map<Long, Boolean> attemptedMap = new HashMap<>();
        Map<Long, Result> resultMap = new HashMap<>();
        for (Quiz quiz : quizzes) {
            boolean attempted = resultService.hasUserAttemptedQuiz(user.getId(), quiz.getId());
            attemptedMap.put(quiz.getId(), attempted);
            if (attempted) {
                resultMap.put(quiz.getId(), resultService.getUserResult(user.getId(), quiz.getId()));
            }
        }

        model.addAttribute("category", category);
        model.addAttribute("quizzes", quizzes);
        model.addAttribute("attemptedMap", attemptedMap);
        model.addAttribute("resultMap", resultMap);
        return "quiz/quiz-list";
    }

    /**
     * Start a quiz attempt. Shows questions with timer.
     */
    @GetMapping("/attempt/{quizId}")
    public String attemptQuiz(@PathVariable Long quizId, Authentication authentication,
                              Model model, RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(authentication.getName());

        // Check if already attempted
        if (resultService.hasUserAttemptedQuiz(user.getId(), quizId)) {
            redirectAttributes.addFlashAttribute("error", "You have already attempted this quiz!");
            Quiz quiz = quizService.getQuizById(quizId).orElse(null);
            if (quiz != null) {
                return "redirect:/quiz/category/" + quiz.getCategory().getId();
            }
            return "redirect:/quiz/categories";
        }

        // Get quiz with randomized questions
        Quiz quiz = quizService.getQuizWithRandomizedQuestions(quizId);
        if (quiz == null) {
            return "redirect:/quiz/categories";
        }

        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", quiz.getQuestions());
        model.addAttribute("totalQuestions", quiz.getQuestions().size());
        model.addAttribute("durationSeconds", quiz.getDurationMinutes() * 60);
        return "quiz/attempt";
    }

    /**
     * Submit a quiz attempt and calculate the score.
     */
    @PostMapping("/submit")
    public String submitQuiz(@RequestParam Long quizId,
                             @RequestParam(required = false) Map<String, String> allParams,
                             @RequestParam(defaultValue = "0") int timeTaken,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(authentication.getName());

        // Prevent duplicate submissions
        if (resultService.hasUserAttemptedQuiz(user.getId(), quizId)) {
            redirectAttributes.addFlashAttribute("error", "You have already attempted this quiz!");
            return "redirect:/quiz/categories";
        }

        Quiz quiz = quizService.getQuizById(quizId).orElse(null);
        if (quiz == null) {
            return "redirect:/quiz/categories";
        }

        // Parse answers from form parameters
        // Form sends: answer_<questionId> = optionId (for single select)
        Map<Long, List<Long>> answers = new HashMap<>();
        for (Map.Entry<String, String> entry : allParams.entrySet()) {
            if (entry.getKey().startsWith("answer_")) {
                try {
                    Long questionId = Long.parseLong(entry.getKey().substring(7));
                    Long optionId = Long.parseLong(entry.getValue());
                    answers.computeIfAbsent(questionId, k -> new ArrayList<>()).add(optionId);
                } catch (NumberFormatException ignored) {
                    // Skip invalid parameters
                }
            }
        }

        // Calculate and save result
        Result result = resultService.calculateAndSaveResult(user, quiz, answers, timeTaken);

        return "redirect:/quiz/result/" + result.getId();
    }

    /**
     * Display quiz result with score and correct answers.
     */
    @GetMapping("/result/{resultId}")
    public String showResult(@PathVariable Long resultId, Authentication authentication, Model model) {
        User user = userService.findByUsername(authentication.getName());
        Result result = resultService.getAllResults().stream()
                .filter(r -> r.getId().equals(resultId) && r.getUser().getId().equals(user.getId()))
                .findFirst().orElse(null);

        if (result == null) {
            return "redirect:/dashboard";
        }

        Quiz quiz = result.getQuiz();
        List<Question> questions = quizService.getQuizById(quiz.getId())
                .map(q -> q.getQuestions()).orElse(List.of());

        model.addAttribute("result", result);
        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", questions);
        return "quiz/result";
    }
}
