package net.catena_x.btp.libraries.edc.api.controller;

import net.catena_x.btp.libraries.edc.model.Edr;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.sedc.apps.oem.edrproxy.controller.OemEdrProxyController;
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
@RequestMapping("/edr")
public class EdcEdrController {
    @Autowired
    private ApiHelper apiHelper;

    private HashMap<String, Edr> edrs = new HashMap<>();

    private final Logger logger = LoggerFactory.getLogger(OemEdrProxyController.class);

    @PostMapping(value = "/callback", produces = MediaType.APPLICATION_JSON_VALUE)
    public synchronized ResponseEntity<DefaultApiResult> edrCallback(@RequestBody @NotNull final Edr edr) {
        logger.info("Received EDR for id \"" + edr.getId()
                + "\": Endpoint=\"" + edr.getEndpoint() + "\", "
                + "\": AuthCode=\"" + edr.getAuthCode() + "\", "
                + "\": AuthKey=\"" + edr.getAuthKey() + "\".");

        edrs.put(edr.getId(), edr);
        return apiHelper.ok("Ok.");
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Edr> getByRequest(@PathVariable @NotNull final String id) {
        final Edr edr = edrs.get(id);

        if(edr == null) {
            logger.info("Request for unknown id \"" + id + "\"!");
            return new ResponseEntity(new Edr(), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(edr, HttpStatus.OK);
        }
    }

    public Edr getEdr(@NotNull final String id) throws BtpException {
        final Edr edr = edrs.get(id);

        if(edr == null) {
            throw new BtpException("Unknown edr id \"" + id + "\"!");
        }

        return edr;
    }
}