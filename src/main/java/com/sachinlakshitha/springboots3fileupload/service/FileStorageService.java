package com.sachinlakshitha.springboots3fileupload.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final AmazonS3 amazonS3;
    @Value("${aws.s3.bucket}")
    String bucketName;

    public void createFolder(String folderName) {
        amazonS3.putObject(bucketName, folderName + "/", "");
    }

    public void deleteFolder(String folderName) {
        ObjectListing objectListing = amazonS3.listObjects(bucketName, folderName);
        List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
        for (S3ObjectSummary objectSummary : objectSummaries) {
            amazonS3.deleteObject(bucketName, objectSummary.getKey());
        }
        amazonS3.deleteObject(bucketName, folderName);
    }

    public String uploadFile(String folderName, String fileName, MultipartFile file) {
        try {
            amazonS3.putObject(new PutObjectRequest(bucketName, folderName + "/" + fileName, file.getInputStream(), null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            return amazonS3.getUrl(bucketName, folderName + "/" + fileName).toString();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to upload the file", e);
        }
    }

    public void uploadFile(String folderName, String fileName, InputStream inputStream) {
        amazonS3.putObject(new PutObjectRequest(bucketName, folderName + "/" + fileName, inputStream, null)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public void uploadMultipleFiles(String folderName, MultipartFile[] files) {
        for (MultipartFile file : files) {
            uploadFile(folderName, file.getOriginalFilename(), file);
        }
    }

    public byte[] downloadFile(String folderName, String fileName) {
        try {
            S3Object object = amazonS3.getObject(bucketName, folderName + "/" + fileName);
            S3ObjectInputStream objectContent = object.getObjectContent();
            return IOUtils.toByteArray(objectContent);
        } catch (AmazonServiceException | IOException e) {
            throw new IllegalStateException("Failed to download the file", e);
        }
    }

    public void deleteFile(String folderName, String fileName) {
        amazonS3.deleteObject(bucketName, folderName + "/" + fileName);
    }

    public void deleteAllFiles(String folderName) {
        amazonS3.listObjects(bucketName, folderName + "/").getObjectSummaries().forEach(s3ObjectSummary -> {
            amazonS3.deleteObject(bucketName, s3ObjectSummary.getKey());
        });
    }

    public String getFileURL(String folderName, String fileName) {
        return amazonS3.getUrl(bucketName, folderName + "/" + fileName).toString();
    }

    public String getFolderURL(String folderName) {
        return amazonS3.getUrl(bucketName, folderName + "/").toString();
    }
}
