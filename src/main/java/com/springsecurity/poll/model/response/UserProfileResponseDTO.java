package com.springsecurity.poll.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Builder
@Getter
@Setter
public class UserProfileResponseDTO {
    private Long id;

    private String name;

    private String userName;

    private String email;

    private Instant registeredAt;

    private Long pollCount;

    private Long voteCount;
}
