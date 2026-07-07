package com.eduapp.repository;

import com.eduapp.model.UserQuizResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserQuizResultRepository extends JpaRepository<UserQuizResult, Long> {

    List<UserQuizResult> findByUserId(Long userId);

    List<UserQuizResult> findByUserIdAndQuizLessonId(Long userId, Long lessonId);

    long countByUserIdAndCorrect(Long userId, boolean correct);
}
