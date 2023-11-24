package com.thomas.modules.server.service.impl;

import com.thomas.modules.server.service.ConversationService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ConversationServiceImpl implements ConversationService {
    private final Map<String, Set<String>> conversations = new ConcurrentHashMap<>();

    @Override
    public void attachUser(String conversation, String userId) {
        conversations.compute(conversation, (key, room) -> {
            if (room == null) {
                Set<String> newRoom = new HashSet<>();
                newRoom.add(userId);
                return newRoom;
            } else {
                room.add(userId);
                return room;
            }
        });
    }

    @Override
    public Set<String> getRoomCopy(String conversation) {
        return new HashSet<>(conversations.get(conversation));
    }
}
