package com.kelvin.psbs.service;

import com.kelvin.psbs.common.external.kakao.KakaoExternalClient;
import com.kelvin.psbs.common.external.naver.NaverExternalClient;
import com.kelvin.psbs.controller.vo.PlaceMetaInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceSearchServiceImpl implements PlaceSearchService {

    private final KakaoExternalClient kakaoExternalClient;
    private final NaverExternalClient naverExternalClient;

    private final int LIMITED_MIN_OF_RESULT = 5;

    @Cacheable("place.keyword.search")
    public List<PlaceMetaInfo> search(String keyword) {
        Mono<List<PlaceMetaInfo>> kakaoSearchMono = kakaoExternalClient.search(keyword);
        Mono<List<PlaceMetaInfo>> naverSearchMono = naverExternalClient.search(keyword);

        List<PlaceMetaInfo> data = Mono.zip(kakaoSearchMono, naverSearchMono,
                (kakaoResult, naverResult) -> {

                    log.info("========= kakako");
                    log.info(kakaoResult.toString());

                    log.info("========= naver");
                    log.info(naverResult.toString());

                    /*
                     1. 네이버 주소명의 address depth1(state) 교정
                     2. 네이버 검색 결과의 타이틀 태그 제거
                     */
                    naverResult.stream()
                            .forEach(placeMetaInfo -> {
                                        placeMetaInfo.setTitle(placeMetaInfo.getTitle().replaceAll("\\<.*?>", ""));
                                        placeMetaInfo.setRoadAddress(addressCorrection(placeMetaInfo.getRoadAddress()));
                                    }
                            );
                    /*
                     도로명 기준으로 카운트 횟수 확인
                     */
                    LinkedHashMap<PlaceMetaInfo, Integer> result = new LinkedHashMap<>();
                    mergePlaceMetaInfoListStrategy(kakaoResult, naverResult).stream()
                            .forEach(placeMetaInfo -> result.put(placeMetaInfo, result.getOrDefault(placeMetaInfo, 0) + 1));

                    return result.entrySet().stream()
                            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toList());

                }
        ).block();

        return data;
    }

    /**
     * 장소검색 우선순위는 외부 호출중에는 카카오가 높은 우선권을 가지며, 네이버에서 부족한 검색어를 갯수 보정
     *
     * @param kakaoResult 카카오 키워드 장소 검색 api호출 결과
     * @param naverResult 네이버 키워드 장소 검색 api호출 결과
     * @return 총 결과
     */
    public List<PlaceMetaInfo> mergePlaceMetaInfoListStrategy(List<PlaceMetaInfo> kakaoResult, List<PlaceMetaInfo> naverResult) {
        /*
         네이버 질의의 경우 요청 제한 5개이므로, 질의결과가 적은 경우 kakao 결과를 보정한다.
         */
        int compensationCount = LIMITED_MIN_OF_RESULT - naverResult.size();
        List<PlaceMetaInfo> result = new ArrayList<>();
        result.addAll(kakaoResult.subList(0, Math.min(kakaoResult.size(), LIMITED_MIN_OF_RESULT + compensationCount)));
        result.addAll(naverResult);

        return result;
    }

    public String addressCorrection(String address) {

        String result = address;
        /*
        제주는 '제주특별자치도'로 naver,kakao 동일
         */
        // state 교정
        if (address.contains("특별시")) {
            result = address.replace("특별시", "");
        } else if (address.contains("광역시")) {
            result = address.replace("광역시", "");
        } else if (address.contains("충청북도")) {
            result = address.replace("충청북도", "충북");
        } else if (address.contains("충청남도")) {
            result = address.replace("충청남도", "충남");
        } else if (address.contains("경기도")) {
            result = address.replace("도", "");
        }

        return result;
    }
}
