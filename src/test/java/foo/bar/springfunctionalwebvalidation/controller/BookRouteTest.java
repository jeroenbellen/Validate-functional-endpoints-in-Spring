package foo.bar.springfunctionalwebvalidation.controller;

import foo.bar.springfunctionalwebvalidation.repo.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class BookRouteTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void addsBook() throws Exception {
        ResponseEntity<Void> response = restTemplate.postForEntity("/books", new AddBook().setAuthor("Dostoyevsky").setTitle("The Idiot"), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(CREATED);

        String ref = response.getHeaders().getLocation().getPath().replace("/books/", "");

        ResponseEntity<Book[]> getBooksResponse = restTemplate.getForEntity("/books", Book[].class);

        assertThat(getBooksResponse.getStatusCode()).isEqualTo(OK);

        Book book = Book.of("The Idiot", "Dostoyevsky");
        ReflectionTestUtils.setField(book, "ref", ref);
        assertThat(getBooksResponse.getBody())
                .usingFieldByFieldElementComparator()
                .containsExactly(book);
    }

    @Test
    public void addsBookWithoutAuthorFails() throws Exception {
        assertsBadRequest(new AddBook().setTitle("The Idiot"));
    }

    @Test
    public void addsBookWithoutTitleFails() throws Exception {
        assertsBadRequest(new AddBook().setAuthor("Dostoyevsky"));
    }

    private void assertsBadRequest(AddBook addBook) {
        ResponseEntity<Void> response = restTemplate.postForEntity("/books", addBook, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(UNPROCESSABLE_ENTITY);
        assertThat(restTemplate.getForEntity("/books", Book[].class).getBody()).isEmpty();
    }
}