package com.kelvin.psbs.common.external.naver.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NaverKeywordSearchResponse {
    private List<NaverKeywordSearchItems> items;
}
