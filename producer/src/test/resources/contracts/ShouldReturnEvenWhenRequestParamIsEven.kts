package contracts

import org.springframework.cloud.contract.spec.ContractDsl.Companion.contract
import org.springframework.cloud.contract.spec.withQueryParameters

contract {

    description = "should return even when number input is even"

    request {
        method  = GET
        url = url("/validate/prime-number") withQueryParameters {
            parameter("number", "2")
        }
    }
    response {
        status = OK
        body = body("Even")
    }

}
