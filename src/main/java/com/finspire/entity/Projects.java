package com.finspire.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "projects")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Projects {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(name = "title")
        private String title;
        @Column(name = "category")
        private String category;
        @Column(name = "description")
        private String description;
        @Column(name = "technologies")
        private String technologies;
        @Column(name = "url")
        private String url; // can be URL or data URL
        @Column(name = "media_type")
        private String mediaType; //image or video
        @Column(name = "featured")
        private boolean featured;
        @Column(name = "created_date")
        private LocalDate createdDate;

        @PrePersist
        public void onCreate(){
            this.createdDate = LocalDate.now();
        }
}
