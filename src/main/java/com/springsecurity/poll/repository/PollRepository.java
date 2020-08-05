package com.springsecurity.poll.repository;

import com.springsecurity.poll.domain.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PollRepository extends JpaRepository<Poll, Long> {
    Optional<Poll> findById(Long pollId);

    long countByCreatedBy(Long userId);

    Optional<Poll> findAllByCreatedBy(Long userId);

    List<Poll> findAllByIdIn(List<Long> pollIds);
}
