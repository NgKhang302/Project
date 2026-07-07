package com.eduapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsDTO {
    private long completedLessons;
    private long inProgressLessons;
    private double averageScore;
    /** Completed lesson count grouped by content type (READING/WRITING/LISTENING). */
    private Map<String, Long> completedByContentType;
    private List<UserProgressDTO> recentActivity;
}
