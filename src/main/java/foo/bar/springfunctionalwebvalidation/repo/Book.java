package foo.bar.springfunctionalwebvalidation.repo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Book {
    @Id
    private String ref;
    private String title;
    private String author;

    private Book() {
    }

    private Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public static Book of(String title, String author) {
        return new Book(title, author);
    }

    public String getRef() {
        return ref;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
}
