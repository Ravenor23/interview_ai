package com.habsida.interview_ai.service;

import com.habsida.interview_ai.exception.NotFoundException;
import com.habsida.interview_ai.model.File;
import com.habsida.interview_ai.model.Question;
import com.habsida.interview_ai.repository.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Set;

@Service

public class QuestionServiceImp implements QuestionService {

    private final QuestionRepository questionRepository;
    private final FileService fileService;
    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionServiceImp.class);

    public QuestionServiceImp(QuestionRepository questionRepository, FileService fileService) {
        this.questionRepository = questionRepository;
        this.fileService = fileService;
    }

    @Transactional(readOnly = true)
    @Override
    public Question findQuestionById(Long id) {
        LOGGER.info(id.toString());

        return questionRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Question not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Question> find10RandomQuestionsByLanguage(String language) {
        LOGGER.info(language);
        Pageable pageable = PageRequest.of(0, 10);

        switch (language) {
            case "eng" -> {
                return questionRepository.find10RandomEnglishQuestions(pageable);
            }
            case "kor" -> {
                return questionRepository.find10RandomKoreanQuestions(pageable);
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Question> findAll(Integer pageNo, Integer pageSize, String sortBy) {
        LOGGER.info(pageNo.toString(), pageSize.toString(), sortBy);

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return questionRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Question addQuestion(MultipartFile file, String text, String language) {
        LOGGER.info(file.toString(), text, language);

        File fileQuestion = fileService.addFile(file, "");
        Question question = null;
        switch (language) {
            case "eng" -> {
                question = Question.builder()
                        .englishText(text)
                        .build();
                question.setEnglishFile(fileQuestion);
            }
            case "kor" -> {
                question = Question.builder()
                        .koreanText(text)
                        .build();
                question.setKoreanFile(fileQuestion);
            }
        }
        return questionRepository.save(question);
    }

    @Transactional
    @Override
    public Question updateQuestion(Long id, Question question, String language) {
        LOGGER.info(id.toString(), question.toString());

        Question questionToUpdate = this.findQuestionById(id);
        switch (language) {
            case "eng" -> {
                if (question.getEnglishText() != null && !question.getEnglishText().equals("")) {
                    questionToUpdate.setEnglishText(question.getEnglishText());
                }
                if (question.getEnglishFile() != null && !question.getEnglishFile().toString().equals("")) {
                    questionToUpdate.setEnglishFile(question.getEnglishFile());
                }
            }
            case "kor" -> {
                if (question.getKoreanText() != null && !question.getKoreanText().equals("")) {
                    questionToUpdate.setKoreanText(question.getKoreanText());
                }
                if (question.getKoreanFile() != null && !question.getKoreanFile().toString().equals("")) {
                    questionToUpdate.setKoreanFile(question.getKoreanFile());
                }
            }
        }
        return questionRepository.saveAndFlush(questionToUpdate);
    }

    //НЕ ТРОГАТЬ
    //ОПАСНО
    //ЕКАТЕРИНА ПОКАРАЕТ
    @Transactional
    @Override
    public void deleteQuestionFromDb(Long id) {
        LOGGER.info(id.toString());

        Question questionToDelete = this.findQuestionById(id);
        questionRepository.deleteById(id);
        fileService.deleteFileFromBucket(questionToDelete.getEnglishFile().getShortPath());
        fileService.deleteFile(questionToDelete.getEnglishFile().getId());
        fileService.deleteFileFromBucket(questionToDelete.getKoreanFile().getShortPath());
        fileService.deleteFile(questionToDelete.getKoreanFile().getId());
    }

    @Transactional
    @Override
    public void deleteQuestion(Long id) {
        LOGGER.info(id.toString());

        if (this.findQuestionById(id) != null){
            questionRepository.deleteById(id);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public com.habsida.interview_ai.model.File findFileByQuestionId(Long id, String language) {
        LOGGER.info(id.toString());

        File file = null;
        switch (language) {
            case "eng" -> file = this.findQuestionById(id).getEnglishFile();
            case "kor" -> file = this.findQuestionById(id).getKoreanFile();
        }

        return file;
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Question> findQuestionBySessionId(Long id) {
        LOGGER.info(id.toString());

        return questionRepository.findBySessionId(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Question> findQuestionByUserId(Long id, Integer pageNo, Integer pageSize, String sortBy) {
        LOGGER.info(id.toString(), pageNo.toString(), pageSize.toString(), sortBy);

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return questionRepository.findByUserId(id, pageable);
    }
}
