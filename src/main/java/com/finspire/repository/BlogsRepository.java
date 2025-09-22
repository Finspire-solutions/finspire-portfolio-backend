package com.finspire.repository;

import com.finspire.entity.Blogs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogsRepository extends JpaRepository<Blogs,Long> {
}
