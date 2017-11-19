package foo.bar.springfunctionalwebvalidation.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BookRepo extends ReactiveMongoRepository<Book, String> {
}
