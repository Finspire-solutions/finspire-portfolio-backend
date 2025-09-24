package com.finspire.converter;

import com.finspire.controller.dto.ProjectsRequestDto;
import com.finspire.entity.Projects;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectConverter {

    public Projects convert(ProjectsRequestDto projectsRequestDto,String url){
        return Projects.builder()
                .title(projectsRequestDto.getTitle())
                .category(projectsRequestDto.getCategory())
                .description(projectsRequestDto.getDescription())
                .featured(projectsRequestDto.isFeatured())
                .technologies(joinTags(projectsRequestDto.getTechnologies()))
                .mediaType(projectsRequestDto.getMediaType())
                .url(url)
                .build();
    }
    private String joinTags(List<String> tags) {
        return tags.stream()
                .filter(tag -> tag != null && !tag.isEmpty())
                .collect(Collectors.joining(","));
    }
}
