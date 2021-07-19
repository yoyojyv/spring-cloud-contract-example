package me.jerry.example.contract.producer

import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension

//@ExtendWith(value = [SpringExtension::class, MockitoExtension::class])
//@SpringBootTest(
//    classes = [ProducerBase.Config::class],
//    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
//    properties = ["server.port=0"]
//)
@SpringBootTest(
    classes = [ProducerBase.Config::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = ["server.port=0"]
)
@DirtiesContext
@AutoConfigureMessageVerifier
class ProducerBase {

    @LocalServerPort
    var port = 0

    @BeforeEach
    fun setup() {
        println("***port : $port")
        RestAssured.baseURI = "http://localhost:$port"
    }

    @Configuration
    @EnableAutoConfiguration
    class Config {

//        @Bean
//        fun personCheckingService(): PersonCheckingService {
//            return PersonCheckingService { personToCheck -> personToCheck.age >= 20 }
//        }
//
//        @Bean
//        fun producerController(): ProducerController {
//            return ProducerController(personCheckingService())
//        }

        @Bean
        fun evenOddController(): EvenOddController {
            return EvenOddController()
        }

    }

}
