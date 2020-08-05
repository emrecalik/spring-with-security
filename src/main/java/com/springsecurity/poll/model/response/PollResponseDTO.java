package com.springsecurity.poll.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PollResponseDTO {
    private Long id;

    private String question;

    private List<ChoiceResponseDTO> choices;

    private UserSummaryResponseDTO createdBy;

    private Instant createdAt;

    private Instant expiredAt;

    private Boolean isExpired;
}
