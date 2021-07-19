package me.jerry.example.contract.consumer

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerPort
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(
    ids = ["me.jerry.example:producer"],
    stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
@AutoConfigureJsonTesters
class ContactConsumerTest {

    lateinit var webTestClient: WebTestClient

    // 이 부분 Integer -> Kotlin Int 로 바꾸면 컴파일 오류임
    // warnning This class shouldn't be used in Kotlin. Use kotlin.Int instead.
    // @StubRunnerPort("producer") lateinit var port: Integer
    @StubRunnerPort("producer") var port: Int = 0

    @BeforeEach
    fun before() {
        webTestClient = WebTestClient
            .bindToServer()
            .baseUrl("http://localhost:" + this.port)
            .build()
    }


    @Test
    fun evenOK() {
        webTestClient
            .get()
            .uri { uriBuilder->
                uriBuilder
                    .path("/validate/prime-number")
                    .queryParam("number", 2)
                    .build()
            }
            .accept(MediaType.APPLICATION_JSON)
            // .header(HttpHeaders.AUTHORIZATION, "FAKE_AUTH")
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.OK)
            .expectBody()
            .jsonPath("$").isNotEmpty
            .jsonPath("$").isEqualTo("Even")
//            .consumeWith {
//                Assertions.assertTrue(String(it.responseBody!!) == "Even")
//            }
    }

    @Test
    fun oddOK() {
        webTestClient
            .get()
            .uri { uriBuilder->
                uriBuilder
                    .path("/validate/prime-number")
                    .queryParam("number", 3)
                    .build()
            }
            .accept(MediaType.APPLICATION_JSON)
            // .header(HttpHeaders.AUTHORIZATION, "FAKE_AUTH")
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.OK)
            .expectBody()
            .consumeWith {
                Assertions.assertTrue(String(it.responseBody!!) == "Odd")
            }
    }

}
