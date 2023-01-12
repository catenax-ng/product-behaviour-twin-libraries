package net.catena_x.btp.libraries.oem.backend.cloud;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteObject;
import org.hibernate.sql.Delete;
import org.springframework.boot.jta.atomikos.AtomikosDependsOnBeanFactoryPostProcessor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/*
Plan for Upload Module:

One instance iof S3Handler is associated with exactly one bucket, which it can freely manage
The main S3Handler is a simple Java Class, Spring Interactions are managed separately.

[x] upload - Only uploads a file (given as string) to a existing bucket
[x] createBucket - Creates the bucket (if it doesn't exist)
[x] uploadAndCreate - Creates bucket (if it doesn't exist) and uploads
[x] deleteBucket - Deletes the bucket
[ ] deleteKey - deletes a key inside the bucket
[ ] Spring Wrapper - See S3SpringWrapper.java
 */

public class S3Handler {
    private MinioClient client;
    private String bucket;

    public S3Handler(URL endpoint, String accessKey, String secretKey,
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
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public InputStream streamFileFromS3(String key) throws IOException, MinioException,
            NoSuchAlgorithmException, InvalidKeyException {
        return client.getObject(
                GetObjectArgs.builder()
                        .bucket(bucket)
                        .object(key)
                        .build()
        );
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
    public <T> T parseJsonFromS3(String bucketName, String key, Class<T> resultType) throws IOException, MinioException,
            NoSuchAlgorithmException, InvalidKeyException {
        ObjectMapper om = new ObjectMapper();
        return om.readValue(streamFileFromS3(key), resultType);
    }

    public void uploadFileToS3(String resultJson, String key) throws IOException, MinioException,
            NoSuchAlgorithmException, InvalidKeyException {

        // if(!client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build()))
        //    throw new IOException("Bucket does not exist!");

        var stream = new ByteArrayInputStream(resultJson.getBytes(StandardCharsets.UTF_8));
        client.putObject(PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(key)
                        .stream(stream, stream.available(), -1)
                        .contentType("application/json")
                        .build());
    }

    public void uploadAndCreate(String resultJson, String key) throws IOException, MinioException,
            NoSuchAlgorithmException, InvalidKeyException {
        createBucket();
        uploadFileToS3(resultJson, key);
    }

    public void createBucket() throws IOException, MinioException,
            NoSuchAlgorithmException, InvalidKeyException {
        boolean found = checkBucketExists();
        if (!found) {
            client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        } else {
            System.out.println("Bucket " + bucket + " already exists.");
        }
    }

    public boolean checkBucketExists() throws IOException, MinioException,
            NoSuchAlgorithmException, InvalidKeyException {
        return client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
    }

    public void deleteBucket(boolean allowNonEmpty) throws IOException, MinioException,
            NoSuchAlgorithmException, InvalidKeyException {
        if(allowNonEmpty) {
            List<DeleteObject> keys = new ArrayList<>();
            for(var e: client.listObjects(ListObjectsArgs.builder().bucket(bucket).build())) {
                keys.add(new DeleteObject(e.get().objectName()));
            }
            // the deletion is lazily evaluated, so as long as the result is not iterated, nothing is deleted...
            for(var e: client.removeObjects(RemoveObjectsArgs.builder().bucket(bucket).objects(keys).build())) {}
        }
        client.removeBucket(RemoveBucketArgs.builder().bucket(bucket).build());
    }
}