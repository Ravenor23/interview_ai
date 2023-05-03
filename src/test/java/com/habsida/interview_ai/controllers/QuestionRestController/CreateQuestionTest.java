package com.habsida.interview_ai.controllers.QuestionRestController;

import com.habsida.interview_ai.model.Question;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestExecutionListeners({
        TransactionalTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        SqlScriptsTestExecutionListener.class
})
public class CreateQuestionTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    FileService fileService;

    @Test
    @Sql(scripts = "/data/sql/controllers/questionRestController/create/delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void saveFromMoviesTest() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "file.txt", MediaType.TEXT_PLAIN_VALUE, "some text".getBytes());


        mockMvc.perform(
                            multipart("/api/question")
                                    .file(multipartFile)
                                    .param("text", "asdsad")
                                    .param("language", "eng"))
                .andExpect(status().isOk());

        Question question = (Question) entityManager.createQuery("select q from Question q where q.id = 1 ").getSingleResult();
        Assertions.assertThat(question.getId()).isEqualTo(1L);
        Assertions.assertThat(question.getEnglishText()).isEqualTo("asdsad");
    }
}