package com.quizsystem.repository;

import com.quizsystem.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository for Quiz entity operations.
 */
@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    /** Find all quizzes in a specific category */
    List<Quiz> findByCategoryId(Long categoryId);

    /** Count quizzes in a category */
    long countByCategoryId(Long categoryId);
}
