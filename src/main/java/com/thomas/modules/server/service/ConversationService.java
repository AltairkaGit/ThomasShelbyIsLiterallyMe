package com.thomas.modules.server.service;

import java.util.Set;

public interface ConversationService {
    void attachUser(String conversation, String userId);
    Set<String> getRoomCopy(String conversation);
}
