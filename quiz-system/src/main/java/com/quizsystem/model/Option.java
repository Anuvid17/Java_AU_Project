package com.quizsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * Entity representing an option for a question.
 * Each option has text and a flag indicating if it's the correct answer.
 */
@Entity
@Table(name = "options")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The option text */
    @NotBlank(message = "Option text is required")
    @Column(nullable = false, length = 500)
    private String optionText;

    /** Whether this option is a correct answer */
    @Column(nullable = false)
    private boolean correct = false;

    /** Question this option belongs to */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    // ===== Constructors =====

    public Option() {}

    public Option(String optionText, boolean correct, Question question) {
        this.optionText = optionText;
        this.correct = correct;
        this.question = question;
    }

    // ===== Getters & Setters =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOptionText() { return optionText; }
    public void setOptionText(String optionText) { this.optionText = optionText; }

    public boolean isCorrect() { return correct; }
    public void setCorrect(boolean correct) { this.correct = correct; }

    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }
}
