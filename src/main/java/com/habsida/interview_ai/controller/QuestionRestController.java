package com.habsida.interview_ai.controller;

import com.habsida.interview_ai.model.Question;
import com.habsida.interview_ai.service.QuestionService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;

@Api(value = "Вопросы", tags = {"Вопросы"})
@RestController
@RequestMapping("/api/question")
public class QuestionRestController {
    private final QuestionService questionService;

    public QuestionRestController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Operation(
            summary = "Получени вопроса по идентификатору"
    )
    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable @Parameter(description = "Идентификатор вопроса") Long id) throws Exception {
        Question q = questionService.findQuestionById(id);
        return ResponseEntity.ok().body(q);
    }

    @Operation(
            summary = "Нахождение всех вопросов"
    )
    @GetMapping
    public ResponseEntity<Page<Question>> findAllQuestions(@RequestParam(defaultValue = "0") @Parameter(description = "Номер страницы") Integer pageNo,
                                                           @RequestParam(defaultValue = "10") @Parameter(description = "Размер страницы") Integer pageSize,
                                                           @RequestParam(defaultValue = "id") @Parameter(description = "Сортировка") String sortBy) {
        return ResponseEntity.ok().body(questionService.findAll(pageNo, pageSize, sortBy));
    }

    @Operation(
            summary = "Получение 10 случайных вопросов по языку"
    )
    @GetMapping(value = "/by-language")
    public ResponseEntity<List<Question>> getListByLanguage(@RequestParam @Parameter(description = "Язык получения вопросов") String language) {
        return ResponseEntity.ok().body(questionService.find10RandomQuestionsByLanguage(language));
    }

    @Operation(
            summary = "Найти все вопросы по сессии"
    )
    @GetMapping("/session/{id}")
    public ResponseEntity<Set<Question>> findQuestionsBySessionId(@PathVariable @Parameter(description = "Идентификатор сессии") Long id) {
        return ResponseEntity.ok().body(questionService.findQuestionBySessionId(id));
    }

    @Operation(
            summary = "Найти вопросы по пользователю"
    )
    @GetMapping("/user/{id}")
    public ResponseEntity<Page<Question>> findQuestionByUserId(@PathVariable @Parameter(description = "Идентификатор пользоваткеля") Long id,
                                                               @RequestParam(defaultValue = "0") @Parameter(description = "") Integer pageNo,
                                                               @RequestParam(defaultValue = "10") @Parameter(description = "") Integer pageSize,
                                                               @RequestParam(defaultValue = "id") @Parameter(description = "") String sortBy) {
        return ResponseEntity.ok().body(questionService.findQuestionByUserId(id, pageNo, pageSize, sortBy));
    }

    @Operation(
            summary = "Создание вопроса"
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Question> createQuestion(@RequestPart("file") @Parameter(description = "Звуковая дорожка") MultipartFile file,
                                                   @RequestParam("text") @Parameter(description = "Текст вопроса") String text,
                                                   @RequestParam("language") @Parameter(description = "Язык вопроса") String language) {
        return ResponseEntity.ok().body(questionService.addQuestion(file, text, language));
    }

    @Operation(
            summary = "Обновление вопроса"
    )
    @PutMapping(value = "/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable @Parameter(description = "Идентификатор вопроса") Long id,
                                                   @RequestParam @Parameter(description = "Язык вопроса") String language,
                                                   @RequestBody @Parameter(description = "Вопрос") Question question){
        return ResponseEntity.ok().body(questionService.updateQuestion(id, question, language));
    }

    @Operation(
            summary = "Удаление вопроса из базы данных и бакета"
    )
    @DeleteMapping("/delete/{id}")
    public void deleteQuestionFromDb(@PathVariable("id") @Parameter(description = "Идентификатор вопроса") Long id) throws FileNotFoundException {
        questionService.deleteQuestionFromDb(id);
    }

    @Operation(
            summary = "Удаление вопроса"
    )
    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable("id") @Parameter(description = "Идентификатор вопроса") Long id) {
        questionService.deleteQuestion(id);
    }

}
