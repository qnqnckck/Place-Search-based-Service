package com.kelvin.psbs.service;

import com.kelvin.psbs.controller.vo.PlaceMetaInfo;

import java.util.List;

public interface PlaceSearchService {
    List<PlaceMetaInfo> search(String keyword);
}

