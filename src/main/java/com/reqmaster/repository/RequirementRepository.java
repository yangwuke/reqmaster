package com.reqmaster.repository;

import com.reqmaster.entity.Requirement;
import com.reqmaster.entity.enums.RequirementType;
import com.reqmaster.entity.enums.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequirementRepository extends JpaRepository<Requirement, Long> {

    /**
     * 根据项目ID查找需求
     */
    List<Requirement> findByProjectId(Long projectId);

    /**
     * 根据需求类型查找
     */
    List<Requirement> findByType(RequirementType type);

    /**
     * 根据优先级查找
     */
    List<Requirement> findByPriority(Priority priority);

    /**
     * 根据项目ID和类型查找需求
     */
    List<Requirement> findByProjectIdAndType(Long projectId, RequirementType type);

    /**
     * 根据标题模糊搜索
     */
    List<Requirement> findByTitleContainingIgnoreCase(String keyword);

    /**
     * 统计项目中的需求数量
     */
    @Query("SELECT COUNT(r) FROM Requirement r WHERE r.project.id = :projectId")
    Long countByProjectId(@Param("projectId") Long projectId);

    /**
     * 查找已分析的需求
     */
    List<Requirement> findByIsAnalyzedTrue();

    /**
     * 根据来源类型查找需求
     */
    List<Requirement> findBySourceType(String sourceType);
}