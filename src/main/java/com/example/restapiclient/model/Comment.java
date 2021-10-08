package com.example.restapiclient.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private long id;
    private long postId;
    private String name;
    private String email;
    private String body;
}
