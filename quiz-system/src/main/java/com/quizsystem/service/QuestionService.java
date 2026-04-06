package com.quizsystem.service;

import com.quizsystem.model.Option;
import com.quizsystem.model.Question;
import com.quizsystem.model.Quiz;
import com.quizsystem.repository.OptionRepository;
import com.quizsystem.repository.QuestionRepository;
import com.quizsystem.repository.QuizRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing questions and their options.
 * Used primarily by the admin panel for CRUD operations.
 */
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final QuizRepository quizRepository;

    public QuestionService(QuestionRepository questionRepository, OptionRepository optionRepository,
                           QuizRepository quizRepository) {
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
        this.quizRepository = quizRepository;
    }

    /** Get all questions for a quiz */
    public List<Question> getQuestionsByQuizId(Long quizId) {
        return questionRepository.findByQuizId(quizId);
    }

    /** Find question by ID */
    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    /** Get all questions */
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    /** Save a question */
    @Transactional
    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }

    /** Delete a question by ID */
    @Transactional
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }

    /** Count total questions */
    public long countQuestions() {
        return questionRepository.count();
    }

    /** Get options for a question */
    public List<Option> getOptionsByQuestionId(Long questionId) {
        return optionRepository.findByQuestionId(questionId);
    }

    /** Get correct options for a question */
    public List<Option> getCorrectOptions(Long questionId) {
        return optionRepository.findByQuestionIdAndCorrectTrue(questionId);
    }

    /** Save an option */
    @Transactional
    public Option saveOption(Option option) {
        return optionRepository.save(option);
    }

    /** Delete an option */
    @Transactional
    public void deleteOption(Long id) {
        optionRepository.deleteById(id);
    }

    /**
     * Add a complete question with options to a quiz.
     * @param quizId The quiz to add the question to
     * @param questionText The question text
     * @param options Array of option texts
     * @param correctIndices Array of indices (0-based) indicating correct options
     */
    @Transactional
    public Question addQuestionWithOptions(Long quizId, String questionText, String[] options, int[] correctIndices) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() ->
                new RuntimeException("Quiz not found with id: " + quizId));

        Question question = new Question();
        question.setQuestionText(questionText);
        question.setMultipleCorrect(correctIndices.length > 1);
        question.setQuiz(quiz);
        question = questionRepository.save(question);

        for (int i = 0; i < options.length; i++) {
            Option option = new Option();
            option.setOptionText(options[i]);
            option.setCorrect(contains(correctIndices, i));
            option.setQuestion(question);
            optionRepository.save(option);
        }

        return question;
    }

    /** Helper: check if array contains value */
    private boolean contains(int[] arr, int val) {
        for (int i : arr) {
            if (i == val) return true;
        }
        return false;
    }
}
