package com.habsida.interview_ai.service;

import com.habsida.interview_ai.model.VerificationCode;
import com.habsida.interview_ai.repository.VerificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VerificationService {
    private final VerificationRepository verificationRepository;

    public VerificationService(VerificationRepository verificationRepository) {
        this.verificationRepository = verificationRepository;
    }

    @Transactional
    public VerificationCode addVerificationCode(Long verificationCode) {
        VerificationCode code = new VerificationCode();
        code.setValue(verificationCode);
        return verificationRepository.save(code);
    }
}
