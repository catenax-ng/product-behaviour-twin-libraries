package net.catena_x.btp.libraries.oem.backend.cloud;

import net.catena_x.btp.libraries.util.exceptions.S3Exception;
import okhttp3.HttpUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Component
public class S3SpringWrapper {
    private final HttpUrl endpoint;
    private final String accessKey;
    private final String secretKey;
    private final String defaultBucket;

    private HashMap<String, S3Uploader> uploaders;

    public S3SpringWrapper(
            @Value("${cloud.endpoint}") final String endpoint,
            @Value("${cloud.accessKey}") final String accessKey,
            @Value("${cloud.secretKey}") final String secretKey,
            @Value("${cloud.defaultbucket}") final String defaultBucket
    ) {
        this.endpoint = HttpUrl.parse(endpoint);
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.defaultBucket = defaultBucket;
        resetUploaders();
    }


    public static InputStream downloadFileViaLink(final HttpUrl downloadUrl) throws S3Exception {
        return S3Downloader.downloadFileViaLink(downloadUrl);
    }

    /**
     * Upload the string given by resultJson into a json file with the name given by key into an existing bucket
     * @param resultJson File contents
     * @param key Desired file name inside the bucket
     * @throws S3Exception if anything goes wrong during the upload
     */
    public void uploadFileToS3Default(final String resultJson, final String key) throws S3Exception {
        uploaders.get(defaultBucket).uploadFileToS3(resultJson, key);
    }

    /**
     * Upload the content given by stream into a file with the name given by key into an existing bucket
     * @param stream Stream with input data
     * @param key desired file name inside the bucket
     * @throws S3Exception if anything goes wrong during the upload
     */
    public void uploadFileToS3Default(final InputStream stream, final String key) throws S3Exception {
        uploaders.get(defaultBucket).uploadFileToS3(stream, key);
    }

    /**
     * Upload the string given by resultJson into a json file with the name given by key,
     * creating the corresponding bucket
     * @param resultJson File contents
     * @param key Desired file name inside the bucket
     * @throws S3Exception if anything goes wrong during the upload
     */
    public void uploadAndCreateDefault(final String resultJson, final String key) throws S3Exception {
        uploaders.get(defaultBucket).uploadAndCreate(resultJson, key);
    }

    /**
     * Upload the content given by stream into a file with the name given by key into an existing bucket,
     * creating the bucket
     * @param key desired file name inside the bucket
     * @throws S3Exception if anything goes wrong during the upload
     */
    public void uploadAndCreateDefault(final InputStream stream, final String key) throws S3Exception {
        uploaders.get(defaultBucket).uploadAndCreate(stream, key);
    }

    /**
     * Create the bucket associated with this instance of S3Uploader
     * @throws S3Exception if anything goes wrong, e.g. the bucket already exists
     */
    public void createDefaultBucket() throws S3Exception {
        uploaders.get(defaultBucket).createBucket();
    }

    /**
     * Check if the bucket associated with this instance of S3Uploader exists
     * @return true if the bucket exists, false if not
     * @throws S3Exception if anything goes wrong during the connection
     */
    public boolean checkDefaultBucketExists() throws S3Exception{
        return uploaders.get(defaultBucket).checkBucketExists();
    }

    /**
     * Delete the bucket associated with this instance of S3Uploader
     * @param allowNonEmpty if set to true, will also delete non-empty buckets (therefore deleting all included files)
     * @throws S3Exception if anything goes wrong during the connection
     */
    public void deleteDefaultBucket(final boolean allowNonEmpty) throws S3Exception {
        uploaders.get(defaultBucket).deleteBucket(allowNonEmpty);
    }

    /**
     * Generates a download URL (GET) (with included authentication) for a file inside the managed bucket
     * @param fileName name of the file inside the bucket.
     * @param expiry scalar indicating the time until the link will expire
     * @param expiryTimeUnit unit of the expiry scalar
     * @return HttpUrl for the download
     * @throws S3Exception if anything goes wrong during the connection
     */
    public HttpUrl getDownloadURLDefault(final String fileName, final int expiry, TimeUnit expiryTimeUnit)
            throws S3Exception {
        return uploaders.get(defaultBucket).getDownloadURL(fileName, expiry, expiryTimeUnit);
    }

