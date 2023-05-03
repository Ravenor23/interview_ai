package com.habsida.interview_ai.service;

import com.habsida.interview_ai.exception.NotFoundException;
import com.habsida.interview_ai.exception.UserExistException;
import com.habsida.interview_ai.model.Roles;
import com.habsida.interview_ai.model.User;
import com.habsida.interview_ai.model.VerificationCode;
import com.habsida.interview_ai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserServiceImp implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationService verificationService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImp.class);
    @Value("${db.raw.password}")
    private String rawPassword;

//    public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder, VerificationService verificationService) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.verificationService = verificationService;
//    }

    @Transactional
    @Override
    public User addUser(User user) {
        LOGGER.info(user.toString());

        if (userRepository.existsByEmail(user.getEmail())) {
            User userFromDb = userRepository.findByEmail(user.getEmail()).orElseThrow(()->new NotFoundException("User not found"));
            if (userFromDb.getRoles().equals(Roles.ANONYMOUS)) {
                userFromDb.setPassword(passwordEncoder.encode(user.getPassword()));
                userFromDb.setRoles(Roles.USER);
                return this.updateUser(userFromDb.getId(), userFromDb);
//            }
//            if (userFromDb.getPassword().equals(passwordEncoder.encode(rawPassword))) {
//                userFromDb.setPassword(passwordEncoder.encode(user.getPassword()));
//                userFromDb.setRoles(Roles.USER);
//                return this.updateUser(userFromDb.getId(), userFromDb);
            } else {
                throw new UserExistException("User exists");
            }
        } else if (user.getPassword() == null) {
            user.setPassword(passwordEncoder.encode(rawPassword));
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Roles.USER);
        }

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<User> userList(Integer pageNo, Integer pageSize, String sortBy) {
        LOGGER.info(pageNo.toString(), pageSize.toString(), sortBy);

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return userRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public User findUserById(Long id) {
        LOGGER.info(id.toString());

        return userRepository.findById(id).orElseThrow(()->new NotFoundException("User not found"));
    }

    @Transactional
    @Override
    public void deleteUserById(Long id) {
        LOGGER.info(id.toString());

        if (this.findUserById(id) != null) {
            userRepository.deleteById(id);
        }
    }

    @Transactional
    @Override
    public User updateUser(Long userId, User userUpdated) {
        LOGGER.info(userId.toString(), userUpdated.toString());

        User userToUpdate = this.findUserById(userId);
        if (userUpdated.getEmail() != null && !userUpdated.getEmail().equals("")) {
            userToUpdate.setEmail(userUpdated.getEmail());
        }
        if (userUpdated.getName() != null && !userUpdated.getName().equals("")) {
            userToUpdate.setName(userUpdated.getName());
        }
        if (userUpdated.getSurname() != null && !userUpdated.getSurname().equals("")) {
            userToUpdate.setSurname(userUpdated.getSurname());
        }
        if (userUpdated.getPassword() != null && !userUpdated.getPassword().equals("")) {
            if (userToUpdate.getPassword() != passwordEncoder.encode(userUpdated.getPassword())){
                userToUpdate.setPassword(passwordEncoder.encode(userUpdated.getPassword()));
            }
        }
        return userRepository.saveAndFlush(userToUpdate);
    }

    @Transactional(readOnly = true)
    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    @Override
    public long addCodeToUser(String email) {
        User user = this.findUserByEmail(email);
        long code = (long) (Math.random() * 100000) + 10000;
        VerificationCode verificationCode = verificationService.addVerificationCode(code);
        user.setVerificationCode(verificationCode);
        this.updateUser(user.getId(), user);
        return code;
    }

    @Transactional(readOnly = true)
    @Override
    public User findUserByCode(Long code) {
        return userRepository.findByCode(code).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found"));
    }
}
