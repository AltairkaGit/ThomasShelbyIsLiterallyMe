package com.thomas.modules.chat.exception;

public class UserAlreadyInTheChatException extends RuntimeException{
    public UserAlreadyInTheChatException(String username, Long chatId) {
        super("User " + username + " already in the chat, chatId: " + chatId);
    }
}
