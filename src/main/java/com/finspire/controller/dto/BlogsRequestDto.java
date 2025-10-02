package com.finspire.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
public class BlogsRequestDto {
    @NotNull(message = "Title can't be null")
    private String title;
    @NotNull(message = "content can't be null")
    private String content;
    @NotNull(message = "tags can't be null")
    private List<String> tags;
    private String instagramLink;
    private String facebookLink;
    private String linkedinLink;
    private String githubLink;
}
