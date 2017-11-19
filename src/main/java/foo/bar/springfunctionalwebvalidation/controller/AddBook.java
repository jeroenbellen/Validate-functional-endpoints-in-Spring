package foo.bar.springfunctionalwebvalidation.controller;

import javax.validation.constraints.NotEmpty;

public class AddBook {
    @NotEmpty
    private String title;
    @NotEmpty
    private String author;

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public AddBook setTitle(String title) {
        this.title = title;
        return this;
    }

    public AddBook setAuthor(String author) {
        this.author = author;
        return this;
    }
}
