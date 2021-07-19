# Spring Cloud Contract Sample - Kotlin DSL Based

## Getting Started
- Gradle Kotlin 기반의 프로젝트 설정
- Spring Webflux 기반 설정 
- Kotlin DSL 을 이용한 Contract 작성
- CDC 관련 정보는 찾아볼 것

### 코드 참조
- [Spring Cloud Contract 공식 샘플](https://github.com/spring-cloud-samples/spring-cloud-contract-samples)

- [개른이님 블로그](https://dev-kidult.tistory.com/15)

- [개른이님 샘플코드](https://github.com/dev-kidult/spring-cloud-contract-study)


### 프로젝트 구조
gradle multi project 구조
- producer
- consumer

* gradle 설정 더 정리를 할 수 있지만, 예제의 중요 부분은 아니니 이부분 참고 할것. 중요부분은 Contract!!   

## 삽질의 흔적
contract gradle 플러그인에서 exec 를 통해 별도 java 실행 부분이 있음 
- GenerateClientStubsFromDslTask 이놈이 그놈 

producer 쪽 dependency (네이티브쪽 문제가 있음)
- testImplementation("org.jetbrains.kotlin:kotlin-scripting-compiler-embeddable")

- contracts 설정 블록 부분 
```
contracts {
}
```
- MVC 기반의 예제는 겁나 많은데, WebFlux 쪽은 너무 찾기 힘듬
- Groovy 기반의 예제는 겁나 많은데, Kotlin DSL 실제 돌아가는 예제는 찾기 힘듬
- 제가 대신 삽질을 해 봤습니다.

## 실행해보기 

- 먼저 producer build
```
./gradlew producer:clean
./gradlew producer:build
```
- Stub 이 만들이지는지 확인, maven home 디렉토리에 생성이 되어있는지 확인

- consumer 쪽 테스트 실행해보기
ContractConsumerTest.kt 테스트 실행 or ./gradlew consumer:test   
