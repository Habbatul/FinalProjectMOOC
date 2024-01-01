package com.finalproject.mooc.model.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentStatusPaginationResponse {

    private List<PaymentStatusResponse> paymentStatusResponses;
    private Integer productCurrentPage;
    private Integer productTotalPage;

}
