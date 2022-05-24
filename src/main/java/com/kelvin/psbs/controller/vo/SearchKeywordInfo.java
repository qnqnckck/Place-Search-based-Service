package com.kelvin.psbs.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class SearchKeywordInfo {
    @ApiModelProperty(example = "키워드")
    String keyword;
    @ApiModelProperty(example = "호출 수")
    double count;
}
