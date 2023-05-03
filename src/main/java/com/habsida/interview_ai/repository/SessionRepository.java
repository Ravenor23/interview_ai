package com.habsida.interview_ai.repository;

import com.habsida.interview_ai.model.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    @EntityGraph("Session.answer")
    Optional<Session> findById(Long id);

    @EntityGraph("Session.answer")
    Page<Session> findAll(Pageable pageable);

    @EntityGraph("Session.answer")
    @Query("SELECT s FROM Session s WHERE s.user.id = :userId")
    Page<Session> findSessionsByUserId(Long userId, Pageable pageable);
}
