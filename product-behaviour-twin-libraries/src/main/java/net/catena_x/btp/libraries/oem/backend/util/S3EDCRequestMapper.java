package net.catena_x.btp.libraries.oem.backend.util;

public interface S3EDCRequestMapper {
    void storePendingRequest(String id, S3EDCRequestMetadata metadata);
    S3EDCRequestMetadata popPendingRequest(String id);
}
