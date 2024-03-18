package net.catena_x.btp.sedc.apps.oem.edrproxy.controller;

import net.catena_x.btp.libraries.edc.model.Edr;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.HashMap;

@RestController
@RequestMapping("/")
public class OemEdrProxyController {
    @Autowired private ApiHelper apiHelper;

    private HashMap<String, Edr> edrs = new HashMap<>();

    private final Logger logger = LoggerFactory.getLogger(OemEdrProxyController.class);

    @PostMapping(value = "/edrcallback", produces = MediaType.APPLICATION_JSON_VALUE)
    public synchronized ResponseEntity<DefaultApiResult> edrCallback(@RequestBody @NotNull final Edr edr) {
        logger.info("Received EDR \"" + edr.getId()
                + "\" for contract id \"" + edr.getContractId()
                + "\": Endpoint=\"" + edr.getEndpoint() + "\", "
                + "\": AuthCode=\"" + edr.getAuthCode() + "\", "
                + "\": AuthKey=\"" + edr.getAuthKey() + "\".");

        edrs.put(edr.getContractId(), edr);
        return apiHelper.ok("Ok.");
    }

    @GetMapping(value = "/edr/bycontract/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Edr> getByContractId
            (@PathVariable @NotNull final String contractId) {
        final Edr edr = edrs.get(contractId);

        if(edr == null) {
            logger.info("Request for unknown contract id \"" + contractId + "\"!");
            return new ResponseEntity(new Edr(), HttpStatus.NOT_FOUND);
        } else {
            logger.info("Request for contract id \"" + contractId + "\" successful.");
            return new ResponseEntity(edr, HttpStatus.OK);
        }
    }
}