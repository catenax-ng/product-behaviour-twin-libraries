package net.catena_x.btp.libraries.oem.backend.cloud;

import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import io.minio.messages.DeleteObject;
import net.catena_x.btp.libraries.util.exceptions.S3Exception;
import okhttp3.HttpUrl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

/*
Plan for Upload Module:

One instance iof S3Handler is associated with exactly one bucket, which it can freely manage
The main S3Handler is a simple Java Class, Spring Interactions are managed separately.

[x] upload - Only uploads a file (given as string) to a existing bucket
[x] createBucket - Creates the bucket (if it doesn't exist)
[x] uploadAndCreate - Creates bucket (if it doesn't exist) and uploads
[x] deleteBucket - Deletes the bucket
[x] deleteKey - deletes a key (~file) inside the bucket
[ ] Spring Wrapper - See S3SpringWrapper.java
[x] Generate url to download a specific file
[x] Generate URL to upload a file to a specific bucket
[x] Javadoc
[x] Confluence / Ark42
 */

public class S3Uploader {
    private final MinioClient client;
    private final String bucket;

    /**
     * Constructs Object that handles uploads to a fixed S3 (or MinIO) instance
     * @param endpoint URl to the S3 instance
     * @param accessKey AccessKey for the S3 instance
     * @param secretKey SecretKey for the S3 instance
     * @param region Region, used by AmazonS3 to select the nearest node. Not important for non-distributed instances
     * @param bucket Name of the bucket to be managed. Can (but doesn't need to) already exist.
     */
    public S3Uploader(final HttpUrl endpoint, final String accessKey, final String secretKey,
                      final String region, final String bucket) {
        this.bucket = bucket;
        client = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .region(region)
                .build();
    }

    /**
     * Constructs Object that handles uploads to a fixed S3 (or MinIO) instance
     * @param endpoint URl to the S3 instance
     * @param accessKey AccessKey for the S3 instance
     * @param secretKey SecretKey for the S3 instance
     * @param bucket Name of the bucket to be managed. Can (but doesn't need to) already exist.
     */
    public S3Uploader(final HttpUrl endpoint, final String accessKey, final String secretKey, final String bucket) {
        this.bucket = bucket;
        client = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    /**
     * Upload the string given by resultJson into a json file with the name given by key into an existing bucket
     * @param resultJson File contents
     * @param key Desired file name inside the bucket
     * @throws S3Exception if anything goes wrong during the upload
     */
    public void uploadFileToS3(final String resultJson, final String key) throws S3Exception {
        // Maybe this method should optionally also set retention - i.e. not be deletable for a set interval of time
        var stream = new ByteArrayInputStream(resultJson.getBytes(StandardCharsets.UTF_8));
        uploadFileToS3(stream, key);
    }

    /**
     * Upload the content given by stream into a file with the name given by key into an existing bucket
     * @param stream Stream with input data
     * @param key desired file name inside the bucket
     * @throws S3Exception if anything goes wrong during the upload
     */
    public void uploadFileToS3(final InputStream stream, final String key) throws S3Exception {
        // Maybe this method should optionally also set retention - i.e. not be deletable for a set interval of time
        try {
            client.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(key)
                    .stream(stream, stream.available(), -1)
                    .contentType("application/json")
                    .build());
        } catch (IOException | MinioException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new S3Exception(e.getMessage(), e.getCause());
        }
    }

    /**
     * Upload the string given by resultJson into a json file with the name given by key,
     * creating the corresponding bucket
     * @param resultJson File contents
     * @param key Desired file name inside the bucket
     * @throws S3Exception if anything goes wrong during the upload
     */
    public void uploadAndCreate(final String resultJson, final String key) throws S3Exception {
        createBucket();
        uploadFileToS3(resultJson, key);
    }

    /**
     * Upload the content given by stream into a file with the name given by key into an existing bucket,
     * creating the bucket
     * @param key desired file name inside the bucket
     * @throws S3Exception if anything goes wrong during the upload
     */
    public void uploadAndCreate(final InputStream stream, final String key) throws S3Exception {
        createBucket();
        uploadFileToS3(stream, key);
    }

    /**
     * Create the bucket associated with this instance of S3Uploader
     * @throws S3Exception if anything goes wrong, e.g. the bucket already exists
     */
    public void createBucket() throws S3Exception {
        try {
            client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        } catch (IOException | MinioException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new S3Exception(e.getMessage(), e.getCause());
        }
    }

    /**
     * Check if the bucket associated with this instance of S3Uploader exists
     * @return true if the bucket exists, false if not
     * @throws S3Exception if anything goes wrong during the connection
     */
    public boolean checkBucketExists() throws S3Exception{
        try {
            return client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        } catch (IOException | MinioException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new S3Exception(e.getMessage(), e.getCause());
        }
    }

