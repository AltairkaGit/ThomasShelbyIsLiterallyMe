package com.thomas.modules.music.service.impl;

import com.thomas.modules.file.service.FileService;
import com.thomas.modules.music.service.TrackService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;

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
                            new FileInputStream(fileService.urlToFilename(fileService.urlToFilename(url)))));
            return outputStream -> {
                try {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (!line.endsWith(".ts")) continue;
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
}
