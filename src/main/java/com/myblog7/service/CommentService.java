package com.myblog7.service;

import com.myblog7.payload.CommentDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CommentService {

    CommentDto createComment(long postId, CommentDto commentDto);

    List<CommentDto> getCommentsByPostId(long postId);

    CommentDto getCommentsById(Long postId, Long commentId);

    List<CommentDto> getAllCommentsById();

    ResponseEntity<String> deleteCommentById(Long postId, Long commentId);
}
