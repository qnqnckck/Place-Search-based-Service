package com.kelvin.psbs.common.external.naver;

import com.kelvin.psbs.common.external.naver.dto.NaverKeywordSearchItems;
import com.kelvin.psbs.common.external.naver.dto.NaverKeywordSearchResponse;
import com.kelvin.psbs.controller.vo.PlaceMetaInfo;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Component
@RequiredArgsConstructor
public class NaverExternalClient {

    private final WebClient webClient;

    @Value("${external.naver.host}")
    private String host;

    @Value("${external.naver.uri.keyword-search}")
    private String keywordSearchUri;

    @Value("${external.naver.max-count}")
    private int maxCount;

    @Value("${external.naver.client-id}")
    private String clientId;

    @Value("${external.naver.client-secret}")
    private String clientSecret;

    @CircuitBreaker(name = "naver", fallbackMethod = "fallback")
    public Mono<List<PlaceMetaInfo>> search(String keyword) {
        return webClient.mutate()
                .baseUrl(host)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(keywordSearchUri)
                        .queryParam("query", keyword)
                        .queryParam("display", maxCount)
                        .build())
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .retrieve()
                .onStatus(status -> status.is4xxClientError()
                                || status.is5xxServerError()
                        , clientResponse -> {
                            log.error("search http error : " + clientResponse.toString());
                            System.out.println(clientResponse.bodyToMono(String.class));

                            return Mono.empty();
                        })
                .bodyToMono(NaverKeywordSearchResponse.class)
                .map(NaverKeywordSearchResponse::getItems)
                .map(result -> result.stream().map(this::convert).collect(Collectors.toList()))
                .onErrorResume(e -> {
                    log.error("!!!!!!!!!!");
                    return Mono.just(new ArrayList<>());
                });
    }

    public PlaceMetaInfo convert(NaverKeywordSearchItems naverKeywordSearchItems) {
        return new PlaceMetaInfo(naverKeywordSearchItems.getTitle(), naverKeywordSearchItems.getRoadAddress());
    }
    private Mono<List<PlaceMetaInfo>> fallback(Throwable t) {
        log.error("??????????");
        log.error("naver fallback : " + t.getMessage());
        return Mono.empty();
    }
}
