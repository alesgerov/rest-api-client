package com.example.restapiclient.controller;


import com.example.restapiclient.model.Comment;
import com.example.restapiclient.model.Message;
import com.example.restapiclient.model.ResponseForm;
import com.example.restapiclient.service.CommentRestClientService;
import com.example.restapiclient.utils.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentRestClientService commentService;

    List<Comment> comments;


    @BeforeEach
    public void prepare() {
        comments = new ArrayList<>();
        comments.add(new Comment(1, 1, "Name1", "EMail1", "nmnmnm"));
        comments.add(new Comment(2, 1, "Name2", "EMail2", "nmnmnm"));
        comments.add(new Comment(3, 1, "Name3", "EMail3", "nmnmnm"));

    }

    @Test
    public void getAllCommentsTest() throws Exception {
        String uri = "/client/api/v1/comments";
        MvcResult result = mockMvc.perform(get(uri)).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        Mockito.verify(commentService).getAllComments();
        Mockito.verifyNoMoreInteractions(commentService);
    }

    @Test
    public void deleteCommentById() throws Exception {
        String uri = "/client/api/v1/comments/1";

        MvcResult result=mockMvc.perform(delete(uri)).andReturn();
        assertEquals(200,result.getResponse().getStatus());

        Mockito.verify(commentService).deleteComment(1);
        Mockito.verifyNoMoreInteractions(commentService);
    }


}
