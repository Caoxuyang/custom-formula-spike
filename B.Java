package com.fixmia.rag.controllers;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.CompletedMultipartUpload;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.CompletedPart;
import software.amazon.awssdk.services.s3.model.CreateBucketConfiguration;
import software.amazon.awssdk.services.s3.model.UploadPartRequest;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketResponse;


import java.io.IOException;

public class UploadObject2 {

    public static void main(String[] args) throws IOException {
          uploadInputStream("ragbagTextFile3.txt");
    }
    public static void uploadInputStream(String fileObjectKeyName) throws IOException {
        File initialFile = new File("src/main/webapp/service_provider_pfp_images/abc.txt");
        InputStream targetStream = new FileInputStream(initialFile);

        String bucketName = "testbucket3rag";
        Region region = Region.EU_NORTH_1;
        S3Client s3 = S3Client.builder()
                .region(region)
                .build();
        Map<String, String> metadata = new HashMap<>();
        metadata.put("x-amz-meta-myVal", "test");
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileObjectKeyName)
                .metadata(metadata)
                .build();
        System.out.println("stream available is "+targetStream.available());
//        System.out.println("stream size is "+getInputStreamLength(targetStream));
        s3.putObject(objectRequest, RequestBody.fromInputStream(targetStream,targetStream.available()));

    }
    public static void uploadImage(String fileObjectKeyName, InputStream stream) throws IOException {
        String bucketName = "testbucket3rag";
        Region region = Region.EU_NORTH_1;
        S3Client s3 = S3Client.builder()
                .region(region)
                .build();


        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileObjectKeyName)
                .build();
        s3.putObject(objectRequest, RequestBody.fromInputStream(stream,getInputStreamLength(stream)));
    }
    public static int getInputStreamLength(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int chunkBytesRead = 0;
        int length = 0;
        while((chunkBytesRead = inputStream.read(buffer)) != -1) {
            length += chunkBytesRead;
        }
        return length;
    }
    private static ByteBuffer getRandomByteBuffer(int size) throws IOException {
        byte[] b = new byte[size];
        new Random().nextBytes(b);
        return ByteBuffer.wrap(b);
    }
}