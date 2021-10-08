package com.example.restapiclient.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ResponseForm  implements Serializable {
    private String message;
}
