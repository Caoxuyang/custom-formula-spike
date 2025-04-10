package com.fixmia.rag.controllers;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.AccessTier;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.BlobRequestConditions;
import com.azure.storage.blob.models.ParallelTransferOptions;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

public class UploadObject2 {

  public static void main(String[] args) throws IOException {
    uploadInputStream("ragbagTextFile3.txt");
  }

  public static void uploadInputStream(String fileObjectKeyName) throws IOException {
    String filePath = "src/main/webapp/service_provider_pfp_images/abc.txt";

    String containerName = "testbucket3rag";
    String endpoint = "https://yourstorageaccount.blob.core.windows.net";

    BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
            .endpoint(endpoint)
            .credential(new DefaultAzureCredentialBuilder().build())
            .buildClient();

    BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
    BlobClient blobClient = containerClient.getBlobClient(fileObjectKeyName);

    BlobHttpHeaders headers = new BlobHttpHeaders()
            .setContentMd5("data".getBytes(StandardCharsets.UTF_8))
            .setContentLanguage("en-US")
            .setContentType("binary");

    Map<String, String> metadata = Collections.singletonMap("timestamp", Instant.now().toString());
    BlobRequestConditions requestConditions = new BlobRequestConditions()
            .setIfUnmodifiedSince(OffsetDateTime.now().minusDays(3));
    Long blockSize = 100L * 1024L * 1024L; // 100 MB;
    ParallelTransferOptions parallelTransferOptions = new ParallelTransferOptions().setBlockSizeLong(blockSize);

    try {
      blobClient.uploadFromFile(filePath, parallelTransferOptions, headers, metadata,
              AccessTier.HOT, requestConditions, Duration.ofSeconds(60));
      System.out.println("Upload from file succeeded");
    } catch (UncheckedIOException ex) {
      System.err.printf("Failed to upload from file %s%n", ex.getMessage());
    }
  }

  public static void uploadImage(String fileObjectKeyName, String filePath) throws IOException {
    String containerName = "testbucket3rag";
    String endpoint = "https://yourstorageaccount.blob.core.windows.net";

    BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
            .endpoint(endpoint)
            .credential(new DefaultAzureCredentialBuilder().build())
            .buildClient();

    BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
    BlobClient blobClient = containerClient.getBlobClient(fileObjectKeyName);

    BlobHttpHeaders headers = new BlobHttpHeaders()
            .setContentMd5("data".getBytes(StandardCharsets.UTF_8))
            .setContentLanguage("en-US")
            .setContentType("binary");

    Map<String, String> metadata = Collections.singletonMap("timestamp", Instant.now().toString());
    BlobRequestConditions requestConditions = new BlobRequestConditions()
            .setIfUnmodifiedSince(OffsetDateTime.now().minusDays(3));
    Long blockSize = 100L * 1024L * 1024L; // 100 MB;
    ParallelTransferOptions parallelTransferOptions = new ParallelTransferOptions().setBlockSizeLong(blockSize);

    try {
      blobClient.uploadFromFile(filePath, parallelTransferOptions, headers, metadata,
              AccessTier.HOT, requestConditions, Duration.ofSeconds(60));
      System.out.println("Upload from file succeeded");
    } catch (UncheckedIOException ex) {
      System.err.printf("Failed to upload from file %s%n", ex.getMessage());
    }
  }

  public static int getInputStreamLength(InputStream inputStream) throws IOException {
    byte[] buffer = new byte[1024];
    int chunkBytesRead = 0;
    int length = 0;
    while ((chunkBytesRead = inputStream.read(buffer)) != -1) {
      length += chunkBytesRead;
    }
    return length;
  }

  private static ByteBuffer getRandomByteBuffer(int size) throws IOException {
    byte[] b = new byte[size];
    new Random().nextBytes(b);
    return ByteBuffer.wrap(b);
  }
