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
    private final ValidationHandler validationHandler;

    public BookHandler(BookRepo bookRepo, ValidationHandler validationHandler) {
        this.bookRepo = bookRepo;
        this.validationHandler = validationHandler;
    }

    public Mono<ServerResponse> getBooks(ServerRequest request) {

        return ok()
                .body(fromPublisher(bookRepo.findAll(), Book.class));
    }

    public Mono<ServerResponse> addBook(ServerRequest request) {

        return validationHandler.validBody(body -> {
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
