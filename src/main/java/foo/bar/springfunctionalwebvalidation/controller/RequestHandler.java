package foo.bar.springfunctionalwebvalidation.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.Validator;
import java.io.Serializable;
import java.util.function.Function;

@Component
public class RequestHandler {

    private final Validator validator;

    public RequestHandler(Validator validator) {
        this.validator = validator;
    }

    public <BODY> Mono<ServerResponse> requireValidBody(
            Function<Mono<BODY>, Mono<ServerResponse>> block,
            ServerRequest request, Class<BODY> bodyClass) {

        return request
                .bodyToMono(bodyClass)
                .flatMap(
                        body -> validator.validate(body).isEmpty()
                                ? block.apply(Mono.just(body))
                                : ServerResponse.unprocessableEntity()
                                .syncBody(validator.validate(body)
                                        .stream()
                                        .map(v -> new Result(v.getPropertyPath().toString(), v.getMessage())))
                );
    }
}

class Result implements Serializable {
    private String field;
    private String message;

    public Result(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
