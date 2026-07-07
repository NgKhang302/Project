package com.eduapp.controller;

import com.eduapp.dto.DashboardStatsDTO;
import com.eduapp.dto.UserDTO;
import com.eduapp.dto.UserProgressDTO;
import com.eduapp.service.ProgressService;
import com.eduapp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final ProgressService progressService;

    public UserController(UserService userService, ProgressService progressService) {
        this.userService = userService;
        this.progressService = progressService;
    }

    @GetMapping("/profile")
    public UserDTO getProfile(HttpServletRequest request) {
        return userService.getProfile(currentUsername(request));
    }

    @PutMapping("/profile")
    public UserDTO updateProfile(HttpServletRequest request, @RequestBody Map<String, String> body) {
        return userService.updateProfile(currentUsername(request), body.get("email"));
    }

    @GetMapping("/progress")
    public List<UserProgressDTO> getProgress(HttpServletRequest request) {
        return progressService.getUserProgress(currentUserId(request));
    }

    @GetMapping("/dashboard")
    public DashboardStatsDTO getDashboard(HttpServletRequest request) {
        return progressService.getDashboardStats(currentUserId(request));
    }

    private String currentUsername(HttpServletRequest request) {
        return (String) request.getAttribute("username");
    }

    private Long currentUserId(HttpServletRequest request) {
        return (Long) request.getAttribute("userId");
    }
}
