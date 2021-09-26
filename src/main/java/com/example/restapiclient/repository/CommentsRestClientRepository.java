package com.example.restapiclient.repository;

import com.example.restapiclient.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentsRestClientRepository {
    List<Comment> getAllComments();

    Optional<Comment> getCommentById(long id);

    List<Comment> getCommentsByPostId(long id);

    Comment addComment(Comment comment);

    Comment deleteComment(long id);

    Comment updateComment(long id, Comment comment);
}
