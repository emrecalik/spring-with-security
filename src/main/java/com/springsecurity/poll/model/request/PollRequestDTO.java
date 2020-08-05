package com.springsecurity.poll.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PollRequestDTO {

    @NotBlank
    @Size(min = 4, max = 100)
    private String question;

    @NotBlank
    @Size(min = 2, max = 6)
    private List<ChoiceRequestDTO> choices;

    @NotBlank
    private PollLengthRequestDTO pollLength;
}
