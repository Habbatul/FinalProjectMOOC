package com.finalproject.mooc.model.requests;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest {
    private String emailAddress;
    private String username;
    private String phoneNumber;
    private String city;
    private String country;
    private MultipartFile file;
}
