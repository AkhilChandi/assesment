package com.app.driveintegration.controller;

import com.app.driveintegration.exception.InvalidArgumentException;
import com.app.driveintegration.service.DriveOperationsService;
import com.app.driveintegration.service.Validations;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@Validated

public class DriveOperationsController {

    @Autowired
    DriveOperationsService driveOperationsService;

    @ApiOperation(value = "upload content to  folder", tags = {"upload"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "content uploaded to folder successfully ", response = Json.class),
        @ApiResponse(code = 401, message = "Typically when User is Not authorized ", response = String.class)}
    )
    @ApiImplicitParam(name = "Authorization", value = "authorization tokens. The format for the header value is Element <token>, User <user secret>", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "user cdd57"
        + "5 Element 0s4353")
    @PostMapping("/upload")
    public Object uploadFileToDriveFolder(@ApiParam(value = "file to be uploaded",
        required = true)@NotNull @RequestParam("file") MultipartFile file,
        @ApiParam(value = "The full path to the file (e.g. /myDirectory/myFile.txt).",
            required = true)@RequestParam("path") @NotEmpty(message = "folder name must not be empty") @NotBlank(message = "folder name must not be blank") String path,
        HttpServletRequest httpServletRequest)
        throws IOException, InvalidArgumentException {
        log.info("file upload request:");
        String authorization = httpServletRequest.getHeader("Authorization");
        //validating path
        Validations validations = new Validations();
        validations.validatePath(path);
        ResponseEntity<String> fileUploadResponse = driveOperationsService
            .uploadFile(file, path, authorization);
        return fileUploadResponse;
    }

    @ApiOperation(value = "download files from folder", tags = {"download"}, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "if everything works as expected ", response = Json.class),
        @ApiResponse(code = 401, message = "Typically when User is Not authorized ", response = String.class),
        @ApiResponse(code = 404, message = "file or path not found ", response = String.class)}
    )

    @ApiImplicitParam(name = "Authorization", value = "authorization tokens. The format for the header value is Element <token>, User <user secret>", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "user cdd57"
        + "5 Element 0s4353")
    @GetMapping("/download")
    public Object downloadFileFromFolder(
        @ApiParam(value = "Absolute path of the file (e.g. /Directory/MyFile.txt)",
            required = true) @RequestParam("path") @NotEmpty(message = "folder name must not be empty") @NotBlank(message = "folder name must not be blank") String path,
        HttpServletRequest httpServletRequest)
        throws IOException, InvalidArgumentException {
        log.info("file download request");
        String authorization = httpServletRequest.getHeader("Authorization");
        //validating path
        Validations validations = new Validations();
        validations.validatePath(path);
        ResponseEntity<Resource> resource = driveOperationsService
            .downloadFile(path, authorization);
        log.info("response body:{}", resource);
        return resource;

    }

    @ApiOperation(value = "get all available  file names from folder with absolute paths", tags = {"See files"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "if every thing works as expected ", response = Json.class),
        @ApiResponse(code = 401, message = "Typically when User is Not authorized ", response = String.class),
        @ApiResponse(code = 404, message = "file or path not found", response = String.class)}
    )
    @ApiImplicitParam(name = "Authorization", value = "authorization tokens. The format for the header value is Element <token>, User <user secret>", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "user cdd57"
        + "5 Element 0s4353")
    @GetMapping("/getfiles")
    public Object getDriveFolderContents(
        @ApiParam(value = "The full path to the directory (e.g. /myDirectory).",
            required = true) @RequestParam("path") @NotEmpty(message = "folder name must not be empty") @NotBlank(message = "folder name must not be blank") String path,
        HttpServletRequest httpServletRequest) throws InvalidArgumentException {
        log.info("get files request:");
        String authorization = httpServletRequest.getHeader("Authorization");
        //validating path
        Validations validations = new Validations();
        validations.validatePath(path);
        ResponseEntity<String> folderContents = driveOperationsService
            .getFolderContents(path, authorization);
        return folderContents;
    }

    @ApiOperation(value = "will create a new folder with given name ", tags = {"Create Folder"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "if every thing works as expected ", response = Json.class),
        @ApiResponse(code = 401, message = "Typically when User is Not authorized ", response = String.class)}
    )

    @ApiImplicitParam(name = "Authorization", value = "authorization tokens. The format for the header value is Element <token>, User <user secret>", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "user cdd57"
        + "5 Element 0s4353")
    @PostMapping("/create/folder")
    public Object createFolderInDrive(
        @ApiParam(value = "The full name for folder (e.g folderName).",
            required = true) @RequestParam("name") @NotEmpty(message = "folder name must not be empty") @NotBlank(message = "folder name must not be blank") String name,
        HttpServletRequest httpServletRequest) {
        log.info("folder creation request:");
        String authorization = httpServletRequest.getHeader("Authorization");
        ResponseEntity<String> folder = driveOperationsService.createFolder(name, authorization);
        return folder;
    }
}
