package com.finspire.converter;

import com.finspire.controller.dto.BlogsRequestDto;
import com.finspire.entity.Blogs;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BlogsConverter {
    public Blogs convert(BlogsRequestDto requestDto,String url){
        return Blogs.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .coverImage(url)
                .tags(joinTags(requestDto.getTags()))
                .build();
    }

    private String joinTags(List<String> tags) {
        return tags.stream()
                .filter(tag -> tag != null && !tag.isEmpty())
                .collect(Collectors.joining(","));
    }
}
