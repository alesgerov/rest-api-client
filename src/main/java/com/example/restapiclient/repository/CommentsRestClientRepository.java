package com.example.restapiclient.repository;

import com.example.restapiclient.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentsRestClientRepository {
    List<Comment> getAllComments();//TODO paging
    Optional<Comment> getCommentById(long id);

}