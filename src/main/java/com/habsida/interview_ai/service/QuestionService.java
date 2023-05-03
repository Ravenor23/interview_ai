package com.habsida.interview_ai.service;

import com.habsida.interview_ai.model.File;
import com.habsida.interview_ai.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;

public interface QuestionService {
    Question findQuestionById(Long id) throws Exception;
    List<Question> find10RandomQuestionsByLanguage(String language);

    Page<Question> findAll(Integer pageNo, Integer pageSize, String sortBy);

    Question addQuestion(MultipartFile file, String text, String language);
    Question updateQuestion(Long id, Question question, String language);
    void deleteQuestionFromDb(Long id) throws FileNotFoundException;
    void deleteQuestion(Long id);
    File findFileByQuestionId(Long id, String language) throws Exception;
    Set<Question> findQuestionBySessionId(Long id);
    Page<Question> findQuestionByUserId(Long id, Integer pageNo, Integer pageSize, String sortBy);
}
