package com.habsida.interview_ai.controllers.QuestionRestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.habsida.interview_ai.model.File;
import com.habsida.interview_ai.model.Question;
import com.habsida.interview_ai.service.AmazonS3Service;
import com.habsida.interview_ai.service.FileService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestExecutionListeners({
        TransactionalTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        SqlScriptsTestExecutionListener.class
})
public class EditQuestionTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    FileService fileService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AmazonS3Service amazonS3Service;

    @Test
    public void editTest() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "newFile.txt", MediaType.TEXT_PLAIN_VALUE, "some text".getBytes());
        File file = File.builder()
                .id(1L)
                .type(multipartFile.getContentType())
                .originalTitle(multipartFile.getOriginalFilename())
                .build();
        Question question = Question.builder()
                .englishText("new text")
                .englishFile(file)
                .build();

        String questionToString = objectMapper.writeValueAsString(question);


        mockMvc.perform(
                        put("/api/question/{id}", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(question))
                                .param("language", "eng"))
                    .andExpect(status().isOk());

        Question questionFromDb = (Question) entityManager.createQuery("select q from Question q where q.id = 1 ").getSingleResult();
        Assertions.assertThat(questionFromDb.getId()).isEqualTo(1L);
        Assertions.assertThat(questionFromDb.getEnglishText()).isEqualTo("new text");
    }
}
