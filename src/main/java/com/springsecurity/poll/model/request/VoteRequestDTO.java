package com.springsecurity.poll.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class VoteRequestDTO {
    @NotBlank
    private Long choiceId;
}