    /**
     * Generates an upload URL (PUT) (with included authentication) for a file inside the managed bucket
     * @param fileName name of the file inside the bucket.
     * @param expiry scalar indicating the time until the link will expire
     * @param expiryTimeUnit unit of the expiry scalar
     * @return HttpUrl for the upload
     * @throws S3Exception if anything goes wrong during the connection
     */
    public HttpUrl getUploadURLDefault(final String fileName, final int expiry, final TimeUnit expiryTimeUnit)
            throws S3Exception {
        return uploaders.get(defaultBucket).getUploadURL(fileName, expiry, expiryTimeUnit);
    }

    /**
     * check if a file exists in the default bucket
     * @param fileName file name as string
     * @return true if file exisst
     * @throws S3Exception if anything goes wrong during the connection
     */
    public boolean checkFileExistsInDefaultBucket(final String fileName) throws S3Exception {
        return uploaders.get(defaultBucket).checkFileExists(fileName);
    }

    /**
     * Uses a given link to upload a file to a foreign S3 bucket.
     * @param uploadUrl upload URl, should be generated like in getUploadUrl
     * @param fileContents stream that gives the file contents
     * @throws S3Exception if anything goes wrong during the connection
     */
    public void uploadToForeignS3ViaLink(final HttpUrl uploadUrl, final InputStream fileContents) throws S3Exception {
        uploaders.get(defaultBucket).uploadToForeignS3ViaLink(uploadUrl, fileContents);
    }

    /**
     * Deletes a file from the managed bucket
     * @param fileName name of the file
     * @throws S3Exception if anything goes wrong during the connection, or the file does not exist
     */
    public void deleteFileFromS3Default(final String fileName) throws S3Exception {
        uploaders.get(defaultBucket).deleteFileFromS3(fileName);
    }


    // Methods for non-default buckets


    /**
     * Upload the string given by resultJson into a json file with the name given by key into an existing bucket
     * @param bucketName Name of the bucket to be used. If you only need one bucket, use the corresponding
     *                   default-method instead
     * @param resultJson File contents
     * @param key Desired file name inside the bucket
     * @throws S3Exception if anything goes wrong during the upload
     */
    public void uploadFileToS3(final String bucketName, final String resultJson, final String key) throws S3Exception {
        constructUploaderIfNotExists(bucketName);
        uploaders.get(bucketName).uploadFileToS3(resultJson, key);
    }

    /**
     * Upload the content given by stream into a file with the name given by key into an existing bucket
     * @param bucketName Name of the bucket to be used. If you only need one bucket, use the respective default-method instead
     * @param stream Stream with input data
     * @param key desired file name inside the bucket
     * @throws S3Exception if anything goes wrong during the upload
     */
    public void uploadFileToS3(final String bucketName, final InputStream stream, final String key)
            throws S3Exception {
        constructUploaderIfNotExists(bucketName);
        uploaders.get(bucketName).uploadFileToS3(stream, key);
    }

    /**
     * Upload the string given by resultJson into a json file with the name given by key,
     * creating the corresponding bucket
     * @param bucketName Name of the bucket to be used. If you only need one bucket, use the respective default-method instead
     * @param resultJson File contents
     * @param key Desired file name inside the bucket
     * @throws S3Exception if anything goes wrong during the upload
     */
    public void uploadAndCreate(final String bucketName, final String resultJson, final String key)
            throws S3Exception {
        constructUploaderIfNotExists(bucketName);
        uploaders.get(bucketName).uploadAndCreate(resultJson, key);
    }

    /**
     * Upload the content given by stream into a file with the name given by key into an existing bucket,
     * creating the bucket
     * @param bucketName Name of the bucket to be used. If you only need one bucket, use the respective default-method instead
     * @param key desired file name inside the bucket
     * @throws S3Exception if anything goes wrong during the upload
     */
    public void uploadAndCreate(final String bucketName, final InputStream stream, final String key)
            throws S3Exception {
        constructUploaderIfNotExists(bucketName);
        uploaders.get(bucketName).uploadAndCreate(stream, key);
    }

