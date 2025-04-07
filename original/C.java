// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: Apache-2.0

package com.example.blob;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;

import java.io.File;
import java.util.Collections;
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

        String connectionString = "DefaultEndpointsProtocol=https;AccountName=yourstorageaccount;AccountKey=youraccountkey;EndpointSuffix=core.windows.net";
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
            .connectionString(connectionString)
            .buildClient();

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        putBlob(containerClient, blobName, filePath);
    }

    /**
     * Uploads a blob to an Azure Blob Storage container with metadata.
     *
     * @param containerClient the BlobContainerClient object used to interact with the Azure Blob Storage service
     * @param blobName the name of the blob to be uploaded
     * @param filePath the local file path of the blob to be uploaded
     */
    public static void putBlob(BlobContainerClient containerClient, String blobName, String filePath) {
        try {
            BlobClient blobClient = containerClient.getBlobClient(blobName);

            BlobHttpHeaders headers = new BlobHttpHeaders()
                .setContentType("application/octet-stream");

            Map<String, String> metadata = Collections.singletonMap("author", "Mary Doe");

            blobClient.uploadFromFile(filePath, true);
            blobClient.setHttpHeaders(headers);
            blobClient.setMetadata(metadata);

            System.out.println("Successfully placed " + blobName + " into container " + containerClient.getBlobContainerName());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
