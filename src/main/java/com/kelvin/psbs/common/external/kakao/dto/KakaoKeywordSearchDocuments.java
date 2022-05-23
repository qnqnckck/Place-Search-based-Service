package com.kelvin.psbs.common.external.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoKeywordSearchDocuments {
    @JsonProperty("place_name")
    private String placeName;

    @JsonProperty("road_address_name")
    private String roadAddressName;
}
