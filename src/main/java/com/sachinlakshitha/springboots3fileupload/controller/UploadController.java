package com.sachinlakshitha.springboots3fileupload.controller;

import com.sachinlakshitha.springboots3fileupload.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UploadController {
    private final FileStorageService fileStorageService;
    String folderName = "test-folder";

    @PostMapping("/file/upload")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file) {
        return new ResponseEntity<>(fileStorageService.uploadFile(folderName, file.getOriginalFilename(), file), HttpStatus.OK);
    }

    @GetMapping("/file/download/{name:.+}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String name) {
        byte[] data = fileStorageService.downloadFile(folderName, name);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + name + "\"")
                .body(resource);
    }

    @GetMapping("/file/url/{name:.+}")
    public ResponseEntity<String> getFileUrl(@PathVariable String name) {
        return new ResponseEntity<>(fileStorageService.getFileURL(folderName, name), HttpStatus.OK);
    }

    @DeleteMapping("/file/{name:.+}")
    public ResponseEntity<String> deleteFile(@PathVariable String name) {
        fileStorageService.deleteFile(folderName, name);
        return new ResponseEntity<>("File deleted successfully", HttpStatus.OK);
    }

    @DeleteMapping("/folder/{name:.+}")
    public ResponseEntity<String> deleteFolder(@PathVariable String name) {
        fileStorageService.deleteFolder(name);
        return new ResponseEntity<>("Folder deleted successfully", HttpStatus.OK);
    }
}
