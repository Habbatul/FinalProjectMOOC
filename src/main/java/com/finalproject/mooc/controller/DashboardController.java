package com.finalproject.mooc.controller;

import com.finalproject.mooc.model.responses.WebResponse;
import com.finalproject.mooc.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {
    @Autowired
    DashboardService dashboardService;
    @Operation(summary = "Menampilkan data dashboard")
    @PutMapping("/dashboard-data")
    ResponseEntity<WebResponse<String>> showDashboardData(){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        dashboardService.cekhasilQuery(username);

        return ResponseEntity.ok(WebResponse.<String>builder()
                .data("fungsinya dikasih disini")
                .build());
    }
}
