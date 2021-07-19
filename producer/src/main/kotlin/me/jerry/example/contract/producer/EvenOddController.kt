package me.jerry.example.contract.producer

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class EvenOddController {

    @GetMapping("/validate/prime-number")
    fun isNumberPrime(@RequestParam number: Int): Mono<String> {
        val result= if (number % 2 == 0) {
            "Even"
        } else {
            "Odd"
        }
        return Mono.just(result)
    }

}
