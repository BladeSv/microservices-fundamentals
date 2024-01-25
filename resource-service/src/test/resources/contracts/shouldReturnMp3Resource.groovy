import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return mp3 song by id request"
    request {
        method GET()
        url("/resources/1")
    }
    response {
        body(
                test(new String([1, 1] as byte[]))
        )
        status(200)
    }
}
