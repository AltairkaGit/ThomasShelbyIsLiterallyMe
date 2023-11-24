package com.thomas.modules.file.service.impl;

import com.thomas.modules.file.entity.FileEntity;
import com.thomas.modules.file.exception.FileIsTooGreatException;
import com.thomas.modules.file.repos.FileRepository;
import com.thomas.modules.file.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Value("${hostnm}")
    private String hostname;
    @Value("${upload.path}")
    private String uploadPath;
    @Value("${upload.workdir}")
    private String uploadWorkdir;
    private static final Long MAX_FILE_SIZE_BYTES = 1024 * 1024 * 35L;
    private static final String DEFAULT_FILE_NAME = "file_";
    private final FileRepository fileRepository;

    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public FileEntity getFile(Long id) {
        Optional<FileEntity> file = fileRepository.findById(id);
        if (file.isEmpty()) throw new NoSuchElementException("file with this id is not present");
        return file.get();
    }

    @Override
    public FileEntity getFile(String url) {
        int lastSlash = url.lastIndexOf('/');
        if (lastSlash == -1 || lastSlash + 1 == url.length()) throw new NoSuchElementException("wrong url");
        String filename = url.substring(lastSlash + 1);
        Optional<FileEntity> file = fileRepository.findByName(filename);
        if (file.isEmpty()) throw new NoSuchElementException("file with this url is not present");
        return file.get();
    }

    @Override
    public FileEntity uploadFile(MultipartFile file) {
        if (file.isEmpty()) throw new MultipartException("empty file");
        long size = file.getSize();
        String origFileName = file.getOriginalFilename();
        if (origFileName == null) throw new MultipartException("no filename");
        int lastPointIdx = origFileName.lastIndexOf('.');
        if (size > MAX_FILE_SIZE_BYTES) throw new FileIsTooGreatException("file size greater than " + (MAX_FILE_SIZE_BYTES / 1024 / 1024) + "MB");
        if (lastPointIdx == -1 || lastPointIdx + 1 == origFileName.length()) throw new MultipartException("filename is incorrect: no file extension");

        String mimeType = file.getContentType();

        String ext = origFileName.substring(lastPointIdx + 1);
        String fileName = DEFAULT_FILE_NAME + UUID.randomUUID() + UUID.randomUUID() + '.' + ext;
        try {
            Path dest = Paths.get(uploadWorkdir, uploadPath, fileName);
            file.transferTo(dest);
        } catch (IOException e) {
            throw new MultipartException("Saving on server error");
        }

        FileEntity fileEntity = new FileEntity();
        fileEntity.setName(fileName);
        fileEntity.setMimeType(mimeType);
        fileEntity.setSize((int)size);
        fileRepository.saveAndFlush(fileEntity);

        return fileEntity;
    }

    @Override
    public void deleteFileOnOs(Path path) {
        try {
            Files.delete(path);
        } catch (IOException ignored) {
        }
    }

    @Override
    public void deleteFile(String url) {
        FileEntity file = getFile(url);
        String filename = urlToFilename(url);
        Path workdir = Paths.get(".").toAbsolutePath();
        Path path = Paths.get(workdir.toString(), uploadPath, filename);
        deleteFileOnOs(path);
        fileRepository.deleteById(file.getFileId());
    }

    @Override
    public String urlToFilename(String url) {
        int lastSlash = url.lastIndexOf('/');
        if (lastSlash + 1 == url.length() || lastSlash == -1) return null;
        return url.substring(lastSlash + 1);
    }

    @Override
    public String composeUrl(String filename) {
        StringBuilder sb = new StringBuilder("http://");
        if (hostname.startsWith("/")) sb.append(hostname.substring(1));
        else sb.append(hostname);
        if (!uploadPath.startsWith("/")) sb.append("/");
        sb.append(uploadPath)
            .append("/")
            .append(filename);
        return sb.toString();
    }
}
