package com.ybe.ybe_toyproject3.domain.comment.service;

import com.ybe.ybe_toyproject3.domain.comment.dto.*;
import com.ybe.ybe_toyproject3.domain.comment.exception.CommentNotFoundException;
import com.ybe.ybe_toyproject3.domain.comment.exception.DisableEditCommentException;
import com.ybe.ybe_toyproject3.domain.comment.exception.DisableLookUpOtherUserInfoException;
import com.ybe.ybe_toyproject3.domain.comment.model.Comment;
import com.ybe.ybe_toyproject3.domain.comment.repository.CommentRepository;
import com.ybe.ybe_toyproject3.domain.trip.exception.TripNotFoundException;
import com.ybe.ybe_toyproject3.domain.trip.model.Trip;
import com.ybe.ybe_toyproject3.domain.trip.repository.TripRepository;
import com.ybe.ybe_toyproject3.domain.user.model.User;
import com.ybe.ybe_toyproject3.domain.user.repository.UserRepository;
import com.ybe.ybe_toyproject3.global.util.SecurityUtilProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final SecurityUtilProvider securityUtilImpl;


    @Transactional
    public CommentCreateResponse createComment(CommentCreateRequest commentCreateRequest, Long tripId) {
        User user = validatedUserNotEmpty();
        Trip trip = validatedTripNotEmpty(tripId);
        Comment comment = commentCreateRequest.toEntity();
        comment.addTrip(trip);
        comment.addUser(user);
        Comment saveComment = commentRepository.save(comment);
        return CommentCreateResponse.fromEntity(saveComment);
    }

    @Transactional(readOnly = true)
    public List<CommentReadResponse> findAllCommentByUserId(Long userId) {
        Long currentUserId = securityUtilImpl.getCurrentUserId();
        if (userId == currentUserId) {
            List<Comment> userPersonalComment = commentRepository.findAllByUserId(currentUserId);
            List<CommentReadResponse> userCommentList = new ArrayList<>();
            for (Comment comment : userPersonalComment) {
                userCommentList.add(CommentReadResponse.fromEntity(comment));
            }
            return userCommentList;
        } else {
            throw new DisableLookUpOtherUserInfoException();
        }
    }
    @Transactional
    public CommentUpdateResponse editComment(Long commentId, CommentUpdateRequest commentUpdateRequest) {
        Long currentUserId = securityUtilImpl.getCurrentUserId();
        Comment editComment = validatedCommentNotEmpty(commentId);
        if (currentUserId.equals(editComment.getUser().getId())) {
            editComment.updateComment(commentUpdateRequest.getContent());
            return CommentUpdateResponse.fromEntity(editComment);
        } else {
            throw new DisableEditCommentException();
        }
    }
    @Transactional
    public String deleteComment(Long commentId) {
        Long currentUserId = securityUtilImpl.getCurrentUserId();
        Comment deleteComment = validatedCommentNotEmpty(commentId);
        if (deleteComment.getUser().getId() == currentUserId) {
            commentRepository.deleteById(commentId);
            return commentId + "번 댓글이 삭제되었습니다.";
        } else {
            throw new DisableEditCommentException();
        }

    }

    private Comment validatedCommentNotEmpty(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
    }

    private Trip validatedTripNotEmpty(Long tripId) {
        return tripRepository.findById(tripId).orElseThrow(TripNotFoundException::new);
    }

    private User validatedUserNotEmpty() {
        Long currentUserId = securityUtilImpl.getCurrentUserId();
        return userRepository.getUserById(currentUserId);
    }
}
