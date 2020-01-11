package com.app.driveintegration.service;

import com.app.driveintegration.model.FolderCreationEntity;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
public class DriveOperationsServiceImplementation implements DriveOperationsService {

    @Value("${host.base.url}")
    String hostBaseUrl;
    @Autowired
    RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> uploadFile(MultipartFile multipartFile, String path,
        String authorization) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("accept", "application/json");
        headers.add("Authorization", authorization);
        MultiValueMap<String, Object> body
            = new LinkedMultiValueMap<>();
        body.add("file", multipartFile.getResource());
        log.info("resource::" + multipartFile.getResource());
        HttpEntity<MultiValueMap<String, Object>> requestEntity
            = new HttpEntity<>(body, headers);
        String serverUrl = hostBaseUrl + "/files";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(serverUrl)
            .queryParam("path", path);
        ResponseEntity<String> response = restTemplate
            .postForEntity(builder.toUriString(), requestEntity, String.class);
        log.debug("upload file response: {}", response);
        return response;
    }

    @Override
    public ResponseEntity<String> createFolder(String name, String authorization) {
        log.info("Auth:"+authorization);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("Authorization", authorization);
        FolderCreationEntity folderCreationEntity = new FolderCreationEntity();
        if (!name.startsWith("/")) {
            folderCreationEntity.setPath("/" + name);
        }else {
            folderCreationEntity.setPath(name);
        }
        HttpEntity<FolderCreationEntity> request = new HttpEntity<FolderCreationEntity>(
            folderCreationEntity, headers);
        ResponseEntity<String> response = restTemplate
            .postForEntity(hostBaseUrl + "/folders", request, String.class);
        log.debug("folder creation response:{} ", response);
        return response;
    }

    @Override
    public ResponseEntity<String> getFolderContents(String path, String authorization) {
        UriComponentsBuilder componentsBuilder = UriComponentsBuilder.fromHttpUrl(hostBaseUrl)
            .path("/folders")
            .path("/contents").queryParam("path", path);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorization);
        log.info(componentsBuilder.toUriString());
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<String> response = restTemplate
            .exchange(componentsBuilder.toUriString(), HttpMethod.GET, entity, String.class);

        log.debug("folder contents response:{} :", response);
        return response;
    }

    @Override
    public ResponseEntity<Resource> downloadFile(String path, String authorization) {

        String serverUrl = hostBaseUrl + "/files";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(serverUrl)
            .queryParam("path", path);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorization);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        ResponseEntity<Resource> response = restTemplate
            .exchange(builder.toUriString(), HttpMethod.GET, entity, Resource.class);
        log.debug("download file response: {}", response);
        return response;
    }
}
