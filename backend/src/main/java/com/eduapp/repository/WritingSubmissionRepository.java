package com.eduapp.repository;

import com.eduapp.model.WritingSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WritingSubmissionRepository extends JpaRepository<WritingSubmission, Long> {

    List<WritingSubmission> findByUserIdAndLessonIdOrderBySubmittedAtDesc(Long userId, Long lessonId);

    List<WritingSubmission> findByUserIdOrderBySubmittedAtDesc(Long userId);
}
