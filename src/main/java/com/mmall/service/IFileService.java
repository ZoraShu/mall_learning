package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by yueshu on 9/9/17.
 */
public interface IFileService {
    String upload(MultipartFile file, String path);
}
