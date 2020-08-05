package com.springsecurity.poll.model.response;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserIdentityAvailabilityResponseDTO {
    private String message;
    private boolean available;
}
