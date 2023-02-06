package net.catena_x.btp.libraries.oem.backend.cloud;


import io.minio.DownloadObjectArgs;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import net.catena_x.btp.libraries.util.exceptions.S3Exception;
import okhttp3.HttpUrl;

import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class S3Downloader {

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
    public S3Downloader(final HttpUrl endpoint, final String accessKey, final String secretKey,
                      final String region, final String bucket) {
        this.bucket = bucket;
        client = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .region(region)
                .build();
    }

    public S3Downloader(final HttpUrl endpoint, final String accessKey, final String secretKey,
                        final String bucket) {
        this.bucket = bucket;
        client = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    /**
     * Upload the content given by stream into a file with the name given by key into an existing bucket
     * @param key file name inside the bucket
     * @throws S3Exception if anything goes wrong during the upload
     */
    public InputStream downloadFileFromS3(final String key) throws S3Exception {
        try {
            return client.getObject(GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(key)
                            .build());
        } catch (IOException | MinioException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new S3Exception(e.getMessage(), e.getCause());
        }
    }

    /**
     * Uses a given link to download a file from foreign S3 bucket.
     * @param downloadUrl download URl
     * @throws S3Exception if anything goes wrong during the connection
     */
    public static InputStream downloadFileViaLink(final HttpUrl downloadUrl) throws S3Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(downloadUrl.uri()).GET().build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<InputStream> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
        } catch (IOException | InterruptedException e) {
            throw new S3Exception(e.getMessage(), e.getCause());
        }
        if(response.statusCode() != 200) {
            throw new S3Exception("Download returned status code " + response.statusCode());
        }
        return response.body();
    }


}
