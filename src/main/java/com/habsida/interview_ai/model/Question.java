package com.habsida.interview_ai.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;


import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Table(name = "questions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@NamedEntityGraph(
        name = "question-entity-graph",
        attributeNodes = {
                @NamedAttributeNode("englishFile"),
                @NamedAttributeNode("koreanFile"),
        }
)
public class Question extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String englishText;
    private String koreanText;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "englishFile_id", referencedColumnName = "id")
    private File englishFile;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "koreanFile_id", referencedColumnName = "id")
    private File koreanFile;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = false, mappedBy = "question")
    @JsonBackReference
    private List<Answer> answers;
}