    /**
     * Delete the bucket associated with this instance of S3Uploader
     * @param allowNonEmpty if set to true, will also delete non-empty buckets (therefore deleting all included files)
     * @throws S3Exception if anything goes wrong during the connection
     */
    public void deleteBucket(final boolean allowNonEmpty) throws S3Exception {
        try {
            if(allowNonEmpty) {
                List<DeleteObject> keys = new ArrayList<>();
                for(var e: client.listObjects(ListObjectsArgs.builder().bucket(bucket).build())) {
                    keys.add(new DeleteObject(e.get().objectName()));
                }
                // the deletion is lazily evaluated, so as long as the result is not iterated, nothing is deleted...
                var deleteErrors =
                        client.removeObjects(RemoveObjectsArgs.builder().bucket(bucket).objects(keys).build());
                for(var e: deleteErrors) {
                    // However, this iterator seems to be empty as long as everything worked well, so we may as well
                    // raise an exception here
                    throw new S3Exception("Deletion of elements in bucket failed: " + e.get().toString());
                }
            }
            client.removeBucket(RemoveBucketArgs.builder().bucket(bucket).build());
        } catch (IOException | MinioException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new S3Exception(e.getMessage(), e.getCause());
        }
    }

    /**
     * Generates a download URL (GET) (with included authentication) for a file inside the managed bucket
     * @param fileName name of the file inside the bucket.
     * @param expiry scalar indicating the time until the link will expire
     * @param expiryTimeUnit unit of the expiry scalar
     * @return HttpUrl for the download
     * @throws S3Exception if anything goes wrong during the connection
     */
    public HttpUrl getDownloadURL(final String fileName, final int expiry, TimeUnit expiryTimeUnit)
            throws S3Exception {
        if(!checkFileExists(fileName)) {
            throw new S3Exception("Target object does not exist!");
        }
        try {
            return HttpUrl.parse(
                    client.getPresignedObjectUrl(
                            GetPresignedObjectUrlArgs.builder()
                                    .method(Method.GET)
                                    .bucket(bucket)
                                    .object(fileName)
                                    .expiry(expiry, expiryTimeUnit)
                                    .build()
                    )
            );
        } catch (IOException | MinioException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new S3Exception(e.getMessage(), e.getCause());
        }
    }

    /**
     * Generates an upload URL (PUT) (with included authentication) for a file inside the managed bucket
     * @param fileName name of the file inside the bucket.
     * @param expiry scalar indicating the time until the link will expire
     * @param expiryTimeUnit unit of the expiry scalar
     * @return HttpUrl for the upload
     * @throws S3Exception if anything goes wrong during the connection
     */
    public HttpUrl getUploadURL(final String fileName, final int expiry, final TimeUnit expiryTimeUnit)
            throws S3Exception {
        if(checkFileExists(fileName)) {
            throw new S3Exception("Target object already exists!");
        }
        try {
            return HttpUrl.parse(
                    client.getPresignedObjectUrl(
                            GetPresignedObjectUrlArgs.builder()
                                    .method(Method.PUT)
                                    .bucket(bucket)
                                    .object(fileName)
                                    .expiry(expiry, expiryTimeUnit)
                                    .build()
                    )
            );
        } catch (IOException | MinioException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new S3Exception(e.getMessage(), e.getCause());
        }
    }

    public boolean checkFileExists(final String fileName) throws S3Exception {
        var iterable = client.listObjects(ListObjectsArgs.builder().bucket(bucket).build());
        var stream = StreamSupport.stream(iterable.spliterator(), false);
        return stream.anyMatch(e -> {
            try {
                return e.get().objectName().equals(fileName);
            } catch (Exception ex) {
                return false;
            }
        });
    }

    /**
     * Uses a given link to upload a file to a foreign S3 bucket.
     * @param uploadUrl upload URl, should be generated like in getUploadUrl
     * @param fileContents stream that gives the file contents
     * @throws S3Exception if anything goes wrong during the connection
     */
    public void uploadToForeignS3ViaLink(final HttpUrl uploadUrl, final InputStream fileContents) throws S3Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(uploadUrl.uri()).PUT(
            HttpRequest.BodyPublishers.ofInputStream(() -> fileContents)
        ).build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new S3Exception(e.getMessage(), e.getCause());
        }
        if(response.statusCode() != 200) {
            throw new S3Exception("Upload returned status code " + response.statusCode());
        }
    }

    /**
     * Deletes a file from the managed bucket
     * @param fileName name of the file
     * @throws S3Exception if anything goes wrong during the connection, or the file does not exist
     */
    public void deleteFileFromS3(final String fileName) throws S3Exception {
        try {
            client.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(fileName)
                            .build()
            );
        } catch (IOException | MinioException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new S3Exception(e.getMessage(), e.getCause());
        }
    }
}