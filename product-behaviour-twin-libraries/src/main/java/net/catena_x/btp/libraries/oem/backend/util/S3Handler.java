package net.catena_x.btp.libraries.oem.backend.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Value;
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
    private MinioClient client;
    private String bucket;


    private S3Handler(
            @Value("cloud.endpoint") URL endpoint,
            @Value("cloud.accessKey") String accessKey,
            @Value("cloud.secretKey") String secretKey,
            @Value("cloud.region") String region,
            @Value("cloud.bucket") String bucket
    ) {
        client = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .region(region)
                .build();
        this.bucket = bucket;
    }

    /**
     * this should be used inside of a try...with-block
     * @param key
     * @return
     * @throws IOException
     * @throws MinioException
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
        // client.putObject(bucketName, key, resultJson);
        var stream = new ByteArrayInputStream(resultJson.getBytes(StandardCharsets.UTF_8));
        client.putObject(PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(key)
                        .stream(stream, stream.available(), -1)
                        .contentType("application/json")
                        .build());
    }
}