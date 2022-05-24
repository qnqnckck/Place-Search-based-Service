package com.kelvin.psbs.common.external.kakao;

import com.kelvin.psbs.common.external.ExternalClient;
import com.kelvin.psbs.common.external.kakao.dto.KakaoKeywordSearchDocuments;
import com.kelvin.psbs.common.external.kakao.dto.KakaoKeywordSearchResponse;
import com.kelvin.psbs.controller.vo.PlaceMetaInfo;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoExternalClient implements ExternalClient {

    private final WebClient webClient;

    @Value("${external.kakao.host}")
    private String host;

    @Value("${external.kakao.uri.keyword-search}")
    private String keywordSearchUri;

    @Value("${external.kakao.max-count}")
    private int maxCount;

    @Value("${external.kakao.api-key}")
    private String apiKey;

    @CircuitBreaker(name = "kakao", fallbackMethod = "fallback")
    public Mono<List<PlaceMetaInfo>> search(String keyword) {
        return webClient.mutate()
                .baseUrl(host)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder.path(keywordSearchUri)
                        .queryParam("query", keyword)
                        .queryParam("size", maxCount)
                        .build())
                .header("Authorization", apiKey)
                .retrieve()
                .onStatus(status -> status.is4xxClientError()
                                || status.is5xxServerError()
                        , clientResponse -> {
                            log.error("search http error : " + clientResponse.toString());
                            System.out.println(clientResponse.bodyToMono(String.class));
                            return Mono.empty();
                        })
                .bodyToMono(KakaoKeywordSearchResponse.class)
                .map(KakaoKeywordSearchResponse::getDocuments)
                .map(result -> result.stream().map(this::convert).collect(Collectors.toList()));
    }

    @Override
    public PlaceMetaInfo convert(Object o) {
        KakaoKeywordSearchDocuments kakaoKeywordSearchDocuments = (KakaoKeywordSearchDocuments) o;
        return new PlaceMetaInfo(kakaoKeywordSearchDocuments.getPlaceName(), kakaoKeywordSearchDocuments.getRoadAddressName());
    }

    private Mono<List<PlaceMetaInfo>> fallback(Throwable t) {
        log.error("Kakao fallback : " + t.getMessage());
        return Mono.empty();
    }
}
