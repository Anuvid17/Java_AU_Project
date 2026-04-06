package com.quizsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a question in a quiz.
 * Each question has question text, a flag for multiple correct answers,
 * and a list of options.
 */
@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The question text */
    @NotBlank(message = "Question text is required")
    @Column(nullable = false, length = 1000)
    private String questionText;

    /** Whether this question allows multiple correct answers */
    @Column(nullable = false)
    private boolean multipleCorrect = false;

    /** Quiz this question belongs to */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    /** Options for this question */
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options = new ArrayList<>();

    // ===== Constructors =====

    public Question() {}

    public Question(String questionText, boolean multipleCorrect, Quiz quiz) {
        this.questionText = questionText;
        this.multipleCorrect = multipleCorrect;
        this.quiz = quiz;
    }

    // ===== Getters & Setters =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public boolean isMultipleCorrect() { return multipleCorrect; }
    public void setMultipleCorrect(boolean multipleCorrect) { this.multipleCorrect = multipleCorrect; }

    public Quiz getQuiz() { return quiz; }
    public void setQuiz(Quiz quiz) { this.quiz = quiz; }

    public List<Option> getOptions() { return options; }
    public void setOptions(List<Option> options) { this.options = options; }
}
