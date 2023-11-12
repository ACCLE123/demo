package com.example.demo.controller;

import com.example.demo.utils.QCLoudUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping
@Slf4j
public class UploadController {
    @Autowired
    private QCLoudUtil cLoudUtil;
    @PostMapping("/upload")
    public String upload(MultipartFile image) throws IOException {
//        log.info("{}", image);
        String upload = cLoudUtil.upload(image);
        return upload;
    }
}
