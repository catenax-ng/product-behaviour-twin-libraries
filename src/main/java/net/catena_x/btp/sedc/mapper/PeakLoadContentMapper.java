package net.catena_x.btp.sedc.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.libraries.util.json.ObjectMapperFactoryBtp;
import net.catena_x.btp.sedc.model.PeakLoadRawValues;
import net.catena_x.btp.sedc.model.PeakLoadResult;
import net.catena_x.btp.sedc.protocol.model.ContentMapperInterface;
import net.catena_x.btp.sedc.protocol.model.blocks.DataBlock;
import net.catena_x.btp.sedc.protocol.model.blocks.HeaderBlock;

import javax.validation.constraints.NotNull;

public class PeakLoadContentMapper implements ContentMapperInterface {
    final ObjectMapper objectMapper = ObjectMapperFactoryBtp.createObjectMapper();

    @Override
    public DataBlock deserialize(@NotNull final String serializedBlock, @NotNull final String contentType)
            throws BtpException {
        try {
            if (contentType.equals("PeakLoadRawValues")) {
                return new DataBlock(objectMapper.readValue(serializedBlock, PeakLoadRawValues.class));
            } else if (contentType.equals("PeakLoadResult")) {
                return new DataBlock(objectMapper.readValue(serializedBlock, PeakLoadResult.class));
            }
        } catch(final JsonProcessingException exception) {
            throw new BtpException(exception);
        }

        throw new BtpException("Unknown content type: \"" + contentType + "\"");
    }

    public DataBlock deserialize(@NotNull final String serializedBlock, @NotNull final Class<?> expectedType)
            throws BtpException {
        try {
            if (expectedType.equals(PeakLoadRawValues.class)) {
                return new DataBlock(objectMapper.readValue(serializedBlock, PeakLoadRawValues.class));
            } else if (expectedType.equals(PeakLoadResult.class)) {
                return new DataBlock(objectMapper.readValue(serializedBlock, PeakLoadResult.class));
            }
        } catch(final JsonProcessingException exception) {
            throw new BtpException(exception);
        }

        throw new BtpException("Unknown content type: \"" + expectedType + "\"");
    }

    @Override
    public <T> String serialize(@NotNull final HeaderBlock headerBlock, @NotNull final DataBlock<T> dataBlock)
            throws BtpException {
        try {
            if (headerBlock.getContentType().equals("PeakLoadRawValues")) {
                return objectMapper.writeValueAsString(dataBlock.getData());
            } else if (headerBlock.getContentType().equals("PeakLoadResult")) {
                return objectMapper.writeValueAsString(dataBlock.getData());
            }
        } catch(final JsonProcessingException exception) {
            throw new BtpException(exception);
        }

        throw new BtpException("Unknown content type: \"" + headerBlock.getContentType() + "\"");
    }
}
