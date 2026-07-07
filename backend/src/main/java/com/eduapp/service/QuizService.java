package com.eduapp.service;

import com.eduapp.dto.QuizAnswerRequest;
import com.eduapp.dto.QuizAnswerResponse;
import com.eduapp.dto.QuizRequest;
import com.eduapp.dto.QuizResponse;
import com.eduapp.exception.ResourceNotFoundException;
import com.eduapp.exception.UnauthorizedException;
import com.eduapp.model.Lesson;
import com.eduapp.model.Quiz;
import com.eduapp.model.User;
import com.eduapp.model.UserQuizResult;
import com.eduapp.repository.LessonRepository;
import com.eduapp.repository.QuizRepository;
import com.eduapp.repository.UserQuizResultRepository;
import com.eduapp.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final LessonRepository lessonRepository;
    private final UserQuizResultRepository userQuizResultRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public QuizService(QuizRepository quizRepository,
                        LessonRepository lessonRepository,
                        UserQuizResultRepository userQuizResultRepository,
                        UserRepository userRepository) {
        this.quizRepository = quizRepository;
        this.lessonRepository = lessonRepository;
        this.userQuizResultRepository = userQuizResultRepository;
        this.userRepository = userRepository;
    }

    public List<QuizResponse> getByLesson(Long lessonId, boolean includeAnswer) {
        return quizRepository.findByLessonId(lessonId).stream()
                .map(quiz -> toResponse(quiz, includeAnswer))
                .toList();
    }

    public QuizResponse create(QuizRequest request) {
        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found: " + request.getLessonId()));

        Quiz quiz = Quiz.builder()
                .lesson(lesson)
                .question(request.getQuestion())
                .options(writeOptions(request.getOptions()))
                .correctAnswer(request.getCorrectAnswer())
                .explanation(request.getExplanation())
                .build();

        return toResponse(quizRepository.save(quiz), true);
    }

    public QuizResponse update(Long id, QuizRequest request) {
        Quiz quiz = findById(id);
        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found: " + request.getLessonId()));

        quiz.setLesson(lesson);
        quiz.setQuestion(request.getQuestion());
        quiz.setOptions(writeOptions(request.getOptions()));
        quiz.setCorrectAnswer(request.getCorrectAnswer());
        quiz.setExplanation(request.getExplanation());

        return toResponse(quizRepository.save(quiz), true);
    }

    public void delete(Long id) {
        quizRepository.delete(findById(id));
    }

    public QuizAnswerResponse submitAnswer(Long userId, QuizAnswerRequest request) {
        Quiz quiz = findById(request.getQuizId());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        boolean correct = quiz.getCorrectAnswer().trim().equalsIgnoreCase(request.getAnswer().trim());

        UserQuizResult result = UserQuizResult.builder()
                .user(user)
                .quiz(quiz)
                .userAnswer(request.getAnswer())
                .correct(correct)
                .build();
        userQuizResultRepository.save(result);

        return QuizAnswerResponse.builder()
                .correct(correct)
                .correctAnswer(quiz.getCorrectAnswer())
                .explanation(quiz.getExplanation())
                .build();
    }

    private Quiz findById(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found: " + id));
    }

    private String writeOptions(List<String> options) {
        try {
            return objectMapper.writeValueAsString(options);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid quiz options");
        }
    }

    private List<String> readOptions(String options) {
        try {
            return objectMapper.readValue(options, objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
        } catch (JsonProcessingException e) {
            return List.of();
        }
    }

    private QuizResponse toResponse(Quiz quiz, boolean includeAnswer) {
        return QuizResponse.builder()
                .id(quiz.getId())
                .lessonId(quiz.getLesson().getId())
                .question(quiz.getQuestion())
                .options(readOptions(quiz.getOptions()))
                .correctAnswer(includeAnswer ? quiz.getCorrectAnswer() : null)
                .explanation(includeAnswer ? quiz.getExplanation() : null)
                .build();
    }
}
