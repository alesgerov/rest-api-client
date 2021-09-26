package com.example.restapiclient.controller;

import com.example.restapiclient.errorhandler.ResourceNotFound;
import com.example.restapiclient.model.TodoClass;
import com.example.restapiclient.service.TodoRestClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = {"/client/api/v1/todos", "/client/api/v1/todosh/"})
public class TodoController {

    private final TodoRestClientService clientService;

    public TodoController(TodoRestClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping(value = {"/user/{id}", "/user/{id}/"})
    public List<TodoClass> getTodosByUserId(@PathVariable("id") long id) {
        return clientService.getTodosByUserId(id);
    }


    @GetMapping(value = {"/status/{status}", "/status/{status}/"})
    public List<TodoClass> getTodosByUserId(@PathVariable("status") boolean status) {
        return clientService.getTodosByCompleted(status);
    }


    @GetMapping(value = {"/", ""})
    public List<TodoClass> getTodosByUserId(@RequestParam("userId") long id,
                                            @RequestParam("status") boolean status) {
        return clientService.getTodosByUserIdAndCompleted(id, status);
    }


    @GetMapping(value = {"/{id}", "/{id}/"})
    public TodoClass getTodosById(@PathVariable("id") long id) {
        try {
            Optional<TodoClass> todoClass = clientService.getTodoById(id);
            return todoClass.get();
        } catch (ResourceNotFound e) {
            return null;
        }
    }


}
