package com.kelvin.psbs.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class PlaceMetaInfo implements Serializable {

    @ApiModelProperty(example = "이름")
    public String title;
    @ApiModelProperty(example = "도로명")
    public String roadAddress;

    @Override
    public int hashCode() {
        return roadAddress.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PlaceMetaInfo))
            return false;
        PlaceMetaInfo target = (PlaceMetaInfo) o;

        return StringUtils.equals(this.getRoadAddress(), target.getRoadAddress());
    }

}
