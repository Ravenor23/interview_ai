package com.habsida.interview_ai.controller;

import com.habsida.interview_ai.model.User;
import com.habsida.interview_ai.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Api(value = "Пользователи", tags = {"Пользователи"})
@RestController
@RequestMapping("/api/user")
public class UserRestController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Получение пользователя по идентификатору"
    )
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable @Parameter(description = "Идентификатор пользователя") Long id) throws Exception {
        return ResponseEntity.ok().body(userService.findUserById(id));
    }

    @Operation(
            summary = "Получение всех пользователей"
    )
    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(@RequestParam(defaultValue = "0") @Parameter(description = "Номер страницы") Integer pageNo,
                                                  @RequestParam(defaultValue = "10") @Parameter(description = "Размер страницы") Integer pageSize,
                                                  @RequestParam(defaultValue = "id") @Parameter(description = "Сортировка") String sortBy){
        Page<User> userPage = userService.userList(pageNo, pageSize, sortBy);
        return ResponseEntity.ok().body(userPage);
    }

    @Operation(
            summary = "Получение авторизованного пользователя"
    )
    @GetMapping("/authUser")
    public UserDetails getUserInfo(Principal principal) {
        return userService.findUserByEmail(principal.getName());
    }

    @Operation(
            summary = "Создание пользователя"
    )
    @PostMapping()
    public ResponseEntity<User> createUser(@RequestBody @Parameter(description = "Пользователя") User user){
       return ResponseEntity.ok().body(userService.addUser(user));
    }

    @Operation(
            summary = "Обновление пользователя"
    )
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable @Parameter(description = "Идентификатор пользователя") Long userId,
                                           @RequestBody @Parameter(description = "Пользотваеля") User user) {
        return ResponseEntity.ok().body(userService.updateUser(userId, user));
    }

    @Operation(
            summary = "Удаление пользователя"
    )
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable ("id") @Parameter(description = "Идентификатор пользователя") Long id) {
        userService.deleteUserById(id);
    }

    @Operation(
            summary = "Return User by email"
    )
    @GetMapping("/email")
    public ResponseEntity<User> getUserByEmail(@RequestParam @Parameter(description = "inbox") String email) {
        return ResponseEntity.ok().body(userService.findUserByEmail(email));
    }

}
