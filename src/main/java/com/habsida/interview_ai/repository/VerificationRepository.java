package com.habsida.interview_ai.repository;

import com.habsida.interview_ai.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationRepository extends JpaRepository<VerificationCode, Long> {
}
