package com.eduapp.repository;

import com.eduapp.model.ProgressStatus;
import com.eduapp.model.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {

    List<UserProgress> findByUserId(Long userId);

    List<UserProgress> findByUserIdAndStatus(Long userId, ProgressStatus status);

    Optional<UserProgress> findByUserIdAndLessonId(Long userId, Long lessonId);

    long countByUserIdAndStatus(Long userId, ProgressStatus status);
}
