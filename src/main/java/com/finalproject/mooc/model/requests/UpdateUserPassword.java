package com.finalproject.mooc.model.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Data
@Getter
@AllArgsConstructor
@Builder
public class UpdateUserPassword {
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;
    @NotBlank
    private String newRePassword;
}
