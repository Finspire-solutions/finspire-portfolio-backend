package com.finspire.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "blogs")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Blogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "content")
    private String content;
    @Column(name = "cover_image")
    private String coverImage; // URL or data URL
    @Column(name = "tags")
    private String tags;
    @Column(name = "created_at")
    private LocalDate createdAt;

    @PrePersist
    public void createDate(){
    this.createdAt = LocalDate.now();
    }
}
