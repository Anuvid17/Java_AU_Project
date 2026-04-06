package com.quizsystem.repository;

import com.quizsystem.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository for Option entity operations.
 */
@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

    /** Find all options for a specific question */
    List<Option> findByQuestionId(Long questionId);

    /** Find all correct options for a question */
    List<Option> findByQuestionIdAndCorrectTrue(Long questionId);
}
