package com.habsida.interview_ai.service;

import com.habsida.interview_ai.model.Session;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SessionService {
    Page<Session> findAll(Integer pageNo, Integer pageSize, String sortBy);

    Page<Session> getSessionsByUserId(Long id, Integer pageNo, Integer pageSize, String sortBy);
    Session findById(Long id) throws Exception;
    Session updateSession(Long sessionId, Session session);
    Session startSession(String username, String language);
    void deleteSession(Long id);
//    Session addAnswerInSession(Long sessionId,Long questionId, MultipartFile file) throws Exception;
}
