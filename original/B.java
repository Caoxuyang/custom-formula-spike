package com.fixmia.rag.controllers;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

public class UploadObject2 {

    public static void main(String[] args) throws IOException {
        uploadInputStream("ragbagTextFile3.txt");
    }

    public static void uploadInputStream(String fileObjectKeyName) throws IOException {
        File initialFile = new File("src/main/webapp/service_provider_pfp_images/abc.txt");
        InputStream targetStream = new FileInputStream(initialFile);

        String containerName = "testcontainer";
        String connectionString = "DefaultEndpointsProtocol=https;AccountName=yourstorageaccount;AccountKey=youraccountkey;EndpointSuffix=core.windows.net";

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(fileObjectKeyName);

        System.out.println("stream available is " + targetStream.available());
        blobClient.upload(targetStream, targetStream.available(), true);
    }

    public static void uploadImage(String fileObjectKeyName, InputStream stream) throws IOException {
        String containerName = "testcontainer";
        String connectionString = "DefaultEndpointsProtocol=https;AccountName=yourstorageaccount;AccountKey=youraccountkey;EndpointSuffix=core.windows.net";

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(fileObjectKeyName);

        blobClient.upload(stream, getInputStreamLength(stream), true);
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
