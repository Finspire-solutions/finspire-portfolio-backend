package com.finspire.service;

import com.finspire.constants.ApplicationConstants;
import com.finspire.controller.dto.ProjectsRequestDto;
import com.finspire.converter.ProjectConverter;
import com.finspire.entity.Projects;
import com.finspire.exception.ServiceException;
import com.finspire.repository.ProjectsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectsService {
    private final ImageService imageService;
    private final ProjectsRepository projectsRepository;
    private final ProjectConverter projectConverter;
    public ResponseEntity<String> saveProjectDetails(MultipartFile file, ProjectsRequestDto projectsRequestDto) throws IOException {
        String url = null;
        if (file != null && !file.isEmpty()){
            url = imageService.uploadImage(file);
        }
        projectsRepository.save(projectConverter.convert(projectsRequestDto,url));
        return ResponseEntity.ok("successfully saved project details");
    }

    public Page<Projects> getAllProjects(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        return projectsRepository.findAll(pageable);
    }

    public String updateProjectDetails(Long id, MultipartFile file, ProjectsRequestDto projectsRequestDto) throws IOException {
        Projects existingBlog = findProjectById(id);
        String url = null;
        if (file != null && !file.isEmpty()){
            url = imageService.uploadImage(file);
            existingBlog.setUrl(url);
        }

        existingBlog.setCategory(projectsRequestDto.getCategory());
        existingBlog.setTechnologies(joinTags(projectsRequestDto.getTechnologies()));
        existingBlog.setTitle(projectsRequestDto.getTitle());
        existingBlog.setDescription(projectsRequestDto.getDescription());
        existingBlog.setFeatured(projectsRequestDto.isFeatured());
        existingBlog.setMediaType(projectsRequestDto.getMediaType());
        projectsRepository.save(existingBlog);
        return "successfully updated blog details";
    }

    public String deleteProjectDetailsById(Long id) {
        Projects existingBlog = findProjectById(id);
        projectsRepository.deleteById(id);
        return "id: "+existingBlog.getId()+" record deleted successfully";
    }

    private String joinTags(List<String> tags) {
        return tags.stream()
                .filter(tag -> tag != null && !tag.isEmpty())
                .collect(Collectors.joining(","));
    }

    private Projects findProjectById(Long id){
        return projectsRepository.findById(id).orElseThrow( () ->
                new ServiceException(ApplicationConstants.PROJECT_DETAILS_NOT_FOUND,ApplicationConstants.BAD_REQUEST, HttpStatus.BAD_REQUEST));
    }
}
