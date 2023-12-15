package com.finalproject.mooc.controller;

import com.finalproject.mooc.model.responses.DasboardResponse;
import com.finalproject.mooc.model.responses.WebResponse;
import com.finalproject.mooc.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {
    @Autowired
    DashboardService dashboardService;
    @Operation(summary = "Menampilkan data dashboard")
    @GetMapping("/dashboard-data")
    ResponseEntity<WebResponse<DasboardResponse>> showDashboardData(){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(WebResponse.<DasboardResponse>builder()
                .data(dashboardService.cekhasilQuery(username))
                .build());
    }
}
