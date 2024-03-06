package by.mitr.storageservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticlesController {
    @GetMapping("/articles")
    public String[] getArticles() {
        return new String[]{"Article 1", "Article 2", "Article 3"};
    }

    @PostMapping("/articles")
    public String[] getArticles2(@RequestParam String id) {
        System.out.println("POST with param=" + id);
        return new String[]{"Post", "Article 2", "Article 3"};
    }
}
