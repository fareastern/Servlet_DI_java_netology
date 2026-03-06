package netology.config;

import netology.controller.PostController;
import netology.repository.PostRepository;
import netology.service.PostService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public PostRepository repository() {
        return new PostRepository();
    }

    @Bean
    public PostService service(PostRepository repository) {
        return new PostService(repository);
    }

    @Bean
    public PostController controller(PostService service) {
        return new PostController(service);
    }
}