package com.reqmaster.repository;

import com.reqmaster.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    /**
     * 根据项目名称查找项目
     */
    Optional<Project> findByName(String name);

    /**
     * 根据项目名称模糊搜索
     */
    List<Project> findByNameContainingIgnoreCase(String name);

    /**
     * 根据领域查找项目
     */
    List<Project> findByDomain(String domain);

    /**
     * 统计项目数量
     */
    @Query("SELECT COUNT(p) FROM Project p")
    Long countAllProjects();

    /**
     * 查找包含特定需求数量的项目
     */
    @Query("SELECT p FROM Project p WHERE SIZE(p.requirements) >= :minRequirements")
    List<Project> findProjectsWithMinRequirements(@Param("minRequirements") int minRequirements);
}