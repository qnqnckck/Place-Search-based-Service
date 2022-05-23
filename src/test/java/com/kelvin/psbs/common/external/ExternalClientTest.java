package com.kelvin.psbs.common.external;

import com.kelvin.psbs.common.exception.PbssException;
import com.kelvin.psbs.common.external.kakao.KakaoExternalClient;
import com.kelvin.psbs.common.external.kakao.dto.KakaoKeywordSearchDocuments;
import com.kelvin.psbs.common.external.kakao.dto.KakaoKeywordSearchResponse;
import com.kelvin.psbs.common.external.naver.NaverExternalClient;
import com.kelvin.psbs.common.vo.CommonResponse;
import com.kelvin.psbs.controller.vo.PlaceMetaInfo;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


//@TestPropertySource({"classpath:application.yml", "classpath:application-local.yml"})
//@TestPropertySource(properties = {
//        "external.kakao.host=http://localhost:8081",
//        "external.kakao.uri.keyword-search=/test",
//        "external.naver.host=http://localhost:8081",
//        "external.naver.uri.keyword-search=/test",
//        "server.port=8081"
//})
//@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
//@Import(ExternalClientTest.CircuitBreakTestController.class)
@SpringBootTest
@EnableAutoConfiguration
class ExternalClientTest {

    @Autowired
    private KakaoExternalClient kakaoExternalClient;

    @Autowired
    private NaverExternalClient naverExternalClient;

    @Test
    @DisplayName("kakao 키워드 장소 검색 확인")
    public void kakaoExtenralClientTest() throws Exception{
        List<PlaceMetaInfo> result =  kakaoExternalClient.search("제주").block();
        System.out.println(result);
        Assertions.assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("naver 키워드 장소 검색 확인")
    public void naverExtenralClientTest() throws Exception{
        List<PlaceMetaInfo> result =  naverExternalClient.search("제주").block();
        System.out.println(result);
        Assertions.assertThat(result).isNotEmpty();
    }
}