package com.quizsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a quiz category (e.g., DSA, Java, Python, ML).
 * Each category can contain multiple quizzes.
 */
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Category name (e.g., "Data Structures & Algorithms") */
    @NotBlank(message = "Category name is required")
    @Column(unique = true, nullable = false)
    private String name;

    /** Brief description of the category */
    @Column(length = 500)
    private String description;

    /** Icon class for UI display (e.g., Bootstrap icon name) */
    @Column(length = 100)
    private String icon;

    /** Color theme for UI cards */
    @Column(length = 20)
    private String color;

    /** Quizzes belonging to this category */
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quiz> quizzes = new ArrayList<>();

    // ===== Constructors =====

    public Category() {}

    public Category(String name, String description, String icon, String color) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.color = color;
    }

    // ===== Getters & Setters =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public List<Quiz> getQuizzes() { return quizzes; }
    public void setQuizzes(List<Quiz> quizzes) { this.quizzes = quizzes; }
}
