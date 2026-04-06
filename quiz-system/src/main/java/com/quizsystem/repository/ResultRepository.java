package com.quizsystem.repository;

import com.quizsystem.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Result entity operations.
 * Provides queries for leaderboards, attempt history, and duplicate prevention.
 */
@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

    /** Find all results for a specific user */
    List<Result> findByUserIdOrderByAttemptDateDesc(Long userId);

    /** Find all results for a specific quiz, ordered by score descending (leaderboard) */
    List<Result> findByQuizIdOrderByScoreDescTimeTakenSecondsAsc(Long quizId);

    /** Check if a user has already attempted a specific quiz */
    boolean existsByUserIdAndQuizId(Long userId, Long quizId);

    /** Find a user's result for a specific quiz */
    Optional<Result> findByUserIdAndQuizId(Long userId, Long quizId);

    /** Leaderboard: top scores for quizzes in a category */
    @Query("SELECT r FROM Result r JOIN r.quiz q WHERE q.category.id = :categoryId ORDER BY r.score DESC, r.timeTakenSeconds ASC")
    List<Result> findTopScoresByCategoryId(@Param("categoryId") Long categoryId);

    /** All results ordered by date for admin view */
    List<Result> findAllByOrderByAttemptDateDesc();

    /** Count total attempts */
    long count();
}
