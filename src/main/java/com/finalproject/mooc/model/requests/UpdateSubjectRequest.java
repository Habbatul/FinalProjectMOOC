package com.finalproject.mooc.model.requests;

import com.finalproject.mooc.enums.TypePremium;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
@Builder
public class UpdateSubjectRequest {
    private String title;
    private String url;
    private String chapter;
    private Integer sequence;
    private TypePremium typePremium;
}
