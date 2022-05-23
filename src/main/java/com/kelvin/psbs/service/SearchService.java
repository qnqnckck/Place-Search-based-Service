package com.kelvin.psbs.service;

import com.kelvin.psbs.controller.vo.SearchKeywordInfo;

import java.util.List;

public interface SearchService {
    List<SearchKeywordInfo> getSearchKeywordRank();

    void notify(String keyword);
}
