package com.finspire.service;

import com.finspire.constants.ApplicationConstants;
import com.finspire.controller.dto.BlogsRequestDto;
import com.finspire.converter.BlogsConverter;
import com.finspire.entity.Blogs;
import com.finspire.exception.ServiceException;
import com.finspire.repository.BlogsRepository;
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
public class BlogsService {
    private final BlogsRepository blogsRepository;
    private final ImageService imageService;
    private final BlogsConverter blogsConverter;

    public Blogs saveBlogDetails(MultipartFile file, BlogsRequestDto blogsRequestDto) throws IOException {
        String url = null;
        if (!file.isEmpty()){
            url = imageService.uploadImage(file);
        }
        return blogsRepository.save(blogsConverter.convert(blogsRequestDto,url));
    }

    public Page<Blogs> getAllBlogs(int page,int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return blogsRepository.findAll(pageable);
    }

    public String updateBlogsDetails(Long id, MultipartFile file, BlogsRequestDto blogsRequestDto) throws IOException {

        Blogs existingBlog = blogsRepository.findById(id).orElseThrow( () ->
        new ServiceException(ApplicationConstants.BLOG_NOT_FOUND,ApplicationConstants.BAD_REQUEST, HttpStatus.BAD_REQUEST));
            String url = null;
            if (file != null && !file.isEmpty()){
                url = imageService.uploadImage(file);
                existingBlog.setCoverImage(url);
            }

        existingBlog.setContent(blogsRequestDto.getContent());
        existingBlog.setTags(joinTags(blogsRequestDto.getTags()));
        existingBlog.setTitle(blogsRequestDto.getTitle());
        blogsRepository.save(existingBlog);
        return "successfully updated blog details";
    }

    private String joinTags(List<String> tags) {
        return tags.stream()
                .filter(tag -> tag != null && !tag.isEmpty())
                .collect(Collectors.joining(","));
    }

    public String deleteBlogsDetailsById(Long id) {
        Blogs existingBlog = blogsRepository.findById(id).orElseThrow( () ->
                new ServiceException(ApplicationConstants.BLOG_NOT_FOUND,ApplicationConstants.BAD_REQUEST, HttpStatus.BAD_REQUEST));
        blogsRepository.deleteById(id);
        return "id: "+existingBlog.getId() +" record successfully deleted";
    }
}
