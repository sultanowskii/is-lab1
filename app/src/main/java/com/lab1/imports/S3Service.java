package com.lab1.imports;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class S3Service {
    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void uploadFile(String key, Path filePath) throws S3Exception {
        var request = PutObjectRequest
                .builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.putObject(request, filePath);
    }

    public void downloadFile(String key, Path destinationPath) throws S3Exception {
        var request = GetObjectRequest
                .builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.getObject(request, destinationPath);
    }

    public void deleteFile(String key) throws S3Exception {
        var request = DeleteObjectRequest
                .builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.deleteObject(request);
    }
}
