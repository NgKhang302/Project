package com.eduapp.service;

import com.eduapp.dto.DialogueLineRequest;
import com.eduapp.dto.DialogueLineResponse;
import com.eduapp.exception.ResourceNotFoundException;
import com.eduapp.model.DialogueLine;
import com.eduapp.model.Lesson;
import com.eduapp.repository.DialogueLineRepository;
import com.eduapp.repository.LessonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DialogueLineService {

    private final DialogueLineRepository dialogueLineRepository;
    private final LessonRepository lessonRepository;

    public DialogueLineService(DialogueLineRepository dialogueLineRepository, LessonRepository lessonRepository) {
        this.dialogueLineRepository = dialogueLineRepository;
        this.lessonRepository = lessonRepository;
    }

    public List<DialogueLineResponse> getByLesson(Long lessonId) {
        return dialogueLineRepository.findByLessonIdOrderByOrderIndexAsc(lessonId)
                .stream().map(this::toResponse).toList();
    }

    @Transactional
    public List<DialogueLineResponse> replaceAll(Long lessonId, List<DialogueLineRequest> lines) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found: " + lessonId));

        dialogueLineRepository.deleteByLessonId(lessonId);

        List<DialogueLine> saved = dialogueLineRepository.saveAll(
                java.util.stream.IntStream.range(0, lines.size())
                        .mapToObj(i -> DialogueLine.builder()
                                .lesson(lesson)
                                .speaker(lines.get(i).getSpeaker())
                                .text(lines.get(i).getText())
                                .orderIndex(i)
                                .build())
                        .toList()
        );

        return saved.stream()
                .sorted((a, b) -> Integer.compare(a.getOrderIndex(), b.getOrderIndex()))
                .map(this::toResponse)
                .toList();
    }

    private DialogueLineResponse toResponse(DialogueLine line) {
        return DialogueLineResponse.builder()
                .id(line.getId())
                .speaker(line.getSpeaker())
                .text(line.getText())
                .orderIndex(line.getOrderIndex())
                .build();
    }
}
