package com.ybe.ybe_toyproject3.domain.trip.model;

import com.ybe.ybe_toyproject3.domain.comment.model.Comment;
import com.ybe.ybe_toyproject3.domain.itinerary.model.Itinerary;
import com.ybe.ybe_toyproject3.domain.likes.model.Likes;
import com.ybe.ybe_toyproject3.domain.user.model.User;
import com.ybe.ybe_toyproject3.global.common.type.TripType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Trip {
    @Id
    @Column(name = "trip_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tripName;
    private LocalDateTime tripStartDate;
    private LocalDateTime tripEndDate;
    @Enumerated(EnumType.STRING)
    private TripType tripType;

    // @JsonManagedReference
    @OneToMany(mappedBy = "trip", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private final List<Itinerary> itineraryList = new ArrayList<>();

    @OneToMany(mappedBy = "trip", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private final List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "trip", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private final List<Likes> likesList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Trip(String tripName, LocalDateTime tripStartDate, LocalDateTime tripEndDate, TripType tripType) {
        this.tripName = tripName;
        this.tripStartDate = tripStartDate;
        this.tripEndDate = tripEndDate;
        this.tripType = tripType;
    }
}