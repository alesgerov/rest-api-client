package com.example.restapiclient.controller;

import com.example.restapiclient.errorhandler.ResourceNotFound;
import com.example.restapiclient.model.Comment;
import com.example.restapiclient.model.Message;
import com.example.restapiclient.model.ResponseForm;
import com.example.restapiclient.service.CommentRestClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
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
    public ResponseEntity<?> deleteComment(@PathVariable("id") String  id) {
        try {
            long longId=Long.parseLong(id);
//            Optional<Comment> optionalComment=commentService.getCommentById(longId);
            return ResponseEntity.status(200).body(commentService.deleteComment(longId));
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return ResponseEntity.status(409).body(new ResponseForm(Message.idIsNotTrue));
        }catch (ResourceNotFound r){
            r.printStackTrace();
            return ResponseEntity.status(409).body(new ResponseForm(Message.commentNotFound));
        }
    }


    @GetMapping(value = {"/{id}", "/{id}/"})
    public ResponseEntity<?> getCommentById(@PathVariable("id") long id) {
        try {
            Optional<Comment> optionalComment = commentService.getCommentById(id);
            return ResponseEntity.ok().body(optionalComment.get());
        } catch (ResourceNotFound e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(404).body(new ArrayList<>());
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return ResponseEntity.status(409).body(new ResponseForm(Message.idIsNotTrue));
        }

    }

    @GetMapping("/post")
    public ResponseEntity<?> getCommentsByPostId(@RequestParam("id") String id) {
        try {
            long postId=Long.parseLong(id);
            return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return ResponseEntity.status(409).body(new ResponseForm(Message.idIsNotTrue));
        }catch (ResourceNotFound r){
            r.printStackTrace();
            return ResponseEntity.status(404).body(new ArrayList<>());
        }
    }


    @PostMapping(value = {"/add", "/add/"})
    public ResponseEntity<?> addComment(@RequestBody Comment comment) {
        return ResponseEntity.ok(commentService.addComment(comment));
    }


    @PutMapping(value = {"/{id}", "/{id}/"})
    public ResponseEntity<?> updateComment(@PathVariable("id") String  id,
                                 @RequestBody Comment comment) {
        try {
            long longId=Long.parseLong(id);
            return ResponseEntity.ok(commentService.updateComment(longId,comment));
        }catch (ResourceNotFound r){
            r.printStackTrace();
            return ResponseEntity.status(404).body(new ResponseForm(Message.commentNotFound));
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return ResponseEntity.status(409).body(new ResponseForm(Message.idIsNotTrue));
        }
    }
}
