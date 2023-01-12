package net.catena_x.btp.libraries.oem.backend.cloud;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.DeleteObject;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import okhttp3.HttpUrl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
Plan for Upload Module:

One instance iof S3Handler is associated with exactly one bucket, which it can freely manage
The main S3Handler is a simple Java Class, Spring Interactions are managed separately.

[x] upload - Only uploads a file (given as string) to a existing bucket
[x] createBucket - Creates the bucket (if it doesn't exist)
[x] uploadAndCreate - Creates bucket (if it doesn't exist) and uploads
[x] deleteBucket - Deletes the bucket
[ ] deleteKey - deletes a key (~file) inside the bucket
[ ] Spring Wrapper - See S3SpringWrapper.java
[x] Generate url to download a specific file
[x] Generate URL to upload a file to a specific bucket
 */

public class S3Uploader {
    private MinioClient client;
    private String bucket;

    public S3Uploader(URL endpoint, String accessKey, String secretKey,
                      String region, String bucket) {

        this.bucket = bucket;
        client = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .region(region)
                .build();
    }


    /**
     * this should be used inside of a try...with-block
     * @param key
     * @return
     * @throws BtpException
     */
    public InputStream streamFileFromS3(String key) throws BtpException {
        try {
            return client.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(key)
                            .build()
            );
        } catch (IOException | MinioException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new BtpException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Downloads the Json-File, specified by bucketName and key, from S3 (configured via environent variables).
     * Then, parses the (json)-file into the supplied type result Type
     * @param bucketName
     * @param key
     * @param resultType
     * @param <T>
     * @return
     * @throws IOException
     */
    public <T> T parseJsonFromS3(String bucketName, String key, Class<T> resultType) throws BtpException {
        ObjectMapper om = new ObjectMapper();
        try {
            return om.readValue(streamFileFromS3(key), resultType);
        } catch (IOException e) {
            throw new BtpException(e.getMessage(), e.getCause());
        }

    }

    public void uploadFileToS3(String resultJson, String key) throws BtpException {

        // Maybe this method should optionally also set retention - i.e. not be deletable for a set interval of time

        var stream = new ByteArrayInputStream(resultJson.getBytes(StandardCharsets.UTF_8));
        try {
            client.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(key)
                    .stream(stream, stream.available(), -1)
                    .contentType("application/json")
                    .build());
        } catch (IOException | MinioException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new BtpException(e.getMessage(), e.getCause());
        }

    }

    public void uploadAndCreate(String resultJson, String key) throws BtpException {
        createBucket();

        uploadFileToS3(resultJson, key);
    }

    public void createBucket() throws BtpException {
        try {
            boolean found = checkBucketExists();
            if (!found) {
                client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            } else {
                System.out.println("Bucket " + bucket + " already exists.");
            }
        } catch (IOException | MinioException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new BtpException(e.getMessage(), e.getCause());
        }
    }

    public boolean checkBucketExists() throws IOException, MinioException,
            NoSuchAlgorithmException, InvalidKeyException {
        return client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
    }

    public void deleteBucket(boolean allowNonEmpty) throws BtpException {
        try {
            if(allowNonEmpty) {
                List<DeleteObject> keys = new ArrayList<>();
                for(var e: client.listObjects(ListObjectsArgs.builder().bucket(bucket).build())) {
                    keys.add(new DeleteObject(e.get().objectName()));
                }
                // the deletion is lazily evaluated, so as long as the result is not iterated, nothing is deleted...
                for(var e: client.removeObjects(RemoveObjectsArgs.builder().bucket(bucket).objects(keys).build())) {}
            }
            client.removeBucket(RemoveBucketArgs.builder().bucket(bucket).build());
        } catch (IOException | MinioException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new BtpException(e.getMessage(), e.getCause());
        }
    }

    // Whats the best way to forward the expiry time?
    public HttpUrl getDownloadURL(String fileName, int expiry, TimeUnit expiryTimeUnit) throws BtpException {
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
            throw new BtpException(e.getMessage(), e.getCause());
        }
    }

    public HttpUrl getUploadURL(String fileName, int expiry, TimeUnit expiryTimeUnit) throws BtpException {
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
            throw new BtpException(e.getMessage(), e.getCause());
        }
    }
}