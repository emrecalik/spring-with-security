package com.springsecurity.poll.utility;

import com.springsecurity.poll.domain.Poll;
import com.springsecurity.poll.domain.User;
import com.springsecurity.poll.model.ChoiceVoteCount;
import com.springsecurity.poll.model.response.ChoiceResponseDTO;
import com.springsecurity.poll.model.response.PollResponseDTO;
import com.springsecurity.poll.model.response.UserSummaryResponseDTO;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class PollToPollResponseDTOConverter {

    public static PollResponseDTO convert(Poll poll, User createdByUser, List<ChoiceVoteCount> choiceVoteCounts) {
        PollResponseDTO pollResponseDTO = new PollResponseDTO();
        pollResponseDTO.setId(poll.getId());
        pollResponseDTO.setQuestion(poll.getQuestion());
        pollResponseDTO.setCreatedAt(poll.getCreatedAt());
        pollResponseDTO.setIsExpired(poll.getExpiredAt().isBefore(Instant.now()));
        pollResponseDTO.setExpiredAt(poll.getExpiredAt());

        UserSummaryResponseDTO userSummaryResponseDTO = new UserSummaryResponseDTO();
        userSummaryResponseDTO.setId(createdByUser.getId());
        userSummaryResponseDTO.setName(createdByUser.getName());
        userSummaryResponseDTO.setUserName(createdByUser.getUserName());
        pollResponseDTO.setCreatedBy(userSummaryResponseDTO);

        List<ChoiceResponseDTO> choiceResponseDTOs = new ArrayList<>();
        poll.getChoices().forEach(choice -> choiceResponseDTOs.add(ChoiceToChoiceResponseDTOConverter.convert(choice, choiceVoteCounts)));
        pollResponseDTO.setChoices(choiceResponseDTOs);

        return pollResponseDTO;
    }
}
