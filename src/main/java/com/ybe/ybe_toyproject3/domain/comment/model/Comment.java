package com.ybe.ybe_toyproject3.domain.comment.model;

import com.ybe.ybe_toyproject3.domain.trip.model.Trip;
import com.ybe.ybe_toyproject3.domain.user.model.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Comment(Long id, String content, Trip trip, User user) {
        this.id = id;
        this.content = content;
        this.trip = trip;
        this.user = user;
    }

    public void addUser(User user) {
        this.user = user;
    }

    public void addTrip(Trip trip) {
        this.trip = trip;
    }

    public void updateComment(String content) {
        this.content = content;
    }
}
