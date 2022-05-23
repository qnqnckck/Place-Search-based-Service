package com.kelvin.psbs.service;

import com.kelvin.psbs.controller.vo.PlaceMetaInfo;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class PlaceSearchServiceImplTest {

    @Autowired
    private PlaceSearchServiceImpl placeSearchServiceImpl;

    @Test
    @DisplayName("네이버 검색 결과가 적은 경우 카카오에서 결과 수로 보정 - 카카오10개/네이버5개 -> 카카오5개/네이버5개")
    public void compensation1() throws Exception {
        //given
        List<PlaceMetaInfo> kakaoResult = new ArrayList<>();
        kakaoResult.add(new PlaceMetaInfo("kakaoA", "A"));
        kakaoResult.add(new PlaceMetaInfo("kakaoB", "B"));
        kakaoResult.add(new PlaceMetaInfo("kakaoC", "C"));
        kakaoResult.add(new PlaceMetaInfo("kakaoD", "D"));
        kakaoResult.add(new PlaceMetaInfo("kakaoE", "E"));
        kakaoResult.add(new PlaceMetaInfo("kakaoF", "A"));
        kakaoResult.add(new PlaceMetaInfo("kakaoG", "B"));
        kakaoResult.add(new PlaceMetaInfo("kakaoH", "C"));
        kakaoResult.add(new PlaceMetaInfo("kakaoI", "D"));
        kakaoResult.add(new PlaceMetaInfo("kakaoJ", "E"));

        List<PlaceMetaInfo> naverResult = new ArrayList<>();
        naverResult.add(new PlaceMetaInfo("naverA", "C"));
        naverResult.add(new PlaceMetaInfo("naverB", "F"));
        naverResult.add(new PlaceMetaInfo("naverC", "E"));
        naverResult.add(new PlaceMetaInfo("naverD", "G"));
        naverResult.add(new PlaceMetaInfo("naverE", "h"));

        //when
        List<PlaceMetaInfo> placeMetaInfos =  placeSearchServiceImpl.mergePlaceMetaInfoListStrategy(kakaoResult, naverResult);
        //then
        Assertions.assertThat(placeMetaInfos).hasSize(10);

        int count =0 ;
        for(PlaceMetaInfo placeMetaInfo : placeMetaInfos){
            if (StringUtils.contains(placeMetaInfo.getTitle(), "naver")) {
                count++;
            }
        }
        Assertions.assertThat(count).isEqualTo(5);
    }

    @Test
    @DisplayName("네이버 검색 결과가 적은 경우 카카오에서 결과 수로 보정 -카카오3개/네이버5개 -> 카카오3개/네이버5개")
    public void compensation2() throws Exception {
        //given
        List<PlaceMetaInfo> kakaoResult = new ArrayList<>();
        kakaoResult.add(new PlaceMetaInfo("kakaoA", "A"));
        kakaoResult.add(new PlaceMetaInfo("kakaoB", "B"));
        kakaoResult.add(new PlaceMetaInfo("kakaoC", "C"));

        List<PlaceMetaInfo> naverResult = new ArrayList<>();
        naverResult.add(new PlaceMetaInfo("naverA", "C"));
        naverResult.add(new PlaceMetaInfo("naverB", "F"));
        naverResult.add(new PlaceMetaInfo("naverC", "E"));
        naverResult.add(new PlaceMetaInfo("naverD", "G"));
        naverResult.add(new PlaceMetaInfo("naverE", "h"));

        //when
        List<PlaceMetaInfo> placeMetaInfos =  placeSearchServiceImpl.mergePlaceMetaInfoListStrategy(kakaoResult, naverResult);

        //then
        Assertions.assertThat(placeMetaInfos).hasSize(8);
    }

    @Test
    @DisplayName("네이버 검색 결과가 적은 경우 카카오에서 결과 수로 보정 - 카카오10개/네이버1개 -> 카카오9개/네이버1개")
    public void compensation3() throws Exception {
        //given
        List<PlaceMetaInfo> kakaoResult = new ArrayList<>();
        kakaoResult.add(new PlaceMetaInfo("kakaoA", "A"));
        kakaoResult.add(new PlaceMetaInfo("kakaoB", "B"));
        kakaoResult.add(new PlaceMetaInfo("kakaoC", "C"));
        kakaoResult.add(new PlaceMetaInfo("kakaoD", "D"));
        kakaoResult.add(new PlaceMetaInfo("kakaoE", "E"));
        kakaoResult.add(new PlaceMetaInfo("kakaoF", "A"));
        kakaoResult.add(new PlaceMetaInfo("kakaoG", "B"));
        kakaoResult.add(new PlaceMetaInfo("kakaoH", "C"));
        kakaoResult.add(new PlaceMetaInfo("kakaoI", "D"));
        kakaoResult.add(new PlaceMetaInfo("kakaoJ", "E"));

        List<PlaceMetaInfo> naverResult = new ArrayList<>();
        naverResult.add(new PlaceMetaInfo("naverA", "C"));

        //when
        List<PlaceMetaInfo> placeMetaInfos =  placeSearchServiceImpl.mergePlaceMetaInfoListStrategy(kakaoResult, naverResult);
        //then
        Assertions.assertThat(placeMetaInfos).hasSize(10);

        int count =0 ;
        for(PlaceMetaInfo placeMetaInfo : placeMetaInfos){
            if (StringUtils.contains(placeMetaInfo.getTitle(), "naver")) {
                count++;
            }
        }
        Assertions.assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("네이버 검색 결과 내 타이틀에 포함된 html 태그 제거")
    public void removeTagInTitle() throws Exception {
        PlaceMetaInfo placeMetaInfo = new PlaceMetaInfo("naver<br>A</br>", "C");
        placeMetaInfo.setTitle(placeMetaInfo.getTitle().replaceAll("\\<.*?>", ""));

        Assertions.assertThat(placeMetaInfo.getTitle()).isEqualTo("naverA");
    }

    @Test
    @DisplayName("네이버 검색 결과 중 도로명의 state를 kakao 기준으로 일원화(ex. 경기도 -> 경기, 서울특별시 -> 서울)")
    public void changeStateInAddress() throws Exception {
        Assertions.assertThat(placeSearchServiceImpl.addressCorrection("경기도 A")).isEqualTo("경기 A");
        Assertions.assertThat(placeSearchServiceImpl.addressCorrection("충청북도 A")).isEqualTo("충북 A");
        Assertions.assertThat(placeSearchServiceImpl.addressCorrection("충청남도 A")).isEqualTo("충남 A");
        Assertions.assertThat(placeSearchServiceImpl.addressCorrection("서울특별시 A")).isEqualTo("서울 A");
        Assertions.assertThat(placeSearchServiceImpl.addressCorrection("제주특별시 A")).isEqualTo("제주 A");
        Assertions.assertThat(placeSearchServiceImpl.addressCorrection("대구광역시 A")).isEqualTo("대구 A");
        Assertions.assertThat(placeSearchServiceImpl.addressCorrection("대전광역시 A")).isEqualTo("대전 A");
    }
}