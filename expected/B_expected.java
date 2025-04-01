package com.fixmia.rag.controllers;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.models.BlobStorageException;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UploadObject2 {

    public static void main(String[] args) throws IOException {
        uploadInputStream("ragbagTextFile3.txt");
    }

    public static void uploadInputStream(String fileObjectKeyName) throws IOException {
        File initialFile = new File("src/main/webapp/service_provider_pfp_images/abc.txt");
        String containerName = "testbucket3rag";
        String endpoint = "https://<your-storage-account-name>.blob.core.windows.net";

        BlobClient blobClient = new BlobClientBuilder()
            .endpoint(endpoint)
            .credential(new DefaultAzureCredentialBuilder().build())
            .containerName(containerName)
            .blobName(fileObjectKeyName)
            .buildClient();

        Map<String, String> metadata = new HashMap<>();
        metadata.put("myVal", "test");

        try (InputStream targetStream = new FileInputStream(initialFile)) {
            byte[] data = targetStream.readAllBytes();
            blobClient.upload(new ByteArrayInputStream(data), data.length, true);
            blobClient.setMetadata(metadata);
            System.out.println("Successfully uploaded " + fileObjectKeyName + " to container " + containerName);
        }
    }

    public static void uploadImage(String fileObjectKeyName, InputStream stream) throws IOException {
        String containerName = "testbucket3rag";
        String endpoint = "https://<your-storage-account-name>.blob.core.windows.net";

        BlobClient blobClient = new BlobClientBuilder()
            .endpoint(endpoint)
            .credential(new DefaultAzureCredentialBuilder().build())
            .containerName(containerName)
            .blobName(fileObjectKeyName)
            .buildClient();

        byte[] data = stream.readAllBytes();
        blobClient.upload(new ByteArrayInputStream(data), data.length, true);
    }

    // Keep unused methods as-is per original code structure
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