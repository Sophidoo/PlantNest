package com.cybertitans.CyberTitans.service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    String uploadFIle(MultipartFile image);
}
