package com.example.s3;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.options.BlobParallelUploadOptions;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PutObjectMetadata {
    public static void main(String[] args) {
        final String USAGE = """

            Usage:
              <containerName> <blobName> <filePath>\s

            Where:
              containerName - The Azure Blob Storage container to upload a blob into.
              blobName - The blob to upload (for example, book.pdf).
              filePath - The path where the file is located (for example, C:/Azure/book2.pdf).\s
            """;

        if (args.length != 3) {
            System.out.println(USAGE);
            System.exit(1);
        }

        String containerName = args[0];
        String blobName = args[1];
        String filePath = args[2];
        System.out.println("Putting blob " + blobName + " into container " + containerName);
        System.out.println("  in container: " + containerName);

        String endpoint = "https://yourstorageaccount.blob.core.windows.net";
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
            .endpoint(endpoint)
            .credential(new DefaultAzureCredentialBuilder().build())
            .buildClient();

        putBlobObject(blobServiceClient, containerName, blobName, filePath);
    }

    /**
     * Uploads a blob to an Azure Blob Storage container with metadata.
     *
     * @param blobServiceClient the BlobServiceClient object used to interact with Azure Blob Storage
     * @param containerName the name of the container to upload the blob to
     * @param blobName the name of the blob to be uploaded
     * @param filePath the local file path of the blob to be uploaded
     */
    public static void putBlobObject(BlobServiceClient blobServiceClient, String containerName, String blobName, String filePath) {
        try {
            Map<String, String> metadata = new HashMap<>();
            metadata.put("author", "Mary Doe");
            metadata.put("version", "1.0.0.0");

            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            BlobClient blobClient = containerClient.getBlobClient(blobName);

            BlobParallelUploadOptions options = new BlobParallelUploadOptions(new File(filePath))
                .setMetadata(metadata);

            blobClient.uploadWithResponse(options, null, null);
            System.out.println("Successfully placed " + blobName + " into container " + containerName);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
