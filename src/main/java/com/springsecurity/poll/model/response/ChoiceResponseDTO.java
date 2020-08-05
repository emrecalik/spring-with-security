package com.springsecurity.poll.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChoiceResponseDTO {
    private Long id;

    private String title;

    private Long voteCount = 0L;
}