    /**
     * Creates a bucket
     * @param bucketName Name of the bucket to be used. If you only need one bucket, use the respective default-method instead
     * @throws S3Exception if anything goes wrong, e.g. the bucket already exists
     */
    public void createBucket(final String bucketName) throws S3Exception {
        constructUploaderIfNotExists(bucketName);
        uploaders.get(bucketName).createBucket();
    }

    /**
     * Check if a bucket exists
     * @param bucketName Name of the bucket to be used. If you only need one bucket, use the respective default-method instead
     * @return true if the bucket exists, false if not
     * @throws S3Exception if anything goes wrong during the connection
     */
    public boolean checkBucketExists(final String bucketName) throws S3Exception{
        constructUploaderIfNotExists(bucketName);
        return uploaders.get(bucketName).checkBucketExists();
    }

    /**
     * Deletes a bucket
     * @param bucketName Name of the bucket to be used. If you only need one bucket, use the respective default-method instead
     * @param allowNonEmpty if set to true, will also delete non-empty buckets (therefore deleting all included files)
     * @throws S3Exception if anything goes wrong during the connection
     */
    public void deleteBucket(final String bucketName, final boolean allowNonEmpty) throws S3Exception {
        constructUploaderIfNotExists(bucketName);
        uploaders.get(bucketName).deleteBucket(allowNonEmpty);
    }

    /**
     * Generates a download URL (GET) (with included authentication) for a file inside the bucket
     * @param bucketName Name of the bucket to be used. If you only need one bucket, use the respective default-method instead
     * @param fileName name of the file inside the bucket.
     * @param expiry scalar indicating the time until the link will expire
     * @param expiryTimeUnit unit of the expiry scalar
     * @return HttpUrl for the download
     * @throws S3Exception if anything goes wrong during the connection
     */
    public HttpUrl getDownloadURL(final String bucketName, final String fileName, final int expiry, TimeUnit expiryTimeUnit)
            throws S3Exception {
        constructUploaderIfNotExists(bucketName);
        return uploaders.get(bucketName).getDownloadURL(fileName, expiry, expiryTimeUnit);
    }

    /**
     * Generates an upload URL (PUT) (with included authentication) for a file inside the bucket
     * @param bucketName Name of the bucket to be used. If you only need one bucket, use the respective default-method instead
     * @param fileName name of the file inside the bucket.
     * @param expiry scalar indicating the time until the link will expire
     * @param expiryTimeUnit unit of the expiry scalar
     * @return HttpUrl for the upload
     * @throws S3Exception if anything goes wrong during the connection
     */
    public HttpUrl getUploadURL(final String bucketName, final String fileName, final int expiry,
                                final TimeUnit expiryTimeUnit) throws S3Exception {
        constructUploaderIfNotExists(bucketName);
        return uploaders.get(bucketName).getUploadURL(fileName, expiry, expiryTimeUnit);
    }

    /**
     * check if a file exists in the default bucket
     * @param bucketName name of the bucket as string
     * @param fileName file name as string
     * @return true if file exists
     * @throws S3Exception if anything goes wrong during the connection
     */
    public boolean checkFileExistsInBucket(final String bucketName, final String fileName) throws S3Exception {
        constructUploaderIfNotExists(bucketName);
        return uploaders.get(bucketName).checkFileExists(fileName);
    }

    /**
     * Deletes a file from the bucket
     * @param bucketName Name of the bucket to be used. If you only need one bucket, use the respective default-method instead
     * @param fileName name of the file
     * @throws S3Exception if anything goes wrong during the connection, or the file does not exist
     */
    public void deleteFileFromS3(final String bucketName, final String fileName) throws S3Exception {
        constructUploaderIfNotExists(bucketName);
        uploaders.get(bucketName).deleteFileFromS3(fileName);
    }

    /**
     * resets the internal storage - recreates the uploader for the default bucket and destroys all others
     */
    public void resetUploaders() {
        uploaders = new HashMap<>();
        uploaders.put(defaultBucket, new S3Uploader(endpoint, accessKey, secretKey, defaultBucket));
    }

    private void constructUploaderIfNotExists(final String bucketName) {
        if(!uploaders.containsKey(bucketName))
            uploaders.put(bucketName, new S3Uploader(endpoint, accessKey, secretKey, bucketName));
    }

}
