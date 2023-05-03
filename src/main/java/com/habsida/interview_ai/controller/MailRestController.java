package com.habsida.interview_ai.controller;

import com.habsida.interview_ai.exception.AnonymPassedException;
import com.habsida.interview_ai.exception.NotFoundException;
import com.habsida.interview_ai.model.Roles;
import com.habsida.interview_ai.model.User;
import com.habsida.interview_ai.service.EmailService;
import com.habsida.interview_ai.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@Api(value = "Почта", tags = {"Почта"})
@RestController
@RequestMapping("/api/mail")
public class MailRestController {
    @Value("${db.raw.password}")
    private String rawPassword;
    private final UserService userService;
    private final EmailService emailService;

    //TODO Rework URL
    private static final String URL = "localhost:8080";

    public MailRestController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @Operation(
            description = "Отправка письма для прохождения одной сессии"
    )
    @PostMapping("/token")
    public void sendToken(@RequestParam @Parameter(description = "Почта на которую будет выслан логин и пароль для прохождения 1 сессии") String email) {
        User user = userService.findUserByEmail(email);
        if (user != null) {
            throw new AnonymPassedException("Users without registration cannot pass more than once");
        }
        User userBuild = User.builder()
                .roles(Roles.ANONYMOUS)
                .email(email)
                .build();
        userService.addUser(userBuild);
        String emailText = "Ваш логин - " + email + " Ваш пароль - " + rawPassword;
        emailService.mailSender(email, "Login and Password", emailText);
    }

    @Operation(
            description = "Отправка одноразового кода для выдачи токена и смены пароля"
    )
    @PostMapping("/forgot-pass")
    public void refreshPassword(@RequestParam @Parameter(description = "Почта на которую будет выслан одноразовый код для смены пароля") String email) {

        User user = userService.findUserByEmail(email);
        if (user == null || user.getRoles() == Roles.ANONYMOUS) {
            throw new NotFoundException("User not found");
        }
        Long code = userService.addCodeToUser(email);
        emailService.mailSender(email, "Verification Code", code.toString());
    }
}
