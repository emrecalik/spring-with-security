package com.springsecurity.poll.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class PollLengthRequestDTO {

    @NotNull
    @Max(7)
    private int days;

    @NotNull
    @Max(23)
    private int hours;
}
