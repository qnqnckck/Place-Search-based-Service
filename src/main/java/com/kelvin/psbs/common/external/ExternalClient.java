package com.kelvin.psbs.common.external;

import com.kelvin.psbs.controller.vo.PlaceMetaInfo;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ExternalClient {
    Mono<List<PlaceMetaInfo>> search(String keyword);
}
