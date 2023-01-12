package net.catena_x.btp.libraries.oem.backend.cloud;

import net.catena_x.btp.libraries.util.exceptions.BtpException;
import okhttp3.HttpUrl;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

class S3UploaderTest {

    String accessKey = System.getenv("MINIO_ACCESS_KEY");
    String secretKey = System.getenv("MINIO_SECRET_KEY");

    // This requires a local minio-instance started seperately
    URL url = HttpUrl.parse("https://dlr-minio.dev.demo.catena-x.net").url();
    String test_json = "{\"hello\": \"world\"}";

    private S3Uploader s3Uploader;

    @BeforeEach
    public void checkKeys() {
        if("".equals(accessKey) || "".equals(secretKey) || accessKey == null || secretKey == null) {
            System.err.println("Please set the accessKey and secretKey to MinIO/s3 via the environment variables " +
                    "MINIO_ACCESS_KEY and MINIO_SECRET_KEY");
            Assertions.fail();
        }
        s3Uploader = new S3Uploader(url, accessKey, secretKey, "eu-west", "test-bucket");
    }

    @AfterEach
    void tidyUp() throws Exception {
        s3Uploader.deleteBucket(true);
        Assertions.assertFalse(s3Uploader.checkBucketExists());
        System.out.println("Deleted bucket successfully!");
    }

    @Test
    void uploadAndCreateThenDownload() throws Exception {
        // the simple upload is also covered in this test!
        String key = "hello_world";
        s3Uploader.uploadAndCreate(test_json, key);
        System.out.println("Uploaded file successfully!");

        // now download via Link to verify file contents
        HttpUrl url = s3Uploader.getDownloadURL("hello_world", 1, TimeUnit.HOURS);
        HttpResponse<String> response = getStringHttpResponse(url);
        System.out.println("File contents were:");
        System.out.println(response.body());
        Assertions.assertEquals(test_json, response.body());
    }

    private HttpResponse<String> getStringHttpResponse(HttpUrl downloadUrl) throws BtpException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(downloadUrl.uri()).GET().build();
        HttpClient client = HttpClient.newHttpClient();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void createAndDeleteBucket() throws Exception {
        s3Uploader.createBucket();
        Assertions.assertTrue(s3Uploader.checkBucketExists());
        System.out.println("Created bucket successfully!");
    }

    @Test
    void getUploadLinkAndThenUploadFileViaLink() throws Exception {
        String file_name = "foreign-file.json";
        s3Uploader.createBucket();
        HttpUrl uploadUrl = s3Uploader.getUploadURL(file_name, 1, TimeUnit.MINUTES);
        InputStream stream = new ByteArrayInputStream(test_json.getBytes(StandardCharsets.UTF_8));
        s3Uploader.uploadToForeignS3ViaLink(uploadUrl, stream);

        // download manually again to verify
        HttpUrl url = s3Uploader.getDownloadURL(file_name, 1, TimeUnit.HOURS);
        HttpResponse<String> response = getStringHttpResponse(url);
        Assertions.assertEquals(test_json, response.body());
    }

    @Test
    void deleteFile() throws Exception {
        String file_name = "ishouldnotbehere.json";
        s3Uploader.uploadAndCreate(test_json, file_name);

        s3Uploader.deleteFileFromS3(file_name);

        HttpUrl download = s3Uploader.getDownloadURL(file_name, 1, TimeUnit.MINUTES);
        HttpResponse<String> response = getStringHttpResponse(download);
        Assertions.assertEquals(404, response.statusCode());
    }
}