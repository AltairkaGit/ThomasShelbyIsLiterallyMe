package com.thomas.modules.file.exception;

import org.springframework.web.multipart.MultipartException;

public class FileIsTooGreatException extends MultipartException {
    public FileIsTooGreatException(String msg) {
        super(msg);
    }
}
