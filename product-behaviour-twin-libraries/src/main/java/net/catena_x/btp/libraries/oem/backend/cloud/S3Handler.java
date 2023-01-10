package net.catena_x.btp.libraries.oem.backend.cloud;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.*;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
@Scope("singleton")
public class S3Handler {
    @Autowired
    @Qualifier("defaultMinio") private MinioClient client;
    @Value("cloud.bucket") private String bucket;

    @Bean("defaultMinio")
    private static MinioClient generateClient(
        @Value("cloud.endpoint") URL endpoint,
        @Value("cloud.accessKey") String accessKey,
        @Value("cloud.secretKey") String secretKey,
        @Value("cloud.region") String region,
        @Value("cloud.bucket") String bucket) {

        return MinioClient.builder()
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
    public InputStream streamFileFromCloud(String key) throws IOException, MinioException,
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
        return om.readValue(streamFileFromCloud(key), resultType);
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
        boolean found = client.bucketExists(BucketExistsArgs.builder().bucket("asiatrip").build());
        if (!found) {
            // Make a new bucket called 'asiatrip'.
            client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        } else {
            System.out.println("Bucket " + bucket + " already exists.");
        }
    }
}