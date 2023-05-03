package com.habsida.interview_ai.service;

import com.habsida.interview_ai.exception.NotFoundException;
import com.habsida.interview_ai.model.Answer;
import com.habsida.interview_ai.model.File;
import com.habsida.interview_ai.repository.AnswerRepository;
import com.habsida.interview_ai.repository.QuestionRepository;
import com.habsida.interview_ai.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnswerServiceImp implements AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final SessionRepository sessionRepository;
    private final FileService fileService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AnswerServiceImp.class);

    @Transactional(readOnly = true)
    @Override
    public Page<Answer> findAllAnswer(Integer pageNo, Integer pageSize, String sortBy) {
        LOGGER.info(pageNo.toString(), pageSize.toString(), sortBy);

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return answerRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Answer findAnswerById(Long id) {
        LOGGER.info(id.toString());

        return answerRepository.findById(id).orElseThrow(()-> new NotFoundException("Answer not found"));
    }

    @Transactional
    @Override
    public Answer addAnswer(Long sessionId, Long questionId, MultipartFile file) throws Exception {
        LOGGER.info(file.toString());

        Answer answer = new Answer();
        if (file != null) {
            String hashName = "Session_" + sessionId + "__Question_" + questionId + "__Date_" + LocalDateTime.now();
            File newFile = fileService.addFile(file, hashName);
            answer.setFile(newFile);
        }

        answer.setQuestion(questionRepository.findById(questionId).orElseThrow(()-> new NotFoundException("Question not found")));
        answer.setSession(sessionRepository.findById(sessionId).orElseThrow(()-> new NotFoundException("Session not found")));
        return answerRepository.save(answer);
    }

    @Transactional
    @Override
    public Answer updateAnswer(Long id, MultipartFile file) {
        LOGGER.info(id.toString(), file.toString());

        Answer answerToUpdate = this.findAnswerById(id);
        if (file != null) {
            File file1 = fileService.findFileById(answerToUpdate.getFile().getId());
            File newFile = fileService.addFile(file, file1.getShortPath());
            answerToUpdate.setFile(newFile);
        }
        return answerRepository.saveAndFlush(answerToUpdate);
    }

    @Transactional
    @Override
    public void deleteAnswer(Long id) {
        LOGGER.info(id.toString());

        if (this.findAnswerById(id) != null) {
            answerRepository.deleteById(id);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Answer> findAnswerByUserId(Long userId, Integer pageNo, Integer pageSize, String sortBy) {
        LOGGER.info(userId.toString(), pageNo.toString(), pageSize.toString(), sortBy);

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return answerRepository.findByUserId(userId, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Answer> findAnswerByQuestionId(Long questionId, Integer pageNo, Integer pageSize, String sortBy) {
        LOGGER.info(questionId.toString(), pageNo.toString(), pageSize.toString(), sortBy);

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return answerRepository.findByQuestionId(questionId, pageable);

    }

    @Transactional(readOnly = true)
    @Override
    public Page<Answer> findAnswerByUserIdAndQuestionId(Long userId, Long questionId, Integer pageNo, Integer pageSize, String sortBy) {
        LOGGER.info(userId.toString(), questionId.toString(), pageNo.toString(), pageSize.toString(), sortBy);

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return answerRepository.findByUserAndQuestionId(userId, questionId, pageable);
    }
}
