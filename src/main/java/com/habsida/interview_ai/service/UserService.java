package com.habsida.interview_ai.service;

import com.habsida.interview_ai.model.User;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    User addUser(User user);
    Page<User> userList(Integer pageNo, Integer pageSize, String sortBy);
    User findUserById(Long id) throws Exception;
    void deleteUserById(Long id);
    User updateUser (Long userId, User userUpdated);
    User findUserByEmail(String email);
    long addCodeToUser(String email);
    User findUserByCode(Long code);
}
