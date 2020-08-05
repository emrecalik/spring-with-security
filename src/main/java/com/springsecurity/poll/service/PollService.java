package com.springsecurity.poll.service;

import com.springsecurity.poll.domain.Choice;
import com.springsecurity.poll.domain.Poll;
import com.springsecurity.poll.domain.User;
import com.springsecurity.poll.domain.Vote;
import com.springsecurity.poll.exception.BadRequestException;
import com.springsecurity.poll.exception.ResourceNotFoundException;
import com.springsecurity.poll.model.ChoiceVoteCount;
import com.springsecurity.poll.model.request.PollRequestDTO;
import com.springsecurity.poll.model.request.VoteRequestDTO;
import com.springsecurity.poll.model.response.PollResponseDTO;
import com.springsecurity.poll.model.response.UserIdentityAvailabilityResponseDTO;
import com.springsecurity.poll.repository.PollRepository;
import com.springsecurity.poll.repository.UserRepository;
import com.springsecurity.poll.repository.VoteRepository;
import com.springsecurity.poll.security.MyUserPrincipal;
import com.springsecurity.poll.utility.PollToPollResponseDTOConverter;
import com.sun.security.auth.UserPrincipal;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PollService {

    PollRepository pollRepository;

    UserRepository userRepository;

    VoteRepository voteRepository;

    public PollService(PollRepository pollRepository, UserRepository userRepository, VoteRepository voteRepository) {
        this.pollRepository = pollRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    public List<PollResponseDTO> getAllPolls() {
        List<PollResponseDTO> pollResponseDTOS = new ArrayList<>();

        pollRepository.findAll().forEach(poll -> {
            User user = userRepository.findById(poll.getCreatedBy())
                    .orElseThrow(() -> new ResourceNotFoundException("Not Found User ID = " + poll.getCreatedBy()));

            List<ChoiceVoteCount> choiceVoteCounts = voteRepository.countByPollId(poll.getId());
            pollResponseDTOS.add(PollToPollResponseDTOConverter.convert(poll, user, choiceVoteCounts));
        });

        return pollResponseDTOS;
    }

    public Poll createPoll(PollRequestDTO pollRequestDTO) {
        Poll pollToSave = new Poll();
        pollToSave.setQuestion(pollRequestDTO.getQuestion());

        pollRequestDTO.getChoices().forEach(choiceRequestDTO -> {
            pollToSave.addChoice(new Choice(choiceRequestDTO.getTitle()));
        });

        pollToSave.setExpiredAt(Instant.now()
                .plus(Duration.ofDays(pollRequestDTO.getPollLength().getDays()))
                .plus(Duration.ofHours(pollRequestDTO.getPollLength().getHours())));

        return pollRepository.save(pollToSave);
    }

    public PollResponseDTO getPollById(Long pollId) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new ResourceNotFoundException("Poll Not Found ID = " + pollId));

        User user = userRepository.findById(poll.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("Resource Not Found ID = " + poll.getCreatedBy()));

        List<ChoiceVoteCount> choiceVoteCounts = voteRepository.countByPollId(pollId);

        return PollToPollResponseDTOConverter.convert(poll, user, choiceVoteCounts);
    }

    public PollResponseDTO castVote(VoteRequestDTO voteRequestDTO, Long pollId, MyUserPrincipal myUserPrincipal) {
        Poll poll = pollRepository.findById(pollId).orElseThrow(() ->
                new ResourceNotFoundException("Poll Not Found ID = " + pollId));

        if (poll.getExpiredAt().isBefore(Instant.now())) {
            throw new BadRequestException("Sorry! This poll has already been expired!");
        }

        User user = userRepository.getOne(myUserPrincipal.getId());

        Choice choiceToVote = poll.getChoices().stream()
                .filter(choice -> choice.getId().equals(pollId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Choice Not Found ID = " + voteRequestDTO.getChoiceId()));

        Vote vote = new Vote();
        vote.setPoll(poll);
        vote.setChoice(choiceToVote);
        vote.setUser(user);

        try {
            voteRepository.save(vote);
        } catch (DataIntegrityViolationException exc) {
            throw new BadRequestException("User has already voted in this poll!");
        }

        List<ChoiceVoteCount> choiceVoteCounts = voteRepository.countByPollId(pollId);

        return PollToPollResponseDTOConverter.convert(poll, user, choiceVoteCounts);
    }
}
