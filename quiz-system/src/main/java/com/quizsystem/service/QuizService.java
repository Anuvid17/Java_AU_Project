package com.quizsystem.service;

import com.quizsystem.model.Category;
import com.quizsystem.model.Quiz;
import com.quizsystem.model.Question;
import com.quizsystem.repository.CategoryRepository;
import com.quizsystem.repository.QuizRepository;
import com.quizsystem.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service handling quiz and category business logic.
 * Provides quiz retrieval with randomized questions and options.
 */
@Service
public class QuizService {

    private final CategoryRepository categoryRepository;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;

    public QuizService(CategoryRepository categoryRepository, QuizRepository quizRepository,
                       QuestionRepository questionRepository) {
        this.categoryRepository = categoryRepository;
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
    }

    // ===== Category Operations =====

    /** Get all categories */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /** Find category by ID */
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    /** Save a category */
    @Transactional
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    /** Delete a category by ID */
    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    /** Count categories */
    public long countCategories() {
        return categoryRepository.count();
    }

    // ===== Quiz Operations =====

    /** Get all quizzes for a category */
    public List<Quiz> getQuizzesByCategory(Long categoryId) {
        return quizRepository.findByCategoryId(categoryId);
    }

    /** Find quiz by ID */
    public Optional<Quiz> getQuizById(Long id) {
        return quizRepository.findById(id);
    }

    /** Get all quizzes */
    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    /** Save a quiz */
    @Transactional
    public Quiz saveQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    /** Delete a quiz by ID */
    @Transactional
    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }

    /** Count total quizzes */
    public long countQuizzes() {
        return quizRepository.count();
    }

    /**
     * Get quiz with randomized questions and options.
     * Creates a shuffled copy of questions and their options for each attempt.
     */
    public Quiz getQuizWithRandomizedQuestions(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElse(null);
        if (quiz != null) {
            List<Question> questions = questionRepository.findByQuizId(quizId);
            // Randomize question order
            Collections.shuffle(questions);
            // Randomize options within each question
            for (Question q : questions) {
                Collections.shuffle(q.getOptions());
            }
            quiz.setQuestions(questions);
        }
        return quiz;
    }

    /** Get question count for a quiz */
    public long getQuestionCount(Long quizId) {
        return questionRepository.countByQuizId(quizId);
    }
}
