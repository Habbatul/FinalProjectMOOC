package com.finalproject.mooc.model.requests;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
@AllArgsConstructor
@Builder
public class UpdateUserRequest {
    private String emailAddress;
    private String username;
    private String phoneNumber;
    private String city;
    private String country;
    private MultipartFile file;
}
