// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: Apache-2.0

package com.example.s3;

// snippet-start:[s3.java2.delete_many_objects.main]
// snippet-start:[s3.java2.delete_many_objects.import]


import com.azure.core.util.BinaryData;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.batch.BlobBatchClient;
import com.azure.storage.blob.batch.BlobBatchClientBuilder;
import com.azure.storage.blob.implementation.models.StorageErrorException;
import com.azure.storage.blob.models.DeleteSnapshotsOptionType;
import java.util.ArrayList;
// snippet-end:[s3.java2.delete_many_objects.import]

/**
 * Before running this Java V2 code example, set up your development
 * environment, including your credentials.
 * <p>
 * For more information, see the following documentation topic:
 * <p>
 * https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/get-started.html
 */

public class DeleteMultiObjects {
    public static void main(String[] args) {
        final String usage = """

            Usage:    
                <containerName> <storageAccountEndpoint>

            Where:
               containerName - The Azure Storage Blob container to delete the blobs from.
               storageAccountEndpoint - The endpoint of the Azure Storage Account
            """;

        if (args.length != 2) {
            System.out.println(usage);
            System.exit(1);
        }

        String containerName = args[0];
        String storageAccountEndpoint = args[1];

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .endpoint(storageAccountEndpoint)
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();

        deleteContainerBlobs(blobServiceClient, containerName);
    }

    /**
     * Deletes multiple blobs from an Azure Storage Blob container.
     *
     * @param blobServiceClient A Blob service client object.
     * @param containerName The name of the Azure Blob Storage to delete blobs from.
     */
    public static void deleteContainerBlobs(BlobServiceClient blobServiceClient, String containerName) {
        // Upload three sample blobs to the specified Azure Storage Blob container.
        ArrayList<String> blobUrls  = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            String blobName = "delete blob example " + i;
            BlobClient blobClient = blobServiceClient.getBlobContainerClient(containerName).getBlobClient(blobName);
           
            blobClient.upload(BinaryData.fromString(blobName));
            blobUrls.add(blobClient.getBlobUrl());
        }

        System.out.println(blobUrls.size() + " blobs successfully created.");

        // Delete multiple blobs in one batch.
        try {
            BlobBatchClient blobBatchClient = new BlobBatchClientBuilder(blobServiceClient).buildClient();
            blobBatchClient.deleteBlobs(blobUrls, DeleteSnapshotsOptionType.INCLUDE).forEach(response -> {
                // The forEach is necessary for the delete batch
            });
            System.out.println("Multiple blobs are deleted!");

        } catch (StorageErrorException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
// snippet-end:[s3.java2.delete_many_objects.main]
