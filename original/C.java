package com.example.s3;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.AccessTier;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.BlobRequestConditions;
import com.azure.storage.blob.models.ParallelTransferOptions;

import java.io.File;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
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

        // Replace with your actual connection string
        String connectionString = "DefaultEndpointsProtocol=https;AccountName=yourstorageaccount;AccountKey=youraccountkey;EndpointSuffix=core.windows.net";
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
            .connectionString(connectionString)
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
            metadata.put("timestamp", Instant.now().toString());

            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            BlobClient blobClient = containerClient.getBlobClient(blobName);

            BlobHttpHeaders headers = new BlobHttpHeaders()
                .setContentLanguage("en-US")
                .setContentType("application/octet-stream");

            BlobRequestConditions requestConditions = new BlobRequestConditions()
                .setIfUnmodifiedSince(OffsetDateTime.now().minusDays(3));

            Long blockSize = 100L * 1024L * 1024L; // 100 MB
            ParallelTransferOptions parallelTransferOptions = new ParallelTransferOptions().setBlockSizeLong(blockSize);

            blobClient.uploadFromFile(filePath, parallelTransferOptions, headers, metadata,
                    AccessTier.HOT, requestConditions, Duration.ofSeconds(60));
            System.out.println("Successfully placed " + blobName + " into container " + containerName);

        } catch (UncheckedIOException ex) {
            System.err.printf("Failed to upload from file %s%n", ex.getMessage());
            System.exit(1);
        }
    }
}
