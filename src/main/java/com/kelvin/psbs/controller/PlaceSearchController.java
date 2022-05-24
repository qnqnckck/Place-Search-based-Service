package com.kelvin.psbs.controller;

import com.kelvin.psbs.common.vo.CommonResponse;
import com.kelvin.psbs.controller.vo.PlaceMetaInfo;
import com.kelvin.psbs.service.PlaceSearchService;
import com.kelvin.psbs.service.SearchService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/place")
@RequiredArgsConstructor
public class PlaceSearchController {

    private final PlaceSearchService placeSearchService;

    private final SearchService searchService;

    /**
     * 장소 검색 API
     *
     * @param keyword 장소 검색을 위한 키워드
     * @return 검색 된 장소 타이틀 목록
     */

    @GetMapping("/search/v1")
    @ApiImplicitParam(name="keyword", value="키워드")
    @ApiOperation(value="장소 키워드 검색", notes = "키워드를 통해 장소를 검색하여 이름 및 도로명을 조회한다.")
    public CommonResponse<List<PlaceMetaInfo>> getPlaceSearchResult(@RequestParam String keyword) {
        if (!StringUtils.hasText(keyword)) {
            throw new IllegalArgumentException("'keyword' is required");
        }

        searchService.notify(keyword);
        return CommonResponse.of(placeSearchService.search(keyword));
    }
}
