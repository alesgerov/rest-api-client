package com.example.restapiclient.repository;

import com.example.restapiclient.model.TodoClass;

import java.util.List;
import java.util.Optional;

public interface TodosRestClientRepository {
    List<TodoClass> getTodosByUserId(long id);
    List<TodoClass> getTodosByCompleted(boolean status);
    List<TodoClass> getTodosByUserIdAndCompleted(long id,boolean status);
    Optional<TodoClass> getTodoById(long id);
}
