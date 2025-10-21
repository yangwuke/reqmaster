package com.reqmaster.repository;

import com.reqmaster.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    /**
     * 根据会话ID查找消息，按时间排序
     */
    List<ChatMessage> findByChatSessionIdOrderByTimestampAsc(Long sessionId);

    /**
     * 统计会话中的消息数量
     */
    @Query("SELECT COUNT(cm) FROM ChatMessage cm WHERE cm.chatSession.id = :sessionId")
    Long countByChatSessionId(@Param("sessionId") Long sessionId);

    /**
     * 查找会话中的用户消息
     */
    List<ChatMessage> findByChatSessionIdAndRoleOrderByTimestampAsc(Long sessionId, String role);

    /**
     * 删除会话的所有消息
     */
    void deleteByChatSessionId(Long sessionId);
}