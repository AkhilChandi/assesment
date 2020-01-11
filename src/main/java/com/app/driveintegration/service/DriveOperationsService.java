package com.app.driveintegration.service;

import java.io.IOException;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface DriveOperationsService {

    ResponseEntity<String> uploadFile(MultipartFile multipartFile,String path,String authorization) throws IOException;
    ResponseEntity<String> createFolder(String name,String authorization);
    ResponseEntity<String> getFolderContents(String path,String authorization);
    ResponseEntity<Resource> downloadFile(String path,String authorization);
}

