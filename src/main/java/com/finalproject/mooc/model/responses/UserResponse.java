package com.finalproject.mooc.model.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
@Builder
public class UserResponse {
    private String username;
    private String emailAddress;
    private String phoneNumber;
    private String city;
    private String country;
    private String imageUrl;
}
