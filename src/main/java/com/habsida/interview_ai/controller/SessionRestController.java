package com.habsida.interview_ai.controller;

import com.habsida.interview_ai.model.Session;
import com.habsida.interview_ai.service.AnswerService;
import com.habsida.interview_ai.service.QuestionService;
import com.habsida.interview_ai.service.SessionService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Api(value = "Сессии", tags = {"Сесии"})
@RestController
@RequestMapping("/api/session")
public class SessionRestController {

    private final SessionService sessionService;

    public SessionRestController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Operation(
            summary = "Получение сессии по идентификатору пользователя"
    )
    @GetMapping("/user/{id}")
    public ResponseEntity<Page<Session>> getSessionsByUserId(@PathVariable @Parameter(description = "Идентификатор пользователя") Long id,
                                                           @RequestParam(defaultValue = "0") @Parameter(description = "Номер страницы") Integer pageNo,
                                                           @RequestParam(defaultValue = "10") @Parameter(description = "Размер страницы") Integer pageSize,
                                                           @RequestParam(defaultValue = "id") @Parameter(description = "Сортировка") String sortBy) {
        return ResponseEntity.ok().body(sessionService.getSessionsByUserId(id, pageNo, pageSize, sortBy));
    }

    @Operation(
            summary = "Получить все сессии"
    )
    @GetMapping
    public ResponseEntity<Page<Session>> findAllSessions(@RequestParam(defaultValue = "0") @Parameter(description = "Номер страницы") Integer pageNo,
                                                   @RequestParam(defaultValue = "10") @Parameter(description = "Размер страницы") Integer pageSize,
                                                   @RequestParam(defaultValue = "id") @Parameter(description = "Сортировка") String sortBy) {
        return ResponseEntity.ok().body(sessionService.findAll(pageNo, pageSize, sortBy));
    }

    @Operation(
            summary = "Получить сессию по идентификатору"
    )
    @GetMapping("/{id}")
    public ResponseEntity<Session> getSessionById(@PathVariable @Parameter(description = "Идентификатор сессии") Long id) throws Exception {
        return ResponseEntity.ok().body(sessionService.findById(id));
    }

    @Operation(
            summary = "Создание сессии"
    )
    @PostMapping("/start-session")
    public ResponseEntity<Session> startSession(@RequestParam("language") @Parameter(description = "Язык сессии") String language, Principal principal) {
        Session session = sessionService.startSession(principal.getName(), language);

        return ResponseEntity.ok().body(session);
    }

    @Operation(
            summary = "Обновление сессии"
    )
    @PutMapping("/{id}")
    public ResponseEntity<Session> updateSession(@PathVariable @Parameter(description = "Идетификатор сессии") Long sessionId,
                                                 @RequestBody @Parameter(description = "Сессия") Session session) {
        return ResponseEntity.ok().body(sessionService.updateSession(sessionId ,session));
    }

    @Operation(
            summary = "Удаление сессии"
    )
    @PostMapping("/{id}")
    public void deleteSession(@PathVariable @Parameter(description = "Идентификатор сессии") Long id) {
        sessionService.deleteSession(id);
    }

}
