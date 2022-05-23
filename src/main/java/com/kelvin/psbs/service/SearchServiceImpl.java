package com.kelvin.psbs.service;

import com.kelvin.psbs.controller.vo.SearchKeywordInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public List<SearchKeywordInfo> getSearchKeywordRank() {
        String key = "ranking";
        ZSetOperations<String, String> ZSetOperations = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> typedTuples = ZSetOperations.reverseRangeWithScores(key, 0, 9);  //score순으로 10개 보여줌
        return typedTuples.stream().map(this::convertToSearchRankMetaInfoList).collect(Collectors.toList());
    }

    /**
     * 키워드 사용 관련 통계룰 위한 Redis롤 notify
     *
     * @param keyword 검색어
     */

    @Override
    public void notify(String keyword) {
        double score = 0.0;
        try {
            redisTemplate.opsForZSet().incrementScore("ranking", keyword, 1);
        } catch (Exception e) {
            log.error(e.toString());
        }

        redisTemplate.opsForZSet().incrementScore("ranking", keyword, score);
    }

    private SearchKeywordInfo convertToSearchRankMetaInfoList(ZSetOperations.TypedTuple<String> stringTypedTuple) {
        return new SearchKeywordInfo(stringTypedTuple.getValue(), stringTypedTuple.getScore());
    }
}
