package com.habsida.interview_ai.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class AmazonS3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;
    private static final Logger LOGGER = LoggerFactory.getLogger(AmazonS3Service.class);

    public AmazonS3Service(AmazonS3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    public String create(MultipartFile file, String hashName) {
        LOGGER.info(file.toString());

        File fileObj = convertMultiPartFileToFile(file);
        if (hashName.isEmpty()){
            hashName = LocalDateTime.now() + "_" + file.getOriginalFilename();
        }
        amazonS3Client.putObject(new PutObjectRequest(bucket, hashName, fileObj));
        amazonS3Client.setObjectAcl(bucket, hashName, CannedAccessControlList.PublicRead);
        fileObj.delete();
        return hashName;
    }

    public byte[] downloadFile(String filename) throws IOException {
        LOGGER.info(filename);

        S3Object s3Object = amazonS3Client.getObject(bucket, filename);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        byte [] content = IOUtils.toByteArray(inputStream);
        return content;
    }

    public void deleteFile(String filename) {
        LOGGER.info(filename);

        amazonS3Client.deleteObject(bucket, filename);
    }


    public File convertMultiPartFileToFile(MultipartFile file) {
        LOGGER.info(file.toString());

        File convertedFile = new File(file.getOriginalFilename());

        try (FileOutputStream fos = new FileOutputStream(convertedFile)){
            fos.write(file.getBytes());
        } catch (IOException e) {
            e.getMessage();
        }
        return convertedFile;
    }

}
