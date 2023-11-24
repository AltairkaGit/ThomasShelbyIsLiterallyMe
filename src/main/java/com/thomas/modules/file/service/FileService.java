package com.thomas.modules.file.service;

import com.thomas.modules.file.entity.FileEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Base64;

@Transactional
public interface FileService {
    FileEntity getFile(String url);
    FileEntity getFile(Long id);
    FileEntity uploadFile(MultipartFile picture);

    /**
     *
     * @param url
     * deletes a file on database
     * and on os if it's on os
     */
    void deleteFile(String url);

    /**
     *
     * @param path
     * deletes a file on os if the file is on the path
     * does nothing otherwise
     */
    void deleteFileOnOs(Path path);
    String composeUrl(String filename);

    /**
     *
     * @param url
     * @return a filename after last slash, null if filename is incorrect
     */
    String urlToFilename(String url);
    private String encodeBase64Url(String rawUrl) {
        return Base64.getUrlEncoder().encodeToString(rawUrl.getBytes());
    }
    private String decodeBase64Url(String rawUrl) {
        return new String(Base64.getUrlDecoder().decode(rawUrl));
    }


}
