package com.habsida.interview_ai.controller;

import com.habsida.interview_ai.exception.AnonymPassedException;
import com.habsida.interview_ai.jwt.JwtAuthenticationRequest;
import com.habsida.interview_ai.jwt.JwtTokenProvider;
import com.habsida.interview_ai.model.Roles;
import com.habsida.interview_ai.model.Session;
import com.habsida.interview_ai.model.User;
import com.habsida.interview_ai.service.SessionService;
import com.habsida.interview_ai.service.UserService;
import io.jsonwebtoken.JwtException;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "Авторизация", tags = {"Авторизация"})
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final SessionService sessionService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService, SessionService sessionService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @Operation(
            description = "Авторизация пользователя с выдачей токена"
    )
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Parameter(description = "почта и пароль") JwtAuthenticationRequest authenticationRequest) {
        try {
            String email = authenticationRequest.getEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, authenticationRequest.getPassword()));
            User user = userService.findUserByEmail(email);

            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            } else if (user.getRoles() == Roles.ANONYMOUS){
                List<Session> sessionList = sessionService.getSessionsByUserId(user.getId(), 0, 10, "id").getContent();
                if (sessionList.size() != 0) {
                    throw new AnonymPassedException("Users without registration cannot pass more than once");
                }
            }

            String token = jwtTokenProvider.createToken(email, user.getRoles());
            Map<Object, Object> responce = new HashMap<>();
            responce.put("email", email);
            responce.put("token", token);
            return ResponseEntity.ok().body(responce);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Operation(
            description = "Обновление пароля пользователя"
    )
    @PostMapping("/refresh")
    public ResponseEntity<User> refreshPassword(@RequestParam @Parameter(description = "Токен для обновления пароля") String token,
                                                @RequestParam @Parameter(description = "Новый пароль") String newPassword) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new JwtException("Jwt token is expired or invalid");
        }
        String email = jwtTokenProvider.getEmail(token);
        User user = userService.findUserByEmail(email);
        user.setPassword(newPassword);
        return ResponseEntity.ok().body(userService.updateUser(user.getId(), user));
    }

    @Operation(
            description = "Выдача короткоживущего токена для смены пароля"
    )
    @PostMapping("/code")
    public ResponseEntity<String> createTokenByCode(@RequestParam @Parameter(description = "Проверочный код из письма для выдачи токена") Long code) {
        User user = userService.findUserByCode(code);
        return ResponseEntity.ok().body(jwtTokenProvider.createShortToken(user.getEmail(), user.getRoles()));
    }

    @Operation(
            description = "Регистрация"
    )
    @PostMapping("/registration")
    public ResponseEntity<User> registration(@RequestBody @Parameter(description = "пользователь") User user) {
        return ResponseEntity.ok().body(userService.addUser(user));
    }
}
