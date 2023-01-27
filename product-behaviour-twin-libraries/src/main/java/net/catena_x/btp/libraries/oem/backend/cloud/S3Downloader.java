package net.catena_x.btp.libraries.oem.backend.cloud;


import net.catena_x.btp.libraries.util.exceptions.S3Exception;
import okhttp3.HttpUrl;

import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class S3Downloader {

    /**
     * Class does not need to be instantiated
     */
    private S3Downloader() {}

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
