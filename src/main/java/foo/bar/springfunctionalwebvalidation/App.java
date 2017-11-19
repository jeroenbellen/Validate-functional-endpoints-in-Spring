package foo.bar.springfunctionalwebvalidation;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(App.class)
                .build()
                .run(args);
    }
}
