package com.example.restapiclient.controller;

import com.example.restapiclient.errorhandler.ResourceNotFound;
import com.example.restapiclient.model.Message;
import com.example.restapiclient.model.ResponseForm;
import com.example.restapiclient.model.TodoClass;
import com.example.restapiclient.service.TodoRestClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping(value = {"/client/api/v1/todos", "/client/api/v1/todos/"})
public class TodoController {

    private final TodoRestClientService clientService;

    public TodoController(TodoRestClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping(value = {"/user", "/user"})
    public ResponseEntity<?> getTodosByUserId(@RequestParam(name = "id", defaultValue = "smt") String st) {
        try {
            long id = Long.parseLong(st);
            if (id == 0) return ResponseEntity.status(404).body(new ArrayList<>());
            return ResponseEntity.ok(clientService.getTodosByUserId(id));
        } catch (NumberFormatException e) {
            return ResponseEntity.status(409).body(new ResponseForm(Message.idIsNotTrue));
        }
    }


    @GetMapping(value = {"/status", "/status"})
    public ResponseEntity<?> getTodosByStatus(@RequestParam("completed") String completed) {

        boolean status = Boolean.parseBoolean(completed);
        return ResponseEntity.ok(clientService.getTodosByCompleted(status));
    }


    @GetMapping(value = {"/", ""})
    public ResponseEntity<?> getTodosByUserIdAndStatus(@RequestParam(value = "userId",defaultValue = "smt") String st,
                                                       @RequestParam(value = "status") String completed) {
        try {
            long id = Long.parseLong(st);
            if (id == 0) return ResponseEntity.status(404).body(new ArrayList<>());
            boolean status = Boolean.parseBoolean(completed);
            return ResponseEntity.ok(clientService.getTodosByUserIdAndCompleted(id, status));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(new ResponseForm(Message.statusAndIdNotTrue));
        }
    }


    @GetMapping(value = {"/{id}", "/{id}/"})
    public ResponseEntity<?> getTodoById(@PathVariable("id") String st) {
        try {
            long id = Long.parseLong(st);
            if (id == 0) return ResponseEntity.status(404).body(new ArrayList<>());
            Optional<TodoClass> todoClass = clientService.getTodoById(id);
            return ResponseEntity.ok(todoClass.get());
        } catch (IllegalArgumentException i) {
            return ResponseEntity.status(409).body(new ResponseForm(Message.idIsNotTrue));
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(404).body(new ArrayList<>());
        }
    }


}
