package com.finalproject.mooc.model.requests;

import lombok.*;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResetPasswordRequest {

    private String resetToken;
    private String newPassword;
    private String newRePassword;
}
