package com.finspire.controller.dto;

import lombok.*;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectsRequestDto {
    private String title;
    private String category;
    private String description;
    private List<String> technologies;
    private String mediaType;
    private boolean featured;
}
