// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: Apache-2.0

package com.example.s3;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.batch.BlobBatchClient;
import com.azure.storage.blob.batch.BlobBatchClientBuilder;

import java.util.ArrayList;
import java.util.List;

public class DeleteMultiObjects {
    public static void main(String[] args) {
        final String usage = """

            Usage:    <containerName>

            Where:
               containerName - the Azure Blob Storage container name.
            """;

        if (args.length != 1) {
            System.out.println(usage);
            System.exit(1);
        }

        String containerName = args[0];

        // Replace with your actual connection string
        String connectionString = "DefaultEndpointsProtocol=https;AccountName=yourstorageaccount;AccountKey=youraccountkey;EndpointSuffix=core.windows.net";

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
            .connectionString(connectionString)
            .buildClient();

        deleteContainerBlobs(blobServiceClient, containerName);
    }

    /**
     * Deletes multiple blobs from an Azure Blob Storage container.
     *
     * @param blobServiceClient An Azure Blob Service client object.
     * @param containerName The name of the Azure Blob Storage container to delete blobs from.
     */
    public static void deleteContainerBlobs(BlobServiceClient blobServiceClient, String containerName) {
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobBatchClient blobBatchClient = new BlobBatchClientBuilder(blobServiceClient).buildClient();

        // Upload three sample blobs to the specified Azure Blob Storage container.
        List<String> blobUrls = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            String blobName = "delete_blob_example_" + i;
            BlobClient blobClient = containerClient.getBlobClient(blobName);
            blobClient.uploadFromFile("sample-file-path-" + i); // Replace with actual file paths
            blobUrls.add(blobClient.getBlobUrl());
        }

        System.out.println(blobUrls.size() + " blobs successfully created.");

        // Delete multiple blobs in one request.
        try {
            blobBatchClient.deleteBlobs(blobUrls, null);
            System.out.println("Multiple blobs are deleted!");

        } catch (Exception e) {
            System.err.println("Error occurred while deleting blobs: " + e.getMessage());
            System.exit(1);
        }
    }
}
