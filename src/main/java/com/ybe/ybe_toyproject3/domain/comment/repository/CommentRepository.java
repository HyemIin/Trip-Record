package com.ybe.ybe_toyproject3.domain.comment.repository;

import com.ybe.ybe_toyproject3.domain.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findAllByUserId(Long user_id);
}
