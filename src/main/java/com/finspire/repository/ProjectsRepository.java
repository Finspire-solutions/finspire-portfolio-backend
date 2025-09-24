package com.finspire.repository;

import com.finspire.entity.Projects;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectsRepository extends JpaRepository<Projects,Long> {
}
