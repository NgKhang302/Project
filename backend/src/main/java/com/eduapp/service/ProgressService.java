package com.eduapp.service;

import com.eduapp.dto.DashboardStatsDTO;
import com.eduapp.dto.UserProgressDTO;
import com.eduapp.model.ProgressStatus;
import com.eduapp.model.UserProgress;
import com.eduapp.repository.UserProgressRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgressService {

    private final UserProgressRepository userProgressRepository;

    public ProgressService(UserProgressRepository userProgressRepository) {
        this.userProgressRepository = userProgressRepository;
    }

    public List<UserProgressDTO> getUserProgress(Long userId) {
        return userProgressRepository.findByUserId(userId).stream().map(this::toDTO).toList();
    }

    public DashboardStatsDTO getDashboardStats(Long userId) {
        List<UserProgress> all = userProgressRepository.findByUserId(userId);

        long completed = all.stream().filter(p -> p.getStatus() == ProgressStatus.COMPLETED).count();
        long inProgress = all.stream().filter(p -> p.getStatus() == ProgressStatus.IN_PROGRESS).count();

        double averageScore = all.stream()
                .filter(p -> p.getStatus() == ProgressStatus.COMPLETED && p.getScore() != null)
                .mapToDouble(UserProgress::getScore)
                .average()
                .orElse(0.0);

        var completedByType = all.stream()
                .filter(p -> p.getStatus() == ProgressStatus.COMPLETED)
                .collect(Collectors.groupingBy(
                        p -> p.getLesson().getContentType().name(),
                        Collectors.counting()
                ));

        List<UserProgressDTO> recent = all.stream()
                .filter(p -> p.getCompletedAt() != null)
                .sorted(Comparator.comparing(UserProgress::getCompletedAt).reversed())
                .limit(5)
                .map(this::toDTO)
                .toList();

        return DashboardStatsDTO.builder()
                .completedLessons(completed)
                .inProgressLessons(inProgress)
                .averageScore(averageScore)
                .completedByContentType(completedByType)
                .recentActivity(recent)
                .build();
    }

    private UserProgressDTO toDTO(UserProgress progress) {
        return UserProgressDTO.builder()
                .id(progress.getId())
                .lessonId(progress.getLesson().getId())
                .lessonTitle(progress.getLesson().getTitle())
                .status(progress.getStatus())
                .score(progress.getScore())
                .completedAt(progress.getCompletedAt())
                .build();
    }
}
