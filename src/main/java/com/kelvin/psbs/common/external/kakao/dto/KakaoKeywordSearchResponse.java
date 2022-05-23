package com.kelvin.psbs.common.external.kakao.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KakaoKeywordSearchResponse {

    private List<KakaoKeywordSearchDocuments> documents;

}
