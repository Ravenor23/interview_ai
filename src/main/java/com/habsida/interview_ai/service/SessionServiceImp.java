package com.habsida.interview_ai.service;

import com.habsida.interview_ai.exception.NotFoundException;
import com.habsida.interview_ai.model.Session;
import com.habsida.interview_ai.repository.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
public class SessionServiceImp implements SessionService{

    private final SessionRepository sessionRepository;
    private final UserService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionServiceImp.class);

    public SessionServiceImp(SessionRepository sessionRepository, UserService userService) {
        this.sessionRepository = sessionRepository;
        this.userService = userService;
    }

    //TODO TEST
    @Transactional(readOnly = true)
    @Override
    public Page<Session> findAll(Integer pageNo, Integer pageSize, String sortBy) {
        LOGGER.info(pageNo.toString(), pageSize.toString(), sortBy);

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return sessionRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Session> getSessionsByUserId(Long id, Integer pageNo, Integer pageSize, String sortBy) {
        LOGGER.info(pageNo.toString(), pageSize.toString(), sortBy);

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return sessionRepository.findSessionsByUserId(id, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Session findById(Long id) {
        LOGGER.info(id.toString());

        return sessionRepository.findById(id).orElseThrow(()-> new NotFoundException("Session not found"));
    }

    @Transactional
    @Override
    public Session updateSession(Long sessionId, Session session) {
        LOGGER.info(sessionId.toString(), session.toString());

        Session sessionToUpdate = this.findById(sessionId);
        if (session.getTitle() != null && !session.getTitle().equals("")) {
            sessionToUpdate.setTitle(session.getTitle());
        }
        if (session.getLanguage() != null && !session.getLanguage().equals("")) {
            sessionToUpdate.setLanguage(session.getLanguage());
        }
        return sessionRepository.saveAndFlush(session);
    }

    @Transactional
    @Override
    public Session startSession(String email, String language) {
        LOGGER.info(email, language);

//        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date dateNow = Date.from(Instant.now());
//        String date = formater.format(dateNow);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd LL yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String title = "Session_" + LocalDate.now().format(dateFormatter) + "_at_" + LocalTime.now().format(timeFormatter);
        System.out.println("Title == " + title);

        Session session = Session.builder()
                .user(userService.findUserByEmail(email))
                .title(title)
                .answers(new ArrayList<>())
                .language(language)
                .build();

        return sessionRepository.save(session);
    }

    @Transactional
    @Override
    public void deleteSession(Long id) {
        LOGGER.info(id.toString());

        if (this.findById(id) != null) {
            sessionRepository.deleteById(id);
        }
    }
}
