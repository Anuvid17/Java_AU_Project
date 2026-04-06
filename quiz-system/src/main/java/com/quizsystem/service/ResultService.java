package com.quizsystem.service;

import com.quizsystem.model.*;
import com.quizsystem.repository.OptionRepository;
import com.quizsystem.repository.QuestionRepository;
import com.quizsystem.repository.ResultRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Service for calculating quiz scores, storing results,
 * and generating leaderboard data.
 */
@Service
public class ResultService {

    private final ResultRepository resultRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;

    public ResultService(ResultRepository resultRepository, QuestionRepository questionRepository,
                         OptionRepository optionRepository) {
        this.resultRepository = resultRepository;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
    }

    /**
     * Calculate score and save the result for a quiz attempt.
     * Compares selected options against correct options for each question.
     *
     * @param user The user who attempted the quiz
     * @param quiz The quiz that was attempted
     * @param answers Map of questionId -> list of selected optionIds
     * @param timeTakenSeconds Time taken to complete the quiz
     * @return The saved Result entity
     */
    @Transactional
    public Result calculateAndSaveResult(User user, Quiz quiz, Map<Long, List<Long>> answers, int timeTakenSeconds) {
        List<Question> questions = questionRepository.findByQuizId(quiz.getId());
        int score = 0;

        Map<Long, Long> selectedOptionsMap = new java.util.HashMap<>();
        for (Question question : questions) {
            List<Option> correctOptions = optionRepository.findByQuestionIdAndCorrectTrue(question.getId());
            List<Long> selectedOptionIds = answers.getOrDefault(question.getId(), List.of());

            if (!selectedOptionIds.isEmpty()) {
                selectedOptionsMap.put(question.getId(), selectedOptionIds.get(0));
            }

            // Get correct option IDs
            List<Long> correctOptionIds = correctOptions.stream()
                    .map(Option::getId)
                    .sorted()
                    .toList();

            // Sort selected for comparison
            List<Long> sortedSelected = selectedOptionIds.stream()
                    .sorted()
                    .toList();

            // Question is correct only if ALL correct options are selected and NO wrong ones
            if (correctOptionIds.equals(sortedSelected)) {
                score++;
            }
        }

        Result result = new Result(user, quiz, score, questions.size(), timeTakenSeconds);
        result.setSelectedOptions(selectedOptionsMap);
        return resultRepository.save(result);
    }

    /**
     * Check if a user has already attempted a specific quiz.
     */
    public boolean hasUserAttemptedQuiz(Long userId, Long quizId) {
        return resultRepository.existsByUserIdAndQuizId(userId, quizId);
    }

    /**
     * Get a user's result for a specific quiz.
     */
    public Result getUserResult(Long userId, Long quizId) {
        return resultRepository.findByUserIdAndQuizId(userId, quizId).orElse(null);
    }

    /**
     * Get all results for a user (attempt history).
     */
    public List<Result> getUserResults(Long userId) {
        return resultRepository.findByUserIdOrderByAttemptDateDesc(userId);
    }

    /**
     * Get leaderboard for a specific category.
     */
    public List<Result> getLeaderboard(Long categoryId) {
        return resultRepository.findTopScoresByCategoryId(categoryId);
    }

    /**
     * Get all results (admin view).
     */
    public List<Result> getAllResults() {
        return resultRepository.findAllByOrderByAttemptDateDesc();
    }

    /**
     * Count total quiz attempts.
     */
    public long countAttempts() {
        return resultRepository.count();
    }
}
