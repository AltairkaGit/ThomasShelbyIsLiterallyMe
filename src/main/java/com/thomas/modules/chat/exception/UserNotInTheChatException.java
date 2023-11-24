package com.thomas.modules.chat.exception;

public class UserNotInTheChatException extends  RuntimeException{
    public UserNotInTheChatException(String username, Long chatId) {
        super("User " + username + " isn't in the chat " + chatId);
    }
}
