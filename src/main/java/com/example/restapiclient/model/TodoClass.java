package com.example.restapiclient.model;


import lombok.Data;

@Data
public class TodoClass {
    private long userId;
    private long id;
    private String  title;
    private boolean completed;
}
