package com.habsida.interview_ai.controller;

import com.habsida.interview_ai.model.Answer;
import com.habsida.interview_ai.service.AmazonS3Service;
import com.habsida.interview_ai.service.AnswerService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(value = "Ответы", tags = {"Ответы"})
@RequestMapping("/api/answer")
public class AnswerRestController {
    private final AnswerService answerService;
    private final AmazonS3Service amazonS3Service;

    public AnswerRestController(AnswerService answerService, AmazonS3Service amazonS3Service) {
        this.answerService = answerService;
        this.amazonS3Service = amazonS3Service;
    }

    @Operation(
            summary = "Возвращение ответа по идентификатору"
    )
    @GetMapping("/{id}")
    public ResponseEntity<Answer> getAnswerById(@PathVariable @Parameter(description = "Идентификатор вопроса") Long id) throws Exception {
        return ResponseEntity.ok().body(answerService.findAnswerById(id));
    }

    @Operation(
            summary = "Получение всех ответов",
            description = "Возваращает объект Page с возможностью выбора количества объектов на странице и типу сортировки"
    )
    @GetMapping
    public ResponseEntity<Page<Answer>> findAllAnswers(@RequestParam(defaultValue = "0") @Parameter(description = "Номер страницы") Integer pageNo,
                                                       @RequestParam(defaultValue = "10") @Parameter(description = "Размер страницы") Integer pageSize,
                                                       @RequestParam(defaultValue = "id") @Parameter(description = "Тип сортировки") String sortBy){
        return ResponseEntity.ok().body(answerService.findAllAnswer(pageNo, pageSize, sortBy));
    }

    @Operation(
            summary = "Возвращение всех ответов по идентификатору вопроса",
            description = "Возваращает объект Page с возможностью выбора количества объектов на странице и типу сортировки"
    )
    @GetMapping("/question/{id}")
    public ResponseEntity<Page<Answer>> getAnswerByQuestionId(@PathVariable @Parameter(description = "Идентификатор вопроса") Long id,
                                                        @RequestParam(defaultValue = "0") @Parameter(description = "Номер страницы") Integer pageNo,
                                                        @RequestParam(defaultValue = "10") @Parameter(description = "Размер страницы") Integer pageSize,
                                                        @RequestParam(defaultValue = "id") @Parameter(description = "Тип сортировки") String sortBy) {
        return ResponseEntity.ok().body(answerService.findAnswerByQuestionId(id, pageNo, pageSize, sortBy));
    }

    @Operation(
            summary = "Возвращение всех ответов пользоветаля",
            description = "Возваращает объект Page с возможностью выбора количества объектов на странице и типу сортировки"
    )
    @GetMapping("/user/{id}")
    public ResponseEntity<Page<Answer>> getAnswerByUserId(@PathVariable @Parameter(description = "Идентификатор вопроса") Long id,
                                                              @RequestParam(defaultValue = "0") @Parameter(description = "Номер страницы") Integer pageNo,
                                                              @RequestParam(defaultValue = "10") @Parameter(description = "Размер страницы") Integer pageSize,
                                                              @RequestParam(defaultValue = "id") @Parameter(description = "Тип сортировки") String sortBy) {
        return ResponseEntity.ok().body(answerService.findAnswerByUserId(id, pageNo, pageSize, sortBy));
    }

    @Operation(
            summary = "Получение всех ответов пользователя на определённый вопрос",
            description = "Возваращает объект Page с возможностью выбора количества объектов на странице и типу сортировки"
    )
    @GetMapping("/question/{questionId}/user/{userId}")
    public ResponseEntity<Page<Answer>> getAnswerByQuestionAndUserId(@PathVariable @Parameter(description = "Идентификатор вопроса") Long questionId,
                                                              @PathVariable @Parameter(description = "Идентификатор пользователя") Long userId,
                                                              @RequestParam(defaultValue = "0") @Parameter(description = "Номер страницы") Integer pageNo,
                                                              @RequestParam(defaultValue = "10") @Parameter(description = "Размер страницы") Integer pageSize,
                                                              @RequestParam(defaultValue = "id") @Parameter(description = "Тип сортировки") String sortBy) {
        return ResponseEntity.ok().body(answerService.findAnswerByUserIdAndQuestionId(questionId, userId, pageNo, pageSize, sortBy));
    }

    @Operation(
            summary = "Создание ответа"
    )
    @PostMapping
    public ResponseEntity<Answer> createAnswer(@RequestParam @Parameter(description = "Звуковая дорожка с ответом") MultipartFile answer,
                                               @Parameter (description = "Идентификатор сессии") Long sessionId,
                                               @Parameter(description = "Идентификатор вопроса") Long questionId) throws Exception {
        return ResponseEntity.ok(answerService.addAnswer(sessionId, questionId, answer));
    }

    @Operation(
            summary = "Обновление ответа"
    )
    @PutMapping("/{id}")
    public ResponseEntity<Answer> updateAnswer(@PathVariable @Parameter(description = "Идентификатор вопроса") Long id,
                                               @RequestParam @Parameter(description = "Звуковая дорожка с ответом") MultipartFile file) throws Exception {
        return ResponseEntity.ok(answerService.updateAnswer(id, file));
    }

    @Operation(
            summary = "Удаление ответа"
    )
    @DeleteMapping("/{id}")
    public void deleteAnswer(@PathVariable @Parameter(description = "Идентификатор вопроса") Long id) throws Exception {
        amazonS3Service.deleteFile(answerService.findAnswerById(id).getFile().getPath());
        answerService.deleteAnswer(id);
    }
}
