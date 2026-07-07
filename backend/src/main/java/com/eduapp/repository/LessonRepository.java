package com.eduapp.repository;

import com.eduapp.model.ContentType;
import com.eduapp.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findByCategoryId(Long categoryId);

    List<Lesson> findByCategoryIdAndContentType(Long categoryId, ContentType contentType);

    List<Lesson> findByContentType(ContentType contentType);

    Optional<Lesson> findBySlug(String slug);

    boolean existsBySlug(String slug);

    List<Lesson> findByTitleContainingIgnoreCase(String title);
}
