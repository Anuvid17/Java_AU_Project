package com.quizsystem.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Transfer Object for submitting a quiz attempt.
 * Maps question IDs to selected option IDs.
 */
public class QuizAttemptDTO {

    /** Quiz ID being attempted */
    private Long quizId;

    /** Map of questionId -> list of selected optionIds */
    private Map<Long, List<Long>> answers = new HashMap<>();

    /** Time taken in seconds */
    private int timeTakenSeconds;

    // ===== Getters & Setters =====

    public Long getQuizId() { return quizId; }
    public void setQuizId(Long quizId) { this.quizId = quizId; }

    public Map<Long, List<Long>> getAnswers() { return answers; }
    public void setAnswers(Map<Long, List<Long>> answers) { this.answers = answers; }

    public int getTimeTakenSeconds() { return timeTakenSeconds; }
    public void setTimeTakenSeconds(int timeTakenSeconds) { this.timeTakenSeconds = timeTakenSeconds; }
}
