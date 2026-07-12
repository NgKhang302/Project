package com.eduapp.repository;

import com.eduapp.model.DialogueLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DialogueLineRepository extends JpaRepository<DialogueLine, Long> {

    List<DialogueLine> findByLessonIdOrderByOrderIndexAsc(Long lessonId);

    void deleteByLessonId(Long lessonId);
}
