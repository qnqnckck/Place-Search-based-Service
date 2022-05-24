package com.kelvin.psbs.controller;

import com.kelvin.psbs.common.vo.CommonResponse;
import com.kelvin.psbs.controller.vo.SearchKeywordInfo;
import com.kelvin.psbs.service.SearchService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
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
     *  검색어 랭킹 Top10 조회 API
     *
     * @return 검색어 랭킹 top 10
     */
    @GetMapping("/rank/v1")
    @ApiOperation(value="검색어 실시간랭킹 Top10", notes = "실시간으로 검색어로 사용된 top10의 랭킹을 제공한다.")
    public CommonResponse<List<SearchKeywordInfo>> searchKeywordTop10() {
        return CommonResponse.of(searchService.getSearchKeywordRank());
    }
}
