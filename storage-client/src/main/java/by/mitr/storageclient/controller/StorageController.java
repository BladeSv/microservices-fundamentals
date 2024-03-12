package by.mitr.storageclient.controller;

import by.mitr.storageclient.model.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@Controller
public class StorageController {

    @Autowired
    private WebClient webClient;

    @Value("${user.application.storage-service.host}")
    private String storageServiceHost;


    @GetMapping("/")
    public String index() {
        return "index";
    }


    @GetMapping("/storages")
    public String getStorages(
            @RegisteredOAuth2AuthorizedClient("storage-client-authorization-code") OAuth2AuthorizedClient authorizedClient, Model model
    ) {
        var storages = this.webClient
                .get()
                .uri(storageServiceHost + "/storages")
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(Storage[].class)
                .block();

        model.addAttribute("storages", storages);
        model.addAttribute("storage", new Storage());

        return "storages";
    }

    @PostMapping("/storages")
    public String getCreateStorage(
            @RegisteredOAuth2AuthorizedClient("storage-client-authorization-code") OAuth2AuthorizedClient authorizedClient,
            Model model, @ModelAttribute("storage") Storage newStorage
    ) {
        try {
            var storage = this.webClient
                    .post()
                    .uri(storageServiceHost + "/storages")
                    .accept(MediaType.APPLICATION_JSON)
                    .body(Mono.just(newStorage), Storage.class)
                    .attributes(oauth2AuthorizedClient(authorizedClient))
                    .retrieve()
                    .bodyToMono(HashMap.class)
                    .block();
        } catch (RuntimeException ignored) {
            model.addAttribute("errorMessage", "You do not have rights for this action.");
        }

        return getStorages(authorizedClient, model);
    }

    @GetMapping("/storages/delete/{id}")
    public String deleteStorage(@PathVariable("id") long id, @RegisteredOAuth2AuthorizedClient("storage-client-authorization-code") OAuth2AuthorizedClient authorizedClient,
                                Model model) {
        try {
            this.webClient
                    .delete()
                    .uri(storageServiceHost + "/storages?id=" + id)
                    .attributes(oauth2AuthorizedClient(authorizedClient))
                    .retrieve()
                    .bodyToMono(HashMap.class)
                    .block();

        } catch (RuntimeException ignored) {
            model.addAttribute("errorMessage", "You do not have rights for this action.");
        }
        return getStorages(authorizedClient, model);
    }
}
