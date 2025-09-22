package com.finspire.controller.api;

import com.finspire.controller.dto.BlogsRequestDto;
import com.finspire.entity.Blogs;
import com.finspire.service.BlogsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/blogs")
@RequiredArgsConstructor
@Validated
@CrossOrigin("*")
public class BlogsController {
    private final BlogsService blogsService;

    @PostMapping()
    public ResponseEntity<String> saveBlogDetails(@RequestPart("file") @Valid MultipartFile file,
                                                 @RequestPart("blogsRequestDto") BlogsRequestDto blogsRequestDto) throws IOException {
        blogsService.saveBlogDetails(file,blogsRequestDto);
        return ResponseEntity.ok("Successfully saved blog details");
    }

    @GetMapping("/getAll")
    public Page<Blogs> getAllBlogs(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size){
        return blogsService.getAllBlogs(page, size);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBlogsDetails(@PathVariable Long id, @RequestPart(value = "file",required = false) MultipartFile file,
                                          @RequestPart("blogsRequestDto") BlogsRequestDto blogsRequestDto) throws IOException {
        return ResponseEntity.ok(blogsService.updateBlogsDetails(id, file,blogsRequestDto));
    }

    @DeleteMapping("/{id}")
    public String deleteBlogDetailsById(@PathVariable Long id){
        return blogsService.deleteBlogsDetailsById(id);
    }

}
