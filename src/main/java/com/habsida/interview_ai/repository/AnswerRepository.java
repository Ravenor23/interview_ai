package com.habsida.interview_ai.repository;

import com.habsida.interview_ai.model.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @EntityGraph("question-entity-graph")
    @Query("SELECT a FROM Answer a JOIN FETCH a.question q JOIN FETCH a.file WHERE a.id = :id")
    Optional<Answer> findById(Long id);

    @Query("SELECT a.id, a.file, a.question.id, a.question.englishText, a.question.koreanText, a.question.englishFile, a.question.koreanFile FROM Answer a")
    Page<Answer> findAll(Pageable pageable);

    @Query("SELECT a.id, a.file, a.question.id, a.question.englishText, a.question.koreanText, a.question.englishFile, a.question.koreanFile FROM Answer a " +
            "INNER JOIN a.question q WHERE q.id = :id")
    Page<Answer> findByQuestionId(Long id, Pageable pageable);

    @Query("SELECT a.id, a.file, a.question.id, a.question.englishText, a.question.koreanText, a.question.englishFile, a.question.koreanFile FROM Answer a " +
            "INNER JOIN a.session s WHERE s.user.id = :id")
    Page<Answer> findByUserId(Long id, Pageable pageable);

    @Query("SELECT a.id, a.file, a.question.id, a.question.englishText, a.question.koreanText, a.question.englishFile, a.question.koreanFile FROM Answer a " +
            "INNER JOIN a.question q INNER JOIN a.session s WHERE q.id = :questionId AND s.user.id = :userId")
    Page<Answer> findByUserAndQuestionId(Long userId, Long questionId, Pageable pageable);
}
