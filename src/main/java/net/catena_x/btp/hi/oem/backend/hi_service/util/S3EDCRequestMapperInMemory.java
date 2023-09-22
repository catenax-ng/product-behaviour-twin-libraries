package net.catena_x.btp.hi.oem.backend.hi_service.util;

import net.catena_x.btp.libraries.edc.util.S3EDCRequestMapper;
import net.catena_x.btp.libraries.edc.util.S3EDCRequestMetadata;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class S3EDCRequestMapperInMemory implements S3EDCRequestMapper {
    private final HashMap<String, HIS3EDCRequestMetadata> requestStorage = new HashMap<>();

    @Override
    public void storePendingRequest(String id, S3EDCRequestMetadata metadata) {
        HIS3EDCRequestMetadata convertedMetadata = (HIS3EDCRequestMetadata) metadata;
        requestStorage.put(id, convertedMetadata);
    }

    @Override
    public S3EDCRequestMetadata popPendingRequest(String id) throws IllegalArgumentException {
        if(!requestStorage.containsKey(id)) {
            throw new IllegalArgumentException("UUID " + id + " was not found in the storeage!");
        }
        var result = requestStorage.get(id);
        requestStorage.remove(id);
        return result;
    }
}
