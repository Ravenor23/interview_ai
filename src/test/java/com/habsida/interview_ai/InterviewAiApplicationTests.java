package com.habsida.interview_ai;

import com.habsida.interview_ai.model.Question;
import com.habsida.interview_ai.model.Roles;
import com.habsida.interview_ai.model.User;
import com.habsida.interview_ai.repository.FileRepository;
import com.habsida.interview_ai.repository.QuestionRepository;
import com.habsida.interview_ai.service.QuestionService;
import com.habsida.interview_ai.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//@SpringBootTest
@AutoConfigureMockMvc
class InterviewAiApplicationTests {
    @Autowired
    private final QuestionService questionService;
    private final MockMvc mockMvc;
    private final FileRepository fileRepository;
    private final QuestionRepository questionRepository;
    private final UserService userService;

    InterviewAiApplicationTests(QuestionService questionService, MockMvc mockMvc, FileRepository fileRepository, QuestionRepository questionRepository, UserService userService) {
        this.questionService = questionService;
        this.mockMvc = mockMvc;
        this.fileRepository = fileRepository;
        this.questionRepository = questionRepository;
        this.userService = userService;
    }

//    @Test
//    void contextLoads() {
//    }
//
//    @Test
//    void getAudioFile() throws Exception {
//        File f = new File("C:\\Users\\82105\\Downloads\\Blackpink - Kill This Love.mp3'");
//        byte[] b = Files.readAllBytes(f.toPath());
//
//        mockMvc.perform(get("/audio/{id}", 1L))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().bytes(b));
////		questionService.uploadFile()
//    }
//
//    @Test
//    void dbTest() {
//        com.habsida.interview_ai.model.File file = new com.habsida.interview_ai.model.File();
//        file.setPath("static/Blackpink - Kill This Love.mp3");
//        file.setUpdatedAt(LocalDateTime.of(22, 12, 10, 22, 16));
//        file.setCreatedAt(LocalDateTime.now());
//        file.setType("music");
//
//        Question question = new Question("test", "ENGLISH", LocalDateTime.now(), LocalDateTime.now(),
//                fileRepository.save(file));
//
//        System.out.println(questionService.addQuestion(question));
//
//    }

//    @Test
//    void userDb() {
//        User user = new User("1@1.com", "One Alone", Roles.USER);
//        userService.addUser(user);
//    }


}
