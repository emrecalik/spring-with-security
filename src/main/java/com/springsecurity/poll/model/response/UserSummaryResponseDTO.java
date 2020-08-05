package com.springsecurity.poll.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSummaryResponseDTO {
    private Long id;

    private String name;

    private String userName;
}
