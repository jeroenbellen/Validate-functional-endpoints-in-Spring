package foo.bar.springfunctionalwebvalidation.controller;

import foo.bar.springfunctionalwebvalidation.repo.Book;
import foo.bar.springfunctionalwebvalidation.repo.BookRepo;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class BookHandler {

    private final BookRepo bookRepo;
    private final RequestHandler requestHandler;

    public BookHandler(BookRepo bookRepo, RequestHandler requestHandler) {
        this.bookRepo = bookRepo;
        this.requestHandler = requestHandler;
    }

    public Mono<ServerResponse> getBooks(ServerRequest request) {

        return ok()
                .body(fromPublisher(bookRepo.findAll(), Book.class));
    }

    public Mono<ServerResponse> addBook(ServerRequest request) {

        return requestHandler.requireValidBody(body -> {
            Mono<Book> bookMono = body.map(addBook -> Book.of(addBook.getTitle(), addBook.getAuthor()));

            return bookRepo.insert(bookMono)
                    .map(Book::getRef)
                    .map(ref -> URI.create("http://localhost:8080/books/" + ref))
                    .map(ServerResponse::created)
                    .flatMap(ServerResponse.HeadersBuilder::build)
                    .next();
        }, request, AddBook.class);
    }

}
