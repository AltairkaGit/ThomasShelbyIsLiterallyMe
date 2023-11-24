package com.thomas;

import com.thomas.lib.validator.exception.LessMinLengthException;
import com.thomas.lib.validator.exception.MoreMaxLengthException;
import com.thomas.lib.validator.exception.WrongEmailException;
import com.thomas.modules.chat.exception.UserAlreadyInTheChatException;
import com.thomas.modules.chat.exception.UserNotInTheChatException;
import com.thomas.modules.file.exception.FileIsTooGreatException;
import com.thomas.modules.server.exception.UserAlreadyOnServerException;
import com.thomas.modules.server.exception.UserNotOnServerException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.AuthenticationException;
import javax.security.auth.login.CredentialException;
import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({
            AuthenticationException.class,
            CredentialException.class,
            SecurityException.class,
            AccessDeniedException.class
    })
    public ResponseEntity<String> handleSecurity(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler({
            SignatureException.class,
            JwtException.class,
            MalformedJwtException.class,
    })
    public ResponseEntity<String> handleDeadToken(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler({
            WrongEmailException.class,
            LessMinLengthException.class,
            MoreMaxLengthException.class,
            FileIsTooGreatException.class,
            UserNotInTheChatException.class,
            UserAlreadyInTheChatException.class,
            UserAlreadyOnServerException.class,
            UserNotOnServerException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<String> handleWrongEmailException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
        NoSuchElementException.class
    })
    public ResponseEntity<String> handleNoSuchElementException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NO_CONTENT);
    }
}
