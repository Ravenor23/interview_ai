package com.habsida.interview_ai.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Table(name = "files")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class File extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String path;
    private String originalTitle;

    @Transient
    private static String urlPath = "http://interview.ai.s3.ap-northeast-2.amazonaws.com/";

    public String getPath() {
        return urlPath + path;
    }

    public String getShortPath() {
        return path;
    }

    @Override
    public String toString() {
        return "File{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", path='" + path + '\'' +
                ", originalName='" + originalTitle + '\'' +
                ", createdAt=" + super.getCreatedAt() +
                ", updatedAt=" + super.getUpdatedAt() +
                '}';
    }
}
