package com.colorpl.comment.controller;

import com.colorpl.comment.dto.CommentDTO;
import com.colorpl.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 특정 리뷰의 모든 댓글 가져오기
    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByReviewId(
            @PathVariable Long reviewId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<CommentDTO> commentPage = commentService.getCommentsByReviewId(reviewId, pageable);
        List<CommentDTO> comments = commentPage.getContent();

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    // 리뷰에 댓글 작성
    @PostMapping("/reviews/{reviewId}/members/{memberId}")
//    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<CommentDTO> createComment(@PathVariable Long reviewId, @PathVariable Integer memberId, @RequestBody CommentDTO commentDTO) {
        CommentDTO createdComment = commentService.createComment(reviewId, memberId, commentDTO);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    // 특정 댓글 수정
    @PutMapping("/{commentId}/members/{memberId}")
//    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long commentId, @PathVariable Integer memberId, @RequestBody CommentDTO commentDTO) {

        if (commentDTO.getMemberId() == null) {
            throw new IllegalArgumentException("Nooooooooo Member ID must not be null!");
        }

        CommentDTO updatedComment = commentService.updateComment(commentId, memberId, commentDTO);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    // 특정 댓글 삭제
    @DeleteMapping("/{commentId}")
//    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Void> deleteCommentById(@PathVariable Long commentId) {
        commentService.deleteById(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
