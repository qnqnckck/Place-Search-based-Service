# Place Search Based Service(PSBS) by keyword
키워드 기반의 장소 검색 서비스입니다. 카카오 네이버의 장소를 키워드 검색으로 제공하는 API 를 통해 데이터를 
정제하고, 실시간 키워드를 랭킹 연산을 통해 다운로드를 제공한다. 



## How to run
```sh
$ ./gradlew clean bootRun
```

## Development Environments
* JDK 11
* IntelliJ 22.04.03

### Default Property
* 서비스 포트
    * port : 8080
* 임베디드 Redis
    * port : 6379
* spring.profiles.active
    * default : local (개발 환경 기준으로 초기 설정)

