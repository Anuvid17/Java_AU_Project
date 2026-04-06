package com.quizsystem.repository;

import com.quizsystem.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository for Question entity operations.
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    /** Find all questions for a specific quiz */
    List<Question> findByQuizId(Long quizId);

    /** Count questions in a quiz */
    long countByQuizId(Long quizId);
}
