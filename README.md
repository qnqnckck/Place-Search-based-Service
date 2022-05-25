# Place Search Based Service(PSBS) by keyword
[![Build Status](https://travis-ci.org/dwyl/esta.svg?branch=master)](https://travis-ci.org/)

키워드 기반의 장소 검색 서비스입니다. 카카오 네이버의 장소를 키워드 검색으로 제공하는 API 를 통해 데이터를 
정제하고, 실시간 키워드 랭킹 연산을 통해 목록 제공 및 실시간 검색 키워드 랭킹을 제공합니다.  

* 제공 API
  * 키워드 장소 검색
  * 실시간 검색 키워드 Top 10


## Usage
* 프로젝트 실행
  * 프로젝트 폴더에서 아래 명령을 수행하여 서버를 동작시킵니다.
```sh
$ ./gradlew clean bootRun
```

* API 호출
  * Intellij에서 api.http 파일내 API를 호출
  * Swagger 접속
    * http://localhost:8080/swagger-ui.html 접속 후 API 규격 확인 및 호출
    

* 테스트 방법
  * 실시간 검색어 랭킹 전 키워드 장소 검색 API를 호출
  * 동일 키워드로 장소 검색 API를 호출
  * 실시간 검색 키워드 Top 10 API를 호출
  
## Description  
 해당 프로젝트를 진행하면서 고민했던 포인트들을 함께 설명합니다.
### 전체 시스템 Architecture
기능을 구현할 서버(API Server) 이외에 전체 아키텍처를 고민하여 개발 범위를 산정하였습니다.
* 1안
  * ![architecture1](https://user-images.githubusercontent.com/10949665/170044674-568ec367-168b-426f-99e1-0c7551e55b07.png)
    * API Server에 대한 키워드 요청에 대한 로그 수집을 ELK를 활용합니다.
    * 검색어 랭킹의 경우 실시간으로 데이터의 정확성이 보장 될 필요가 없으므로 기반시스템 구성에 Jenkins 배치를 통해 주기적으로 Redis에 업데이트하여 랭킹을 제공합니다.
    

* 2안
  * ![architecture2](https://user-images.githubusercontent.com/10949665/170044658-bfa54706-44a2-4d98-85d6-d794f762990b.png)
    * 실시간 랭킹을 제공하기 위한 방법으로 Redis Sorted Set(Zset) 기능을 사용합니다.
    * 요청이 올때마다 Redis에 업데이트하여 랭킹을 산정합니다.
    * 문제 발생시 랭킹에 대한 데이터 보존을 위한 Snapshot으로 배치를 통해 DB에 저장합니다.

최종적으로 2안으로 채택하였습니다. API 제공 서버 기준으로 실시간 랭킹 구현이 Redis에 업데이트하는 기능을 포함하고 있어 더 많은 기능을 기준으로 선정하였습니다.   

### 설계 및 구현시 고려사항
* 응답성 개선
  * 키워드 검색시 성능 및 동시성 이슈를 해결하기 위해 싱글 스레드 기반의 전체 시스템이 공유하는 Global Redis Server가 있다는 가정하에 설게 하였습니다.
    * 로컬 동작시 테스트를 위해 임베디드 Redis 서버도 추가구성하였습니다.
* 장애 처리 방안 
  * 외부 연동을 하는 카카오와 네이버의 오픈 API의 경우 연동상의 Exception 발생시 10번 중 5번 이상 문제 발생하는 경우 장애 조치 되도록 CircuitBreak 설정하였습니다.
  * 키워드 랭킹에 대한 문제 발생시 키워드 랭킹에 대한 스냅샷을 DB에 저장 후 필요시 복구
* 외부연동 추가에 따른 코드 내 기능 확정성 고려
  * 외부연동을 하는 모듈의 경우  common 내  external 패키지에서 타겟별(카카오,네이버등)을 분리하여 구현하였습니다. 구글이 추가되는 경우 external의 대상을 추가하고 service에서 호출하는 구조로 작성하여 최소한의 변경점을 두었습니다.
  
### 장소 검색시 시나리오 정의
* 장소 검색의 결과가 도로명이 동일한 경우 같은 장소로 취급
  * 위도 경도로 하지 않는 이유
    * 네이버에서 사용하는 카텍 좌표계를 변환하더라도 위도, 경도와 동일하지 않을 수 있기 때문에
  * 이름을 동일장소 조건에 포함하지 않는 이유
    * 띄어쓰기 구분 및 장소명도 동일하게 등록되지 않은 곳도 많기 때문에

## IMPLEMENTATION
### 기술스택
  * springboot
  * webflux : webclient를 사용하여 non-blocking 외부연동
  * cache : redis client 
  * assertj : unit test를 위한 라이브러리
  * embedded redis : local profile에만 개발을 위해 반영
  * swagger : apidoc 자동화 라이브러리

### 개발환경
* JDK 11
* IntelliJ 22.04.03
* gradle
* SpringBoot 2.6.7 

### 기본 속성
* 서비스 포트
    * port : 8080
* 임베디드 Redis
  * server : localhost
  * port : 6379
* spring.profiles.active
    * default : local (개발 환경 기준으로 초기 설정)

