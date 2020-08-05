package com.springsecurity.poll.domain;

import com.springsecurity.poll.domain.audit.UserDateAudit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Poll extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 4, max = 100)
    private String question;

    @OneToMany(mappedBy = "poll",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            targetEntity = Choice.class,
            orphanRemoval = true)
    private List<Choice> choices = new ArrayList<>();

    @NotNull
    private Instant expiredAt;

    public void addChoice(Choice choice) {
        this.choices.add(choice);
        choice.setPoll(this);
    }
}
