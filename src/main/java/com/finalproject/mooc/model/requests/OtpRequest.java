package com.finalproject.mooc.model.requests;

import lombok.*;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpRequest {

    String email;
    String Otp;
}
