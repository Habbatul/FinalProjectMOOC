package com.finalproject.mooc.model.requests;


import com.finalproject.mooc.enums.TypePremium;
import lombok.*;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateSubjectRequest {
    private String title;
    private String url;
    private String chapter;
    private Integer sequence;
    private TypePremium TypePremium;
}
