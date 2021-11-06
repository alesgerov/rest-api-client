package com.example.restapiclient.controller;


import com.example.restapiclient.model.Comment;
import com.example.restapiclient.model.Message;
import com.example.restapiclient.model.ResponseForm;
import com.example.restapiclient.service.resttemp.CommentRestClientService;
import com.example.restapiclient.utils.Util;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

        Mockito.when(commentService.getAllComments()).thenReturn(comments);
        Mockito.when(commentService.getCommentById(1)).thenReturn(Optional.of(comments.get(0)));
        Mockito.when(commentService.deleteComment(999999)).thenReturn(new ResponseForm(Message.commentNotFound));
        Mockito.when(commentService.getCommentsByPostId(1)).thenReturn(comments);
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
    public void testDeleteCommentById() throws Exception {
        String uri = "/client/api/v1/comments/1";

        MvcResult result = mockMvc.perform(delete(uri)).andReturn();
        assertEquals(200, result.getResponse().getStatus());

        Mockito.verify(commentService).deleteComment(1);
        Mockito.verifyNoMoreInteractions(commentService);
    }

    @Test
    public void testDeleteCommentByIDIsEqualStringShouldReturnError() throws Exception {
        String uri = "/client/api/v1/comments/smt";
        mockMvc.perform(delete(uri))
                .andExpect(status().is(409))
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.message", equalTo(Message.idIsNotTrue)));
        Mockito.verifyNoMoreInteractions(commentService);
    }

    @Test
    public void testDeleteCommentByIDIsEqualNotRealId() throws Exception {
        String uri = "/client/api/v1/comments/999999";
        mockMvc.perform(delete(uri))
                .andExpect(status().is(200));
        Mockito.verify(commentService).deleteComment(999999);
        Mockito.verifyNoMoreInteractions(commentService);
    }

    @Test
    public void testGetCommentByID() throws Exception {
        String uri = "/client/api/v1/comments/1";

        MvcResult result = mockMvc.perform(get(uri)).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        String json = result.getResponse().getContentAsString();
        Comment comment = Util.jsonToObject(json, Comment.class);

        assertNotNull(comment);
        assertComment(comment, comments.get(0));
        Mockito.verify(commentService).getCommentById(1);
        Mockito.verifyNoMoreInteractions(commentService);
    }

    @Test
    public void testGetCommentByIdIsEqualNotRealId() throws Exception {
        String uri = "/client/api/v1/comments/999999";
        mockMvc.perform(get(uri))
                .andExpect(status().is(409))
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.message", equalTo(Message.commentNotFound)));
        Mockito.verify(commentService).getCommentById(999999);
        Mockito.verifyNoMoreInteractions(commentService);
    }


    @Test
    public void testGetCommentByIdEqualString() throws Exception {
        String uri = "/client/api/v1/comments/smt";
        mockMvc.perform(get(uri))
                .andExpect(status().is(409))
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.message", equalTo(Message.idIsNotTrue)));
        Mockito.verifyNoMoreInteractions(commentService);
    }


    @Test
    public void testGetCommentsByPostId() throws Exception {
        String uri = "/client/api/v1/comments/post?id=1";
        MvcResult result = mockMvc.perform(get(uri)).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        String json = result.getResponse().getContentAsString();
        List<Comment> commentList = Arrays.asList(Util.jsonToObject(json, Comment[].class));
        assertNotNull(commentList);
        assertEquals(comments.size(), commentList.size());
        for (int i = 0; i < commentList.size(); i++) {
            assertComment(commentList.get(i), comments.get(i));
        }
        Mockito.verify(commentService).getCommentsByPostId(1);
        Mockito.verifyNoMoreInteractions(commentService);
    }


    @Test
    public void testGetCommentsByPostIdEqualString() throws Exception {
        String uri = "/client/api/v1/comments/post?id=smt";
        mockMvc.perform(get(uri))
                .andExpect(status().is(409))
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.message", equalTo(Message.idIsNotTrue)));
        Mockito.verifyNoMoreInteractions(commentService);
    }

    private void assertComment(Comment comment1, Comment comment2) {
        assertEquals(comment1.getId(), comment2.getId());
        assertEquals(comment1.getBody(), comment2.getBody());
        assertEquals(comment1.getEmail(), comment2.getEmail());
        assertEquals(comment1.getPostId(), comment2.getPostId());
        assertEquals(comment1.getName(), comment2.getName());
    }

}
