package com.finalproject.mooc.model.responses;

import lombok.*;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String username;
    private String emailAddress;
    private String phoneNumber;
    private String city;
    private String country;
    private String imageUrl;
}
