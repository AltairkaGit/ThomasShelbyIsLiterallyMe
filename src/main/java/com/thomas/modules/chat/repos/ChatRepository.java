package com.thomas.modules.chat.repos;

import com.thomas.modules.chat.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {
    void deleteByChatId(Long chatId);
}
