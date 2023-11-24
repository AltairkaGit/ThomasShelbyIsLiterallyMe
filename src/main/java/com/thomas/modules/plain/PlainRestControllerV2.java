package com.thomas.modules.plain;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/hello-world")
public class PlainRestControllerV2 {
    @GetMapping("")
    ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok().body("Hello world!");
    }

}
