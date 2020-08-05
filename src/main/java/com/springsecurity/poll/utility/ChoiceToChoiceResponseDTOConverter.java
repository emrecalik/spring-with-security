package com.springsecurity.poll.utility;

import com.springsecurity.poll.domain.Choice;
import com.springsecurity.poll.exception.ResourceNotFoundException;
import com.springsecurity.poll.model.ChoiceVoteCount;
import com.springsecurity.poll.model.response.ChoiceResponseDTO;

import java.util.List;

public class ChoiceToChoiceResponseDTOConverter {

    public static ChoiceResponseDTO convert(Choice choice, List<ChoiceVoteCount> choiceVoteCounts) {
        ChoiceResponseDTO choiceResponseDTO = new ChoiceResponseDTO();
        choiceResponseDTO.setId(choice.getId());
        choiceResponseDTO.setTitle(choice.getTitle());

        for (ChoiceVoteCount choiceVoteCount : choiceVoteCounts) {
            if (choiceVoteCount.getChoiceId().equals(choice.getId())) {
                choiceResponseDTO.setVoteCount(choiceVoteCount.getVoteCount());
                break;
            }
        }

        return choiceResponseDTO;
    }
}
