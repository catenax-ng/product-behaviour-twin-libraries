package net.catena_x.btp.hi.oem.backend.hi_service.controller;

import net.catena_x.btp.hi.oem.backend.hi_service.controller.util.HIKnowledgeAgentTest;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(HIBackendApiConfig.KNOWLEDGEAGENT_API_PATH_BASE)
public class HIKnowledgeAgentIntegration {
    @Autowired private HIKnowledgeAgentTest knowledgeAgentTest;

    @GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DefaultApiResult> test() {
        return knowledgeAgentTest.run();
    }
}
