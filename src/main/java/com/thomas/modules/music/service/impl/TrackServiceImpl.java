package com.thomas.modules.music.service.impl;

import com.thomas.modules.file.service.FileService;
import com.thomas.modules.music.service.TrackService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.util.List;
import java.util.UUID;

@Service
public class TrackServiceImpl implements TrackService {
    private final FileService fileService;

    public TrackServiceImpl(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public Long getDuration(MultipartFile track) {
        return 1703L;
    }

    @Override
    public StreamingResponseBody m3u8Index(String url) {
        try {
            BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                            new FileInputStream(fileService.urlToFilename(url))));
            return outputStream -> {
                try {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.endsWith(".mp3")) line = fileService.composeUrl(line);
                        outputStream.write(line.getBytes());
                        outputStream.write(System.lineSeparator().getBytes());
                    }
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    reader.close();
                    outputStream.close();
                }
            };
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public StreamingResponseBody mp3(String filename) {
        try {
            InputStream inputStream = new FileInputStream(filename);
            return outputStream -> {
                try {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    inputStream.close();
                    outputStream.close();
                }
            };
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
