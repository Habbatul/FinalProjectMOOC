package com.finalproject.mooc.model.requests;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserPassword {
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;
    @NotBlank
    private String newRePassword;
}
