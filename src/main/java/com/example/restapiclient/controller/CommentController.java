package com.example.restapiclient.controller;

import com.example.restapiclient.errorhandler.ResourceNotFound;
import com.example.restapiclient.model.Comment;
import com.example.restapiclient.model.Message;
import com.example.restapiclient.model.ResponseForm;
import com.example.restapiclient.service.CommentRestClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RequestMapping(value = {"/client/api/v1/comments", "/client/api/v1/comments/"})
@RestController
public class CommentController {

    private final CommentRestClientService commentService;

    public CommentController(CommentRestClientService commentService) {
        this.commentService = commentService;
    }


    @GetMapping
    public ResponseEntity<?> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @DeleteMapping(value = {"/{id}", "/{id}/"})
    public ResponseEntity<?> deleteComment(@PathVariable("id") String id) {
        try {
            long longId = Long.parseLong(id);
            return ResponseEntity.status(200).body(commentService.deleteComment(longId));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(409).body(new ResponseForm(Message.idIsNotTrue));
        } catch (ResourceNotFound r) {
            r.printStackTrace();
            return ResponseEntity.status(409).body(new ResponseForm(Message.commentNotFound));
        }
    }


    @GetMapping(value = {"/{id}", "/{id}/"})
    public ResponseEntity<?> getCommentById(@PathVariable("id") String stId) {
        try {
            long id=Long.parseLong(stId);
            Optional<Comment> optionalComment = commentService.getCommentById(id);
            if (optionalComment.isEmpty()) {
                return ResponseEntity.status(409).body(new ResponseForm(Message.commentNotFound));
            }
            return ResponseEntity.ok().body(optionalComment.get());
        } catch (ResourceNotFound e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(404).body(new ArrayList<>());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ResponseEntity.status(409).body(new ResponseForm(Message.idIsNotTrue));
        }

    }

    @GetMapping("/post")
    public ResponseEntity<?> getCommentsByPostId(@RequestParam("id") String id) {
        try {
            long postId = Long.parseLong(id);
            return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
        } catch (IllegalArgumentException  e) {
            e.printStackTrace();
            return ResponseEntity.status(409).body(new ResponseForm(Message.idIsNotTrue));
        } catch (ResourceNotFound r) {
            r.printStackTrace();
            return ResponseEntity.status(404).body(new ArrayList<>());
        }
    }


    @PostMapping(value = {"/add", "/add/"})
    public ResponseEntity<?> addComment(@RequestBody Comment comment) {
        return ResponseEntity.ok(commentService.addComment(comment));
    }


    @PutMapping(value = {"/{id}", "/{id}/"})
    public ResponseEntity<?> updateComment(@PathVariable("id") String id,
                                           @RequestBody Comment comment) {
        try {
            long longId = Long.parseLong(id);
            return ResponseEntity.ok(commentService.updateComment(longId, comment));
        } catch (ResourceNotFound r) {
            r.printStackTrace();
            return ResponseEntity.status(404).body(new ResponseForm(Message.commentNotFound));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(409).body(new ResponseForm(Message.idIsNotTrue));
        }
    }
}
