package com.reqmaster.repository;

import com.reqmaster.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {

    /**
     * 根据项目ID查找对话会话
     */
    List<ChatSession> findByProjectIdOrderByUpdatedAtDesc(Long projectId);

    /**
     * 根据项目ID和会话类型查找
     */
    List<ChatSession> findByProjectIdAndSessionTypeOrderByUpdatedAtDesc(Long projectId, String sessionType);

    /**
     * 根据标题模糊搜索
     */
    List<ChatSession> findByTitleContainingIgnoreCase(String keyword);

    /**
     * 统计项目的对话会话数量
     */
    @Query("SELECT COUNT(cs) FROM ChatSession cs WHERE cs.project.id = :projectId")
    Long countByProjectId(@Param("projectId") Long projectId);

    /**
     * 查找最近更新的对话会话
     */
    @Query("SELECT cs FROM ChatSession cs WHERE cs.project.id = :projectId ORDER BY cs.updatedAt DESC LIMIT :limit")
    List<ChatSession> findRecentSessions(@Param("projectId") Long projectId, @Param("limit") int limit);
}