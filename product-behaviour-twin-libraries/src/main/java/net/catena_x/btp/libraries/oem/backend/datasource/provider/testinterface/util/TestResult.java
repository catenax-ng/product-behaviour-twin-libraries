package net.catena_x.btp.libraries.oem.backend.datasource.provider.testinterface.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TestResult {
    private String contentType = "application/json";
    private byte[] result = "{\"info\": \"Result not initialized.\"}".getBytes();
}
