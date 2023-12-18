package com.finalproject.mooc.model.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PaymentStatusResponse {

    private Integer id;

    private String category;

    private String premiumCourse;

    private String status;

    private String paymentMethod;

    private LocalDateTime paymentDate;

}
