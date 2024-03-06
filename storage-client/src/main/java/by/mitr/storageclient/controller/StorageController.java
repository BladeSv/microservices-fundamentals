package by.mitr.storageclient.controller;

import by.mitr.storageclient.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@Controller
public class StorageController {


    @Autowired
    private WebClient webClient;


    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }
//    @GetMapping("/storages")
//    public String storages(Model model){
//        model.addAttribute("message","Hello storages");
//        return "storages";
//    }

    @GetMapping("/storages")
    public String getArticles(
            @RegisteredOAuth2AuthorizedClient("storage-client-authorization-code") OAuth2AuthorizedClient authorizedClient, Model model
    ) {
        var articles = this.webClient
                .get()
                .uri("http://localhost:8083/articles")
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(String[].class)
                .block();
        model.addAttribute("student", new Student());
        model.addAttribute("storages", articles);
        return "storages";
    }

    @PostMapping("/storages")
    public String getArticles2(
            @RegisteredOAuth2AuthorizedClient("storage-client-authorization-code") OAuth2AuthorizedClient authorizedClient,
            Model model, @ModelAttribute("student") Student student
    ) {
        System.out.println("Post /storages");
        var articles = this.webClient
                .post()
                .uri("http://localhost:8083/articles?id=" + student.getName())
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(String[].class)
                .block();

        model.addAttribute("storages", articles);
        return "storages";
    }
}
