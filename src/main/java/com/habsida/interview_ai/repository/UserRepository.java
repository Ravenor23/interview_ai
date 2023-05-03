package com.habsida.interview_ai.repository;

import com.habsida.interview_ai.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u JOIN FETCH u.verificationCode v WHERE v.value = :code")
    Optional<User> findByCode(Long code);

    boolean existsByEmail(String email);
}
