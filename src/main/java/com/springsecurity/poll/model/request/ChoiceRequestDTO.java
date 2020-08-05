package com.springsecurity.poll.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class ChoiceRequestDTO {
    @NotBlank
    @Size(min = 2, max = 100)
    private String title;
}
