package com.finalproject.mooc.model.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
@Builder
public class ResetPasswordRequest {
    private String resetToken;
    private String newPassword;
    private String newRePassword;
}
