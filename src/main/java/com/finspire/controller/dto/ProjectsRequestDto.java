package com.finspire.controller.dto;

import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Validated
public class ProjectsRequestDto {
    private String title;
    private String category;
    @Size(max = 500)
    private String description;
    private List<String> technologies;
    private String mediaType;
    private boolean featured;
}
