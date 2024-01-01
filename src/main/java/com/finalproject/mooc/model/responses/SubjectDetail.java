package com.finalproject.mooc.model.responses;

import com.finalproject.mooc.enums.TypePremium;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubjectDetail {

    private String subjectCode;
    private String title;
    private String url;
    private String chapter;
    private Integer sequence;
    private TypePremium TypePremium;
}
