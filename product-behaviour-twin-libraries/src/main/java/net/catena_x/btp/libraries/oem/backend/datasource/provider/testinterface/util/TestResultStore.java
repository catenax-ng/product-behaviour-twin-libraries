package net.catena_x.btp.libraries.oem.backend.datasource.provider.testinterface.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TestResultStore {
    private TestResult resultGetRequest = new TestResult();
    private TestResult resultPostRequest = new TestResult();

    public ResponseEntity<byte[]> makeGetResponse() {
        return new ResponseEntity<>(resultGetRequest.getResult(),
                getHeaders(resultGetRequest.getContentType()), HttpStatus.OK);
    }

    public ResponseEntity<byte[]> makePostResponse() {
        return new ResponseEntity<>(resultPostRequest.getResult(),
                getHeaders(resultGetRequest.getContentType()), HttpStatus.OK);
    }

    private static HttpHeaders getHeaders(@NotNull final String contentType) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", contentType);
        headers.add("Content-Disposition", "inline");
        return headers;
    }
}
