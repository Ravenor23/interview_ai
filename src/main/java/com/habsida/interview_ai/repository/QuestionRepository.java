package com.habsida.interview_ai.repository;

import com.habsida.interview_ai.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @EntityGraph("question-entity-graph")
    @Query("SELECT q FROM Question q WHERE q.id = :id")
    Optional<Question> findById(Long id);

    @EntityGraph("question-entity-graph")
    @Query("SELECT q FROM Question q")
    Page<Question> findAll(Pageable pageable);

    @Query("SELECT q FROM Question q join fetch q.englishFile order by rand()")
    List<Question> find10RandomEnglishQuestions(Pageable pageable);

    @Query("SELECT q FROM Question q join fetch q.koreanFile order by rand()")
    List<Question> find10RandomKoreanQuestions(Pageable pageable);

    @Query("SELECT q.id, q.englishText, q.koreanText, q.englishFile FROM Question q INNER JOIN q.answers a WHERE a.session.user.id = :id")
    Page<Question> findByUserId(Long id, Pageable pageable);

    @EntityGraph("question-entity-graph")
    @Query("SELECT q FROM Question q " +
            "JOIN FETCH q.answers a WHERE a.session.id = :id")
    Set<Question> findBySessionId(Long id);
}
