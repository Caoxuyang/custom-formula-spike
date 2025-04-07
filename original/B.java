package com.fixmia.rag.controllers;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.options.BlobParallelUploadOptions;
import com.azure.identity.DefaultAzureCredentialBuilder;

public class UploadObject2 {

    public static void main(String[] args) throws IOException {
        uploadInputStream("ragbagTextFile3.txt");
    }

    public static void uploadInputStream(String fileObjectKeyName) throws IOException {
        File initialFile = new File("src/main/webapp/service_provider_pfp_images/abc.txt");
        InputStream targetStream = new FileInputStream(initialFile);

        String containerName = "testbucket3rag";
        // Replace with your actual connection string
        String connectionString = "DefaultEndpointsProtocol=https;AccountName=yourstorageaccount;AccountKey=youraccountkey;EndpointSuffix=core.windows.net";
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(fileObjectKeyName);

        Map<String, String> metadata = new HashMap<>();
        metadata.put("x-ms-meta-myVal", "test");

        BlobHttpHeaders headers = new BlobHttpHeaders().setContentType("text/plain");
        BlobParallelUploadOptions options = new BlobParallelUploadOptions(targetStream)
                .setHeaders(headers)
                .setMetadata(metadata);

        System.out.println("stream available is " + targetStream.available());
        blobClient.uploadWithResponse(options, null, null);
    }

    public static void uploadImage(String fileObjectKeyName, InputStream stream) throws IOException {
        String containerName = "testbucket3rag";
        // Replace with your actual connection string
        String connectionString = "DefaultEndpointsProtocol=https;AccountName=yourstorageaccount;AccountKey=youraccountkey;EndpointSuffix=core.windows.net";
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(fileObjectKeyName);

        BlobHttpHeaders headers = new BlobHttpHeaders().setContentType("image/jpeg");
        BlobParallelUploadOptions options = new BlobParallelUploadOptions(stream)
                .setHeaders(headers);

        blobClient.uploadWithResponse(options, null, null);
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
}
