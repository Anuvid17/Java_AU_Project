package com.quizsystem.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a quiz attempt result.
 * Tracks the user, quiz attempted, score, total questions, and timestamp.
 */
@Entity
@Table(name = "results")
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** User who attempted the quiz */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Quiz that was attempted */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    /** Number of correct answers */
    @Column(nullable = false)
    private int score;

    /** Total number of questions in the quiz */
    @Column(nullable = false)
    private int totalQuestions;

    /** Timestamp when the quiz was submitted */
    @Column(nullable = false)
    private LocalDateTime attemptDate;

    /** Time taken in seconds */
    @Column
    private int timeTakenSeconds;

    /** Map of questionId -> selectedOptionId */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "result_answers", joinColumns = @JoinColumn(name = "result_id"))
    @MapKeyColumn(name = "question_id")
    @Column(name = "option_id")
    private java.util.Map<Long, Long> selectedOptions = new java.util.HashMap<>();

    // ===== Constructors =====

    public Result() {}

    public Result(User user, Quiz quiz, int score, int totalQuestions, int timeTakenSeconds) {
        this.user = user;
        this.quiz = quiz;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.timeTakenSeconds = timeTakenSeconds;
        this.attemptDate = LocalDateTime.now();
    }

    // ===== Computed Properties =====

    /** Calculate percentage score */
    public double getPercentage() {
        return totalQuestions > 0 ? (score * 100.0) / totalQuestions : 0;
    }

    // ===== Getters & Setters =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Quiz getQuiz() { return quiz; }
    public void setQuiz(Quiz quiz) { this.quiz = quiz; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public LocalDateTime getAttemptDate() { return attemptDate; }
    public void setAttemptDate(LocalDateTime attemptDate) { this.attemptDate = attemptDate; }

    public int getTimeTakenSeconds() { return timeTakenSeconds; }
    public void setTimeTakenSeconds(int timeTakenSeconds) { this.timeTakenSeconds = timeTakenSeconds; }

    public java.util.Map<Long, Long> getSelectedOptions() { return selectedOptions; }
    public void setSelectedOptions(java.util.Map<Long, Long> selectedOptions) { this.selectedOptions = selectedOptions; }
}
