package contracts

import org.springframework.cloud.contract.spec.ContractDsl.Companion.contract
import org.springframework.cloud.contract.spec.withQueryParameters

contract {

    description = "should return even when number input is even"

    request {
        method  = GET
        url = url("/validate/prime-number") withQueryParameters {
            parameter("number", "3")
        }
    }
    response {
        body = body("Odd")
//        bodyMatchers {
//            jsonPath("$", byRegex("Odd"))
//        }
        status = OK
    }

}
