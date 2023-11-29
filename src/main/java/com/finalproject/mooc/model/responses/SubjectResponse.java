package com.finalproject.mooc.model.responses;

import com.finalproject.mooc.enums.TypePremium;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SubjectResponse {
    private String subjectCode;
    private String title;
    private String url;
    private Integer chapter;
    private Integer sequence;
    private TypePremium TypePremium;
}
