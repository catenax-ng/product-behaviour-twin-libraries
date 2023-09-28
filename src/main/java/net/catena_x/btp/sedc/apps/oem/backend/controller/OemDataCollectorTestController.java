package net.catena_x.btp.sedc.apps.oem.backend.controller;

import net.catena_x.btp.libraries.edc.api.EdcApi;
import net.catena_x.btp.libraries.edc.model.CatalogRequest;
import net.catena_x.btp.libraries.edc.model.catalog.QuerySpec;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class OemDataCollectorTestController {
    @Autowired private ApiHelper apiHelper;
    @Autowired private EdcApi edcApi;

    @GetMapping(value = "/test", produces = "application/json")
    public ResponseEntity<DefaultApiResult> infoInit() {
        final CatalogRequest catalogRequest = new CatalogRequest();
        catalogRequest.setQuerySpec(new QuerySpec());
        catalogRequest.setProviderUrl("https://supplier-btp-test.dev.demo.catena-x.net:8282/api/v1/dsp");

        final String result = edcApi.requestCatalog(catalogRequest).getBody();

        return apiHelper.ok(result);
    }
}