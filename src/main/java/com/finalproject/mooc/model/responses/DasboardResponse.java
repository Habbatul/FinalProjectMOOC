package com.finalproject.mooc.model.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class DasboardResponse {
    Integer activeUser;
    Integer activeClass;
    Integer premiumClass;
}
