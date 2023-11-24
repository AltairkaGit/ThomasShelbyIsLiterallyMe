package com.thomas.modules.server.exception;

public class UserNotOnServerException extends RuntimeException {
    public UserNotOnServerException(long serverId, long userId) {
        super("User: " + userId + " not on the server: " + serverId);
    }
}
