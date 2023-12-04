package com.finalproject.mooc.model.responses;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


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
