package foo.bar.springfunctionalwebvalidation.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BookRoute {

    private static final String ENDPOINT = "/books";

    @Bean
    public RouterFunction<ServerResponse> bookRouter(final BookHandler handler) {
        return route(
                GET(ENDPOINT), handler::getBooks
        ).and(route(
                POST(ENDPOINT), handler::addBook)
        );
    }
}
