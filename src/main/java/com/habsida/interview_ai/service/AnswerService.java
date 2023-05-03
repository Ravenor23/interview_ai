package com.habsida.interview_ai.service;

import com.habsida.interview_ai.model.Answer;
import com.habsida.interview_ai.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface AnswerService {
    Page<Answer> findAllAnswer(Integer pageNo, Integer pageSize, String sortBy);
    Answer findAnswerById(Long id) throws Exception;
    Answer addAnswer(Long sessionId, Long questionId, MultipartFile file) throws Exception;
    Answer updateAnswer(Long id, MultipartFile file) throws Exception;
    void deleteAnswer(Long id);
    Page<Answer> findAnswerByUserId(Long userId, Integer pageNo, Integer pageSize, String sortBy);
    Page<Answer> findAnswerByQuestionId(Long questionId, Integer pageNo, Integer pageSize, String sortBy);
    Page<Answer> findAnswerByUserIdAndQuestionId(Long userId, Long questionId, Integer pageNo, Integer pageSize, String sortBy);
}
