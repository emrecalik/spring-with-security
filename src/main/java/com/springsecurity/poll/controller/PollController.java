package com.springsecurity.poll.controller;

import com.springsecurity.poll.domain.Poll;
import com.springsecurity.poll.domain.Vote;
import com.springsecurity.poll.model.request.PollRequestDTO;
import com.springsecurity.poll.model.request.VoteRequestDTO;
import com.springsecurity.poll.model.response.ApiResponseDTO;
import com.springsecurity.poll.model.response.PollResponseDTO;
import com.springsecurity.poll.security.MyUserPrincipal;
import com.springsecurity.poll.service.PollService;
import com.sun.security.auth.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/polls")
public class PollController {

    PollService pollService;

    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public List<PollResponseDTO> getAllPolls() {
        return pollService.getAllPolls();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createPoll(@Valid @RequestBody PollRequestDTO pollRequestDTO) {
        Poll pollSaved = pollService.createPoll(pollRequestDTO);

        UriComponents savedPollUri = UriComponentsBuilder.fromPath("/{pollId}")
                .buildAndExpand(pollSaved.getId());

        return ResponseEntity.created(savedPollUri.toUri()).body(
                new ApiResponseDTO(true, "Poll created successfully"));
    }

    @GetMapping("/{pollId}")
    @ResponseStatus(HttpStatus.FOUND)
    public PollResponseDTO getPollById(@PathVariable Long pollId) {
        return pollService.getPollById(pollId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping("/{pollId}/votes")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public PollResponseDTO castVote(@Valid @RequestBody VoteRequestDTO voteRequestDTO,
                                    @PathVariable Long pollId,
                                    @AuthenticationPrincipal MyUserPrincipal myUserPrincipal) {
        return pollService.castVote(voteRequestDTO, pollId, myUserPrincipal);
    }
}
