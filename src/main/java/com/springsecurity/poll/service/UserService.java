package com.springsecurity.poll.service;

import com.springsecurity.poll.domain.Poll;
import com.springsecurity.poll.domain.User;
import com.springsecurity.poll.exception.ResourceNotFoundException;
import com.springsecurity.poll.model.ChoiceVoteCount;
import com.springsecurity.poll.model.response.PollResponseDTO;
import com.springsecurity.poll.model.response.UserIdentityAvailabilityResponseDTO;
import com.springsecurity.poll.model.response.UserProfileResponseDTO;
import com.springsecurity.poll.repository.PollRepository;
import com.springsecurity.poll.repository.UserRepository;
import com.springsecurity.poll.repository.VoteRepository;
import com.springsecurity.poll.utility.PollToPollResponseDTOConverter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    UserRepository userRepository;

    PollRepository pollRepository;

    VoteRepository voteRepository;

    public UserService(UserRepository userRepository,
                       PollRepository pollRepository,
                       VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.pollRepository = pollRepository;
        this.voteRepository = voteRepository;
    }

    public UserIdentityAvailabilityResponseDTO checkUserNameAvailability(String userName) {
        boolean doesExist = userRepository.existsByUserName(userName);
        return UserIdentityAvailabilityResponseDTO.builder()
                .message("Username Check = " + userName)
                .available(!doesExist)
                .build();
    }

    public UserIdentityAvailabilityResponseDTO checkEmailAvailability(String email) {
        boolean doesExist = userRepository.existsByEmail(email);
        return UserIdentityAvailabilityResponseDTO.builder()
                .message("Email Check = " + email)
                .available(!doesExist)
                .build();
    }

    public UserProfileResponseDTO getUserProfile(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found UserName = " + userName));

        long pollCount = pollRepository.countByCreatedBy(user.getId());
        long voteCount = voteRepository.countByUserId(user.getId());

        return UserProfileResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .userName(user.getUserName())
                .email(user.getEmail())
                .registeredAt(user.getCreatedAt())
                .pollCount(pollCount)
                .voteCount(voteCount)
                .build();
    }

    public List<PollResponseDTO> getPollsCreatedBy(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found Username = " + userName));

        List<PollResponseDTO> pollResponseDTOs = new ArrayList<>();

        pollRepository.findAllByCreatedBy(user.getId()).ifPresent(poll -> {
            List<ChoiceVoteCount> choiceVoteCounts = voteRepository.countByPollId(poll.getId());
            pollResponseDTOs.add(PollToPollResponseDTOConverter.convert(poll, user, choiceVoteCounts));
        });

        return pollResponseDTOs;
    }

    public List<PollResponseDTO> getPollsVotedBy(String userName) {

        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found Username = " + userName));

        List<Long> votedPollIds = voteRepository.findVotedPollIdsByUserId(user.getId());

        List<Poll> pollsVoted = pollRepository.findAllByIdIn(votedPollIds);

        List<PollResponseDTO> pollResponseDTOs = new ArrayList<>();

        pollsVoted.forEach(poll -> {
            User pollCreatorUser = userRepository.findById(poll.getCreatedBy())
                    .orElseThrow(() -> new ResourceNotFoundException("Not Found User ID = " + poll.getCreatedBy()));

            List<ChoiceVoteCount> choiceVoteCounts = voteRepository.countByPollId(poll.getId());
            pollResponseDTOs.add(PollToPollResponseDTOConverter.convert(poll, pollCreatorUser, choiceVoteCounts));
        });

        return pollResponseDTOs;
    }
}
