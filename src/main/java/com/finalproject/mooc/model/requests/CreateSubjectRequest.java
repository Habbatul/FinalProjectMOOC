package com.finalproject.mooc.model.requests;


import com.finalproject.mooc.enums.TypePremium;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
@Builder
public class CreateSubjectRequest {
    private String title;
    private String url;
    private Integer chapter;
    private Integer sequence;
    private TypePremium TypePremium;
}
