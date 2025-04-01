// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: Apache-2.0

package com.example.s3;

// snippet-start:[s3.java2.s3_object_upload.metadata.main]
// snippet-start:[s3.java2.s3_object_upload.metadata.import]

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.implementation.models.StorageErrorException;

import java.util.HashMap;
import java.util.Map;
// snippet-end:[s3.java2.s3_object_upload.metadata.import]

/**
 * Before running this Java V2 code example, set up your development
 * environment, including your credentials.
 * <p>
 * For more information, see the following documentation topic:
 * <p>
 * https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/get-started.html
 */
public class PutObjectMetadata {
    public static void main(String[] args) {
        final String USAGE = """

            Usage:
              <containerName> <blobName> <filePath> <storageAccountEndpoint>\s

            Where:
              containerName - The Azure Blob Storage container to upload an blob into.
              blobName - The blob to upload (for example, book.pdf).
              filePath - The path where the file is located (for example, C:/Azure/book2.pdf).
              storageAccountEndpoint - The endpoint of the Azure Storage Account.\s
            """;

        if (args.length != 4) {
            System.out.println(USAGE);
            System.exit(1);
        }

        String containerName = args[0];
        String blobName = args[1];
        String filePath = args[2];
        String storageAccountEndpoint = args[3];
        System.out.println("Putting blob " + blobName + " into container " + containerName);
        System.out.println("  in container: " + containerName);
        
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .endpoint(storageAccountEndpoint)
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();

        putBlob(blobServiceClient, containerName, blobName, filePath);
    }

    /**
     * Uploads an blob to an Azure Blob Storage container with metadata.
     *
     * @param blobServiceClient the client used to interact with the Azure Blob Storage service
     * @param containerName the name of the container to upload the blob to
     * @param blobName the name of the blob to be uploaded
     * @param filePath the local file path of the blob to be uploaded
     */
    public static void putBlob(BlobServiceClient blobServiceClient, String containerName, String blobName, String filePath) {
        try {
            Map<String, String> metadata = new HashMap<>();
            metadata.put("author", "Mary Doe");
            metadata.put("version", "1.0.0.0");

            BlobClient blobClient = blobServiceClient.getBlobContainerClient(containerName).getBlobClient(blobName);
            blobClient.uploadFromFile(filePath, null, null, metadata, null, null, null);

            System.out.println("Successfully placed " + blobName + " into container " + containerName);

        } catch (StorageErrorException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
// snippet-end:[s3.java2.s3_object_upload.metadata.main]