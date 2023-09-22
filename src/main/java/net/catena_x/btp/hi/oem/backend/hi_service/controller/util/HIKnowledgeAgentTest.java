package net.catena_x.btp.hi.oem.backend.hi_service.controller.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.catena_x.btp.hi.oem.backend.hi_service.receiver.HIResultProcessor;
import net.catena_x.btp.hi.oem.common.model.dao.knowledgeagent.HIKAInputsDAO;
import net.catena_x.btp.hi.oem.common.model.dao.knowledgeagent.HIKAOutputsDAO;
import net.catena_x.btp.hi.oem.common.model.dto.knowledgeagent.*;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.ResponseChecker;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.libraries.util.json.ObjectMapperFactoryBtp;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;

@Component
public class HIKnowledgeAgentTest {
    @Autowired private ApiHelper apiHelper;
    @Autowired private RestTemplate restTemplate;
    @Autowired @Qualifier(ObjectMapperFactoryBtp.EXTENDED_OBJECT_MAPPER) private ObjectMapper objectMapper;
    @Autowired private HIResultProcessor resultProcessor;
    @Autowired private HIKAInputsConverter hikaInputsConverter;
    @Autowired private HIKAOutputsConverter hikaAOutputsConverter;

    @Value("${knowledgeagent.api.username:foo}") private String knowledgeApiUsername;
    @Value("${knowledgeagent.api.password:bar}") private String knowledgeApiPassword;
    @Value("${knowledgeagent.api.address:https://bmw-agent-data.dev.demo.catena-x.net}")
    private String knowledgeApiAddress;

    private final Logger logger = LoggerFactory.getLogger(HIKnowledgeAgentTest.class);

    public ResponseEntity<DefaultApiResult> run() {
        return callService(generateTestInputs());
    }
    public ResponseEntity<DefaultApiResult> run(@NotNull final HIKAInputs inputs) {
        return callService(inputs);
    }

    private ResponseEntity<DefaultApiResult> callService(@NotNull final HIKAInputs inputs) {
        final HttpUrl requestUrl = HttpUrl.parse(knowledgeApiAddress)
                .newBuilder().addPathSegment("api").addPathSegment("agent")
                .addEncodedQueryParameter("asset", "urn:cx:Skill:oem:Health").build();
        final HttpHeaders headers = generateDefaultHeaders();
        addAuthorizationHeaders(headers);

        final HttpEntity<HIKAInputsDAO> request = new HttpEntity<>(hikaInputsConverter.toDAO(inputs), headers);

        try {
            System.out.println("=======================");
            System.out.println("HI input to agent:");
            System.out.println(objectMapper.writeValueAsString(request.getBody()));
            System.out.println("=======================");
        } catch (final Exception exception) {
            logger.error("Input to agent can not be showed: " + exception.getMessage());
        }

        final ResponseEntity<HIKAOutputsDAO> response =
                restTemplate.postForEntity(requestUrl.toString(), request, HIKAOutputsDAO.class);

        if(!checkAndShowResponse(response)) {
            return apiHelper.failed("Failed to run the knowledge agent!");
        }

        resultProcessor.process(convertResult(response.getBody()), inputs);

        return apiHelper.ok("Received data from Knowledge Agent. Processing started.");
    }

    private HIKAOutputs convertResult(@NotNull final HIKAOutputsDAO result) {
        return hikaAOutputsConverter.toDTO(result);
    }

    private boolean checkAndShowResponse(final ResponseEntity<HIKAOutputsDAO> response) {
        try {
            ResponseChecker.checkResponse(response);
            showResponse(response.getBody());
            return true;
        } catch (final BtpException exception) {
            logger.error(exception.getMessage());
            return false;
        }
    }

    private void showResponse(@Nullable final HIKAOutputsDAO response) {
        System.out.println("===========================================================================");
        if(response == null) {
            System.out.println("The knowledge agent response was null!");
        } else {
            System.out.println("The knowledge agent responded:");
            System.out.println("");
            System.out.println(resultToString(response));
            System.out.println("");
            System.out.println("===========================================================================");
        }
    }

    private void addAuthorizationHeaders(final HttpHeaders headers) {
        headers.add("Authorization", getAuthString());
    }

    private String getAuthString() {
        StringBuilder sb = new StringBuilder();

        String authStr = sb.append(knowledgeApiUsername).append(":").append(knowledgeApiPassword).toString();
        sb.setLength(0);
        sb.append("Basic ").append(Base64.getEncoder().encodeToString(authStr.getBytes()));
        return sb.toString();
    }

    protected HttpHeaders generateDefaultHeaders() {
        final HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Type", "application/sparql-results+json");
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    private HIKAInputs generateTestInputs() {
        final HIKAInputs inputs = new HIKAInputs();
        inputs.setRequests(new ArrayList<>(2));
        inputs.getRequests().add(new HIKAInput("FNKQHZHFTHMCRX"));
        inputs.getRequests().add(new HIKAInput("LKTYZWBNDOMPGQ"));
        return inputs;
    }

    private String resultToString(@NotNull HIKAOutputsDAO result) {
        try {
            return objectMapper.writerFor(HIKAOutputsDAO.class).writeValueAsString(result);
        } catch(final JsonProcessingException exception) {
            return "ERROR while converting result: " + exception.getMessage();
        }
    }
}
