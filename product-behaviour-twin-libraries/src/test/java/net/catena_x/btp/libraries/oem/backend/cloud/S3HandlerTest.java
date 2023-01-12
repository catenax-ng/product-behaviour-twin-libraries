package net.catena_x.btp.libraries.oem.backend.cloud;

import okhttp3.HttpUrl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class S3HandlerTest {

    String accessKey = System.getenv("MINIO_ACCESS_KEY");
    String secretKey = System.getenv("MINIO_SECRET_KEY");

    // This requires a local minio-instance started seperately
    URL url = HttpUrl.parse("http://172.29.154.185:9000").url();

    private S3Handler s3handler = new S3Handler(url, accessKey, secretKey, "eu-west", "test-bucket");

    @AfterEach
    void tidyUp() throws Exception {
        s3handler.deleteBucket(true);
        Assertions.assertFalse(s3handler.checkBucketExists());
        System.out.println("Deleted bucket successfully!");
    }

    @Test
    void streamFileFromS3() {
    }

    @Test
    void parseJsonFromS3() {
    }

    @Test
    void uploadAndCreate() throws Exception {
        // the simple upload is also covered in this test!
        String test_json = "{\"hello\": \"world\"}";
        String key = "hello_world";
        s3handler.uploadAndCreate(test_json, key);
        System.out.println("Uploaded file successfully!");
    }

    @Test
    void createAndDeleteBucket() throws Exception {
        s3handler.createBucket();
        Assertions.assertTrue(s3handler.checkBucketExists());
        System.out.println("Created bucket successfully!");
    }
}