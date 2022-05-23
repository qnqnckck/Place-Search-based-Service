package com.kelvin.psbs.controller;

import com.kelvin.psbs.common.vo.CommonResponse;
import com.kelvin.psbs.controller.vo.SearchKeywordInfo;
import com.kelvin.psbs.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchCoreController {

    private final SearchService searchService;

    /**
     * 장소 검색 API
     *
     * @return 검색 된 장소 타이틀 목록
     */
    @GetMapping("/rank/v1")
    public CommonResponse<List<SearchKeywordInfo>> getPlaceSearchResult() {
        return CommonResponse.of(searchService.getSearchKeywordRank());
    }
}
