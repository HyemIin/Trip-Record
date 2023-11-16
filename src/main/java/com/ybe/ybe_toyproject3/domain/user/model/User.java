package com.ybe.ybe_toyproject3.domain.user.model;

import com.ybe.ybe_toyproject3.domain.comment.model.Comment;
import com.ybe.ybe_toyproject3.domain.likes.model.Likes;
import com.ybe.ybe_toyproject3.domain.trip.model.Trip;
import com.ybe.ybe_toyproject3.global.common.Authority;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String email;
    private String name;
    private String password;
    @Column(name = "user_authority")
    @Enumerated(EnumType.STRING)
    protected Authority authority;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private final List<Trip> tripList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private final List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private final List<Likes> likesList = new ArrayList<>();

    @Builder
    public User(String email, String name, String password, Authority authority, Long id) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.authority = authority;
        //test위해 임시 추가
        this.id = id;
    }

    public void setId(Long userId) {
        this.id = userId;
    }
}
