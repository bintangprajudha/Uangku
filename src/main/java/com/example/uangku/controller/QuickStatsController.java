package com.example.uangku.controller;

import com.example.uangku.dto.CategoryStatsDTO;
import com.example.uangku.dto.QuickStatsDTO;
import com.example.uangku.service.QuickStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/quick-stats")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class QuickStatsController {
    
    private final QuickStatsService quickStatsService;
    
    @GetMapping
    public ResponseEntity<QuickStatsDTO> getQuickStats(HttpSession session) {
        com.example.uangku.model.User user = (com.example.uangku.model.User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        
        QuickStatsDTO stats = quickStatsService.getQuickStats(user.getId());
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryStatsDTO>> getCategoryBreakdown(HttpSession session) {
        com.example.uangku.model.User user = (com.example.uangku.model.User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        
        List<CategoryStatsDTO> breakdown = quickStatsService.getCategoryBreakdown(user.getId());
        return ResponseEntity.ok(breakdown);
    }
}