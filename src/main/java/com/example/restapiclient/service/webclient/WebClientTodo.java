package com.example.restapiclient.service.webclient;

import com.example.restapiclient.config.RestClientConfig;
import com.example.restapiclient.model.TodoClass;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class WebClientTodo {


    private final WebClient webClient;


    public WebClientTodo(WebClient.Builder builder, RestClientConfig commentConfig) {
        this.webClient = builder.baseUrl(commentConfig.getTodosBaseUrl()).build();
    }

    public Mono<TodoClass> getTodoById(long id) {
        return webClient.get().uri("/"+id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(TodoClass.class)
                .onErrorReturn(new TodoClass());
    }

    public Flux<TodoClass> getTodosByUserId(long id) {
        return webClient.get().uri("?userId=" + id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(TodoClass.class);
    }

    public Flux<TodoClass> getTodosByCompleted(boolean status) {
        return webClient.get().uri("?completed=" + status)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(TodoClass.class);
    }

    public Flux<TodoClass> getTodosByUserIdAndCompleted(long id, boolean status) {
        return webClient.get().uri("?userId=" + id+"&completed="+status)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(TodoClass.class);
    }
}
