package com.example.restapiclient.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoClass {
    private long userId;
    private long id;
    private String title;
    private boolean completed;
}
