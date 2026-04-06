package com.quizsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a quiz within a category.
 * Each quiz has a title, duration (in minutes), and a set of questions.
 */
@Entity
@Table(name = "quizzes")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Quiz title (e.g., "Arrays & Linked Lists") */
    @NotBlank(message = "Quiz title is required")
    @Column(nullable = false)
    private String title;

    /** Quiz description */
    @Column(length = 500)
    private String description;

    /** Duration in minutes for completing this quiz */
    @Min(value = 1, message = "Duration must be at least 1 minute")
    @Column(nullable = false)
    private int durationMinutes;

    /** Category this quiz belongs to */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /** Questions in this quiz */
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    /** Results for this quiz */
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Result> results = new ArrayList<>();

    // ===== Constructors =====

    public Quiz() {}

    public Quiz(String title, String description, int durationMinutes, Category category) {
        this.title = title;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.category = category;
    }

    // ===== Getters & Setters =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }

    public List<Result> getResults() { return results; }
    public void setResults(List<Result> results) { this.results = results; }
}
