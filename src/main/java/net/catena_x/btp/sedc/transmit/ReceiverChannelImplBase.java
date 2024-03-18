package net.catena_x.btp.sedc.transmit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import net.catena_x.btp.libraries.edc.model.Edr;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.libraries.util.json.ObjectMapperFactoryBtp;
import net.catena_x.btp.sedc.apps.oem.backend.receiver.ResultReceiver;
import net.catena_x.btp.sedc.protocol.model.blocks.ConfigBlock;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.validation.constraints.NotNull;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public abstract class ReceiverChannelImplBase {
    @Getter private final RawBlockReceiver rawReceiver = new RawBlockReceiver();
    @Getter private String streamId = null;
    private final ObjectMapper objectMapper = ObjectMapperFactoryBtp.createObjectMapper();
    private final static Logger logger = LoggerFactory.getLogger(ReceiverChannelImplBase.class);

    public void open(@NotNull final Edr edr, @NotNull final ConfigBlock config,
                     @Nullable HttpHeaders headers) throws BtpException {
        if(headers == null) {
            headers = new HttpHeaders();
        }

        if(edr.getAuthKey() != null) {
            headers.add(edr.getAuthKey(), edr.getAuthCode());
        }
        logger.info("Received EDR: " + edr.getEndpoint());

        open(edr.getEndpoint(), config, headers);
    }

    private void open(@NotNull final String partnerStreamUrl,
                      @NotNull final ConfigBlock config,
                      @Nullable final HttpHeaders headers) throws BtpException {
        try {
            this.streamId = config.getStream().getStreamId();
            final URLConnection connection = establishConnection(partnerStreamUrl, headers);
//FA            sendConfigBlock(connection, config);
            final java.io.InputStream inputStream = connection.getInputStream();
            rawReceiver.init(new BufferedInputStream(inputStream));
        } catch (final IOException exception) {
            throw new BtpException(exception);
        }
    }

    private URLConnection establishConnection(@NotNull final String partnerStreamUrl,
                                              @Nullable final HttpHeaders headers) throws IOException {
        final URL url = new URL(partnerStreamUrl);
        final HttpURLConnection connection = (HttpURLConnection)url.openConnection();
//FA        connection.setRequestMethod("POST");
        connection.setRequestMethod("GET");

        connection.addRequestProperty("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        connection.addRequestProperty("Accept", MediaType.ALL_VALUE);

        if(headers != null) {
            headers.toSingleValueMap().forEach((key, value) -> connection.addRequestProperty(key, value));
        }

        connection.setDoInput(true);
//FA PROXYBODY       connection.setDoOutput(true);

        return connection;
    }

    private void sendConfigBlock(@NotNull final URLConnection connection,
                                 @NotNull final ConfigBlock configBlock) throws IOException {
        final java.io.OutputStream outputStream = connection.getOutputStream();
        byte[] requestBody = objectMapper.writeValueAsString(configBlock).getBytes("utf-8");
        outputStream.write(requestBody, 0, requestBody.length);
        outputStream.flush();
        outputStream.close();
    }
}