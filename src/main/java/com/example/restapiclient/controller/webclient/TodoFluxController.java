package com.example.restapiclient.controller.webclient;


import com.example.restapiclient.errorhandler.ResourceNotFound;
import com.example.restapiclient.model.Message;
import com.example.restapiclient.model.TodoClass;
import com.example.restapiclient.service.webclient.WebClientTodo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping(value = {"/flux/api/v1/todos", "/flux/api/v1/todos/"})
public class TodoFluxController {

    private final WebClientTodo webClient;

    public TodoFluxController(WebClientTodo webClient) {
        this.webClient = webClient;
    }

    @GetMapping(value = {"/user", "/user"})
    public Flux<TodoClass> getTodosByUserId(@RequestParam(name = "id", defaultValue = "smt") String st) {
        try {
            long id = Long.parseLong(st);
            if (id == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found");
            return webClient.getTodosByUserId(id);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,Message.idIsNotTrue);
        }
    }

    @GetMapping(value = {"/status", "/status"})
    public Flux<TodoClass> getTodosByStatus(@RequestParam("completed") String completed) {
        boolean status = Boolean.parseBoolean(completed);
        return webClient.getTodosByCompleted(status);
    }



    @GetMapping(value = {"/", ""})
    public Flux<TodoClass> getTodosByUserIdAndStatus(@RequestParam(value = "userId",defaultValue = "smt") String st,
                                                       @RequestParam(value = "status") String completed) {
        try {
            long id = Long.parseLong(st);
            if (id == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found");
            boolean status = Boolean.parseBoolean(completed);
            return webClient.getTodosByUserIdAndCompleted(id,status);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,Message.idIsNotTrue);
        }
    }


    @GetMapping(value = {"/{id}", "/{id}/"})
    public Mono<TodoClass> getTodoById(@PathVariable("id") String st) {
        try {
            long id = Long.parseLong(st);
            if (id == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found");
            return webClient.getTodoById(id);
        } catch (IllegalArgumentException i) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,Message.idIsNotTrue);
        } catch (ResourceNotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found");
        }
    }
}
