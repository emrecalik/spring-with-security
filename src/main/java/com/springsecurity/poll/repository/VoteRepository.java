package com.springsecurity.poll.repository;

import com.springsecurity.poll.domain.Poll;
import com.springsecurity.poll.domain.Vote;
import com.springsecurity.poll.model.ChoiceVoteCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query("SELECT new com.springsecurity.poll.model.ChoiceVoteCount(v.choice.id, count(v.id)) " +
            "FROM Vote v " +
            "WHERE v.poll.id = :pollId " +
            "GROUP BY v.choice.id")
    List<ChoiceVoteCount> countByPollId(@Param("pollId") Long pollId);

    Long countByUserId(Long userId);

    @Query("SELECT v.poll.id FROM Vote v WHERE v.user.id = :userId")
    List<Long> findVotedPollIdsByUserId(@Param("userId") Long userId);
}
