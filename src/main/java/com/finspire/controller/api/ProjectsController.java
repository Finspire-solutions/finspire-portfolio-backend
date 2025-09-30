package com.finspire.controller.api;

import com.finspire.controller.dto.ProjectsRequestDto;
import com.finspire.entity.Projects;
import com.finspire.service.ProjectsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
@Validated
@CrossOrigin("*")
public class ProjectsController {
    private final ProjectsService projectsService;

    @PostMapping()
    public ResponseEntity<String> saveProjectDetails(@RequestPart("file") @Valid MultipartFile file,
                                                  @RequestPart("projectsRequestDto") @Valid ProjectsRequestDto projectsRequestDto) throws IOException {
        projectsService.saveProjectDetails(file,projectsRequestDto);
        return ResponseEntity.ok("Successfully saved project details");
    }

    @GetMapping("/getAll")
    public Page<Projects> getAllProjects(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return projectsService.getAllProjects(page, size);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProjectDetails(@PathVariable Long id, @RequestPart(value = "file",required = false) MultipartFile file,
                                                     @RequestPart("projectsRequestDto") ProjectsRequestDto projectsRequestDto) throws IOException {
        return ResponseEntity.ok(projectsService.updateProjectDetails(id, file,projectsRequestDto));
    }

    @DeleteMapping("/{id}")
    public String deleteProjectDetailsById(@PathVariable Long id){
        return projectsService.deleteProjectDetailsById(id);
    }

}
