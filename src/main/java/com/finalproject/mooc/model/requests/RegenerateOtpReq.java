package com.finalproject.mooc.model.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
@Builder
public class RegenerateOtpReq {
    String email;
}
