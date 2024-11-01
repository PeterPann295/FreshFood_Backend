package com.freshfood.controller;

import com.freshfood.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping("/cloud")
@RestController
@RequiredArgsConstructor
public class CloudController {
    private final CloudinaryService cloudinaryService;

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String uploadToCloud(@RequestPart MultipartFile file) throws IOException {
        return cloudinaryService.uploadImage(file);
    }

}
