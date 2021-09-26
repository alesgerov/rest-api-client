package com.example.restapiclient.service;

import com.example.restapiclient.config.RestClientConfig;
import com.example.restapiclient.errorhandler.ClientErrorHandler;
import com.example.restapiclient.model.TodoClass;
import com.example.restapiclient.repository.TodosRestClientRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.awt.geom.RectangularShape;
import java.util.List;
import java.util.Optional;

@Repository
public class TodoRestClientService implements TodosRestClientRepository {

    private final RestClientConfig config;
    private final ClientErrorHandler errorHandler;
    public TodoRestClientService(RestClientConfig config, ClientErrorHandler errorHandler) {
        this.config = config;
        this.errorHandler = errorHandler;
    }

    @Override
    public List<TodoClass> getTodosByUserId(long id) {
        RestTemplate restTemplate =new RestTemplate();
        restTemplate.setErrorHandler(errorHandler);
        return restTemplate.getForObject(config.getTodosBaseUrl()+"?userId="+id,List.class);
    }

    @Override
    public List<TodoClass> getTodosByCompleted(boolean status) {
        RestTemplate restTemplate=new RestTemplate();
        restTemplate.setErrorHandler(errorHandler);
        return restTemplate.getForObject(config.getTodosBaseUrl()+"?completed="+status,List.class);
    }

    @Override
    public List<TodoClass> getTodosByUserIdAndCompleted(long id, boolean status) {
        RestTemplate restTemplate=new RestTemplate();
        restTemplate.setErrorHandler(errorHandler);
        return restTemplate.getForObject(config.getTodosBaseUrl()+"?completed="+status+"&"+"userId="+id,List.class);
    }

    @Override
    public Optional<TodoClass> getTodoById(long id) {
        RestTemplate restTemplate=new RestTemplate();
        restTemplate.setErrorHandler(errorHandler);
        TodoClass optionalTodoClass=restTemplate.getForObject(config.getTodosBaseUrl()+"/"+id,TodoClass.class);
        return Optional.of(optionalTodoClass);
    }
}
