package com.kelvin.psbs.service;

import com.kelvin.psbs.controller.vo.SearchKeywordInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SearchServiceImplTest {

    @Autowired
    private SearchServiceImpl searchServiceImpl;

    @Test
    @DisplayName("실시간 검색어 랭킹 업데이트 및 조회 - 10개 이하 ")
    public void rankingTest1() throws Exception {
        //given
        for (int i = 0; i < 5; i++) {
            searchServiceImpl.notify("keyword" + i);
        }

        //then
        List<SearchKeywordInfo> searchKeywordRank = searchServiceImpl.getSearchKeywordRank();

        int i = 5;
        for (SearchKeywordInfo searchKeywordInfo : searchKeywordRank) {
            Assertions.assertThat(searchKeywordInfo.getKeyword()).isEqualTo("keyword" + i);
            Assertions.assertThat(searchKeywordInfo.getCount()).isEqualTo(i);
            i--;
        }
    }

    @Test
    @DisplayName("실시간 검색어 랭킹 업데이트 및 조회 - 10개 이상 순위 변경시 ")
    public void rankingTest2() throws Exception {
        //given
        for (int i = 0; i < 10; i++) {
            searchServiceImpl.notify("keyword" + i);
        }

        //when

        //then
        List<SearchKeywordInfo> searchKeywordRank = searchServiceImpl.getSearchKeywordRank();
        Assertions.assertThat(searchKeywordRank.get(9).getKeyword()).isEqualTo("keyword1");

        // add new keyword
        searchServiceImpl.notify("keyword11");
        searchServiceImpl.notify("keyword11");

        searchKeywordRank = searchServiceImpl.getSearchKeywordRank();
        Assertions.assertThat(searchKeywordRank.get(9).getKeyword()).isNotEqualTo("keyword1");
    }
}