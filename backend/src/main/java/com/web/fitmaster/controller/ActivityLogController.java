package com.web.fitmaster.controller;

import com.web.fitmaster.dto.ActivityLogDTOs;
import com.web.fitmaster.model.enums.EntityType;
import com.web.fitmaster.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/logs")
public class ActivityLogController {
    private final ActivityLogService activityLogService;


    @GetMapping("")
    public ResponseEntity<ActivityLogDTOs.LogsResponse>getLogs(@RequestParam(required = false) Long performedBy, @RequestParam(required = false) EntityType entityType, Pageable pageable) {
        return new ResponseEntity<>(activityLogService.getLogs(performedBy,entityType,pageable), HttpStatus.OK);
    }
}
