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
        edrs.put(edr.getId(), edr);
        return apiHelper.ok("Ok.");
    }

    @GetMapping(value = "/edr/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Edr> input(@PathVariable @NotNull final String id) {
        final Edr edr = edrs.get(id);

        if(edr == null) {
            logger.info("Request for unknown id \"" + id + "\"!");
            return new ResponseEntity(new Edr(), HttpStatus.NOT_FOUND);
        } else {
            logger.info("Request for id \"" + id + "\" successful.");
            return new ResponseEntity(edr, HttpStatus.OK);
        }
    }
}