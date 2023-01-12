package net.catena_x.btp.libraries.oem.backend.cloud;

import okhttp3.HttpUrl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URL;

class S3UploaderTest {

    String accessKey = System.getenv("MINIO_ACCESS_KEY");
    String secretKey = System.getenv("MINIO_SECRET_KEY");

    // This requires a local minio-instance started seperately
    URL url = HttpUrl.parse("http://172.29.154.185:9000").url();

    private S3Uploader s3Uploader = new S3Uploader(url, accessKey, secretKey, "eu-west", "test-bucket");

    @AfterEach
    void tidyUp() throws Exception {
        s3Uploader.deleteBucket(true);
        Assertions.assertFalse(s3Uploader.checkBucketExists());
        System.out.println("Deleted bucket successfully!");
    }

    //@Test
    void streamFileFromS3() {
    }

    //@Test
    void parseJsonFromS3() {
    }

    @Test
    void uploadAndCreate() throws Exception {
        // the simple upload is also covered in this test!
        String test_json = "{\"hello\": \"world\"}";
        String key = "hello_world";
        s3Uploader.uploadAndCreate(test_json, key);
        System.out.println("Uploaded file successfully!");
    }

    @Test
    void createAndDeleteBucket() throws Exception {
        s3Uploader.createBucket();
        Assertions.assertTrue(s3Uploader.checkBucketExists());
        System.out.println("Created bucket successfully!");
    }
}