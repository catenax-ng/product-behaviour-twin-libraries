package net.catena_x.btp.libraries.oem.backend.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

// This class tries to be a thread-safe singleton with lazy initialization.
// lazy initialization from https://stackoverflow.com/questions/16106260/thread-safe-singleton-class
// thread safety information from https://stackoverflow.com/questions/70687454/how-i-can-implement-aws-s3-client-in-a-thread-safe-manner

// this is based on java aws sdk 1.x, as 2.x is not yet fully complete

// WARNING: This is completely untested and heavily WIP!

public class S3Handler {
    private final AmazonS3 client = AmazonS3Client.builder().build();

    private static class Holder {
        private static final S3Handler singletonInstance = new S3Handler();
    }

    private S3Handler() {
        // TODO assert that region and credentials are set
        //  see https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/setup-credentials.html
        //  assert that construction can not fail, else there will be NoClassDefFound Errors,
        //  see https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
    }

    /**
     * @return Singleton instance of S3Handler - will be initialized lazily.
     */
    public static S3Handler getInstance() {
        return Holder.singletonInstance;
    }

    // TODO bucket should probably be fixed in config
    public S3ObjectInputStream streamFileFromS3(String bucketName, String key) {
        return client.getObject(new GetObjectRequest(bucketName, key)).getObjectContent();
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
    public <T> T parseJsonFromS3(String bucketName, String key, Class<T> resultType) throws IOException {
        ObjectMapper om = new ObjectMapper();
        return om.readValue(streamFileFromS3(bucketName, key), resultType);
    }
}