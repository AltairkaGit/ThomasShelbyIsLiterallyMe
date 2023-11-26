package com.thomas.modules.music.service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

public interface TrackService {
    Long getDuration(MultipartFile track);
    StreamingResponseBody m3u8Index(String url);
    StreamingResponseBody mp3(String filename);
}
