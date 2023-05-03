package com.habsida.interview_ai.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@NamedEntityGraph(
        name = "Session.answer",
        attributeNodes = {
                @NamedAttributeNode(value = "answers", subgraph = "subgraph.answer"),
                @NamedAttributeNode(value = "user"),
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "subgraph.answer",
                        attributeNodes = {
                                @NamedAttributeNode(
                                        value = "question",
                                        subgraph = "subgraph.question"
                                ),
                                @NamedAttributeNode(
                                        value = "file"
                                )
                        }

                ),
                @NamedSubgraph(
                        name = "subgraph.question",
                        attributeNodes = {
                        @NamedAttributeNode(value = "englishFile"),
                        @NamedAttributeNode(value = "koreanFile")
                } ),
        }
)

public class Session extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String language;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = false, mappedBy = "session")
    @JsonManagedReference
    private List<Answer> answers;

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
//                ", user=" + user +
                ", created_at=" + super.getCreatedAt() +
                ", updated_at=" + super.getUpdatedAt() +
                '}';
    }
}
