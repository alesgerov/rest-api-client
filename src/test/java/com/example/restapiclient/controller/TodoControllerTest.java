package com.example.restapiclient.controller;


import com.example.restapiclient.model.TodoClass;
import com.example.restapiclient.service.TodoRestClientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoRestClientService service;
    private List<TodoClass> todosFalse;
    private List<TodoClass> todosTrue;
    private List<TodoClass> todosById;
    private TodoClass todo;


    @BeforeEach
    public  void prepare() {
        todosFalse = new ArrayList<>();
        todosFalse.add(new TodoClass(1, 1, "Titlle1", false));
        todosFalse.add(new TodoClass(1, 2, "Titlle2", false));
        todosFalse.add(new TodoClass(1, 3, "Titlle3", false));
        todo=new TodoClass(1,1,"Title1",false);
        todosById = todosFalse;
        Mockito.when(service.getTodosByCompleted(false)).thenReturn(todosFalse);
        Mockito.when(service.getTodosByUserId(1)).thenReturn(todosById);

        todosTrue = new ArrayList<>();
        todosTrue.add(new TodoClass(1, 1, "Titlle1", true));
        todosTrue.add(new TodoClass(1, 2, "Titlle2", true));
        todosTrue.add(new TodoClass(1, 3, "Titlle3", true));

        Mockito.when(service.getTodosByCompleted(true)).thenReturn(todosTrue);
        Mockito.when(service.getTodosByUserIdAndCompleted(1, false)).thenReturn(todosFalse);
        Mockito.when(service.getTodoById(1)).thenReturn(Optional.of(todo));

    }

    @Test
    public void testGetTodosByStatusByObjectFalse() throws Exception {
        MvcResult result = mockMvc.perform(get("/client/api/v1/todos/status?completed=false")).andReturn();

        assertEquals(200, result.getResponse().getStatus());

        String json = result.getResponse().getContentAsString();
        List<TodoClass> todos = Arrays.asList(jsonToObject(json, TodoClass[].class));
        assertNotNull(todos);
        assertEquals(todos.size(), this.todosFalse.size());

        for (int i = 0; i < todos.size(); i++) {
            assertTodos(todos.get(i), this.todosFalse.get(i));
        }

        Mockito.verify(service).getTodosByCompleted(false);
        Mockito.verifyNoMoreInteractions(service);

    }

    @Test
    public void testGetTodosByStatusByObjectTrue() throws Exception {
        MvcResult result = mockMvc.perform(get("/client/api/v1/todos/status?completed=true")).andReturn();

        assertEquals(200, result.getResponse().getStatus());

        String json = result.getResponse().getContentAsString();
        List<TodoClass> todos = Arrays.asList(jsonToObject(json, TodoClass[].class));
        assertNotNull(todos);
        assertEquals(todos.size(), this.todosTrue.size());

        for (int i = 0; i < todos.size(); i++) {
            assertTodos(todos.get(i), this.todosTrue.get(i));
        }
        Mockito.verify(service).getTodosByCompleted(true);
        Mockito.verifyNoMoreInteractions(service);

    }


    @Test
    public void testGetTodosByStatusByDummyData() throws Exception {
        MvcResult result = mockMvc.perform(get("/client/api/v1/todos/status?completed=smmsms")).andReturn();

        assertEquals(200, result.getResponse().getStatus());

        String json = result.getResponse().getContentAsString();
        List<TodoClass> todos = Arrays.asList(jsonToObject(json, TodoClass[].class));
        assertNotNull(todos);
        assertEquals(todos.size(), this.todosFalse.size());

        for (int i = 0; i < todos.size(); i++) {
            assertTodos(todos.get(i), this.todosFalse.get(i));
        }
        Mockito.verify(service).getTodosByCompleted(false);
        Mockito.verifyNoMoreInteractions(service);

    }


    @Test
    public void testGetTodosByUserId() throws Exception {
        String uri = "/client/api/v1/todos/user?id=1";
        MvcResult result = mockMvc.perform(get(uri)).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        String json = result.getResponse().getContentAsString();
        List<TodoClass> todoClasses = Arrays.asList(jsonToObject(json, TodoClass[].class));
        assertNotNull(todoClasses);
        assertEquals(todoClasses.size(), this.todosById.size());

        for (int i = 0; i < todoClasses.size(); i++) {
            assertTodos(todoClasses.get(i), this.todosById.get(i));
        }
        Mockito.verify(service).getTodosByUserId(1);
        Mockito.verifyNoMoreInteractions(service);
    }


    @Test
    public void testGetTodosByUserIdIsEquals0ShouldReturnEmptyObject() throws Exception {
        String uri = "/client/api/v1/todos/user?id=0";
        MvcResult result = mockMvc.perform(get(uri)).andReturn();
        assertEquals(404, result.getResponse().getStatus());
        String json = result.getResponse().getContentAsString();
        List<TodoClass> todoClasses = Arrays.asList(jsonToObject(json, TodoClass[].class));
        assertEquals(todoClasses, new ArrayList<>());
        Mockito.verifyNoMoreInteractions(service);
    }


    @Test
    public void testGetTodosByUserIdIsEqualsStringShouldReturnIdNotFoundResponse() throws Exception {
        String uri = "/client/api/v1/todos/user?id=mmmmmmmm";
        mockMvc.perform(get(uri))
                .andExpect(status().is(409))
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.message", equalTo("Id is not true")));

        Mockito.verifyNoMoreInteractions(service);
    }

    @Test
    public void testGetTodosByUserIdAndStatusInNormalSituation() throws Exception {
        String uri = "/client/api/v1/todos?userId=1&status=false";
        MvcResult result = mockMvc.perform(get(uri)).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        String json = result.getResponse().getContentAsString();
        List<TodoClass> todoClasses = Arrays.asList(jsonToObject(json, TodoClass[].class));
        assertNotNull(todoClasses);
        assertEquals(todoClasses.size(), this.todosFalse.size());
        for (int i = 0; i < todoClasses.size(); i++) {
            assertEquals(todoClasses.get(i), this.todosFalse.get(i));
        }
        Mockito.verify(service).getTodosByUserIdAndCompleted(1, false);
        Mockito.verifyNoMoreInteractions(service);
    }


    @Test
    public void testGetTodosByUserIdEqualsStringAndStatus() throws Exception {
        String uri = "/client/api/v1/todos?userId=uuuuu&status=false";
        mockMvc.perform(get(uri))
                .andExpect(status().is(409))
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.message", equalTo("Id or status is not true")));

        Mockito.verifyNoMoreInteractions(service);
    }

    @Test
    public void testGetTodosByUserIdEqualsZeroAndStatus() throws Exception {
        String uri = "/client/api/v1/todos?userId=0&status=false";
        MvcResult result = mockMvc.perform(get(uri)).andReturn();
        assertEquals(404, result.getResponse().getStatus());
        String json = result.getResponse().getContentAsString();
        List<TodoClass> todoClasses = Arrays.asList(jsonToObject(json, TodoClass[].class));
        assertEquals(todoClasses, new ArrayList<>());
        Mockito.verifyNoMoreInteractions(service);
    }


    @Test
    public void testGetTodosById() throws Exception{
        String uri = "/client/api/v1/todos/1";
        MvcResult result=mockMvc.perform(get(uri)).andReturn();
        assertEquals(200,result.getResponse().getStatus());
        String json=result.getResponse().getContentAsString();
        TodoClass todoClass=jsonToObject(json,TodoClass.class);
        assertNotNull(todoClass);
        assertTodos(todoClass,todo);
        Mockito.verify(service).getTodoById(1);
        Mockito.verifyNoMoreInteractions(service);
    }


    @Test
    public void testGetTodosByIdWhenIdEqualZero() throws Exception{
        String uri = "/client/api/v1/todos/0";
        MvcResult result=mockMvc.perform(get(uri)).andReturn();
        assertEquals(404,result.getResponse().getStatus());
        String json = result.getResponse().getContentAsString();
        List<TodoClass> todoClasses = Arrays.asList(jsonToObject(json, TodoClass[].class));
        assertEquals(todoClasses, new ArrayList<>());
        Mockito.verifyNoMoreInteractions(service);
    }


    @Test
    public void testGetTodoByIdWhenIdEqualString() throws Exception {
        String uri = "/client/api/v1/todos/uuuuu";
        mockMvc.perform(get(uri))
                .andExpect(status().is(409))
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.message", equalTo("Id is not true.")));

        Mockito.verifyNoMoreInteractions(service);
    }



    private void assertTodos(TodoClass todoClass1, TodoClass todoClass2) {
        assertEquals(todoClass1.getUserId(), todoClass2.getUserId());
        assertEquals(todoClass1.getId(), todoClass2.getId());
        assertEquals(todoClass1.getTitle(), todoClass2.getTitle());
        assertEquals(todoClass1.isCompleted(), todoClass2.isCompleted());
    }


    private <T> T jsonToObject(String json, Class<T> o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, o);
    }


    private String objectToJson(Object o) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(o);
    }
}