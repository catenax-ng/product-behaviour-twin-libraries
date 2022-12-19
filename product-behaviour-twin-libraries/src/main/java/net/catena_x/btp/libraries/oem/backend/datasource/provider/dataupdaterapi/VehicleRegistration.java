package net.catena_x.btp.libraries.oem.backend.datasource.provider.dataupdaterapi;

import net.catena_x.btp.libraries.bamm.digitaltwin.DigitalTwin;
import net.catena_x.btp.libraries.bamm.testdata.TestData;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.model.TestDataCategorized;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.util.CatenaXIdToDigitalTwinType;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.util.VehicleDataLoader;
import net.catena_x.btp.libraries.oem.backend.datasource.model.registration.VehicleInfo;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.UncheckedDataProviderException;
import net.catena_x.btp.libraries.util.apihelper.ResponseChecker;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import okhttp3.HttpUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.HashMap;

@Component
public class VehicleRegistration {
    @Autowired private DataUpdaterApi dataUpdaterApi;
    @Autowired private VehicleDataLoader vehicleDataLoader;
    @Autowired private TestDataCategorized testDataCategorized;
    @Autowired private RestTemplate restTemplate;

    public void registerFromTestData(@NotNull TestData testData) throws DataProviderException {
        testDataCategorized.initFromTestData(testData);
        registerFromTestDataCetegorized(testDataCategorized);
    }

    public void registerFromTestDataCetegorized(@NotNull TestDataCategorized testDataCategorized)
            throws DataProviderException {

        try {
            final HashMap<String, DigitalTwin> vehicles = testDataCategorized.getDigitalTwinsVehicles();
            final CatenaXIdToDigitalTwinType idToType =
                    (final String catenaXId) -> testDataCategorized.catenaXIdToType(catenaXId);

            sortVehicles(vehicles, idToType);
        } catch (final Exception exception) {
            throw new DataProviderException(exception);
        }
    }

    private void sortVehicles(@NotNull final HashMap<String, DigitalTwin> vehicles,
                              @NotNull final CatenaXIdToDigitalTwinType idToType) {
        vehicles.entrySet().stream().forEachOrdered((vehicleEntry) -> {
            try {
                registerVehicle(vehicleEntry.getValue(), idToType);
            } catch (final BtpException exception) {
                throw new UncheckedDataProviderException(new DataProviderException(exception));
            }
        });
    }

    private void registerVehicle(@NotNull final DigitalTwin vehicle, @NotNull final CatenaXIdToDigitalTwinType idToType)
            throws BtpException {

        callVehicleRegistrationService(new VehicleInfo(
                vehicleDataLoader.getCatenaXId(vehicle),
                vehicleDataLoader.getVan(vehicle),
                vehicleDataLoader.getGearboxID(vehicle, idToType),
                vehicleDataLoader.getProductionDate(vehicle)));
    }

    private void callVehicleRegistrationService(@NotNull final VehicleInfo vehicleInfo) throws BtpException {

        final HttpUrl requestUrl = getVehicleRegistrationUrl();
        final HttpHeaders headers = dataUpdaterApi.generateDefaultHeaders();

        dataUpdaterApi.addAuthorizationHeaders(headers);

        final HttpEntity<VehicleInfo> request = new HttpEntity<>(vehicleInfo, headers);
        final ResponseEntity<DefaultApiResult> response = restTemplate.postForEntity(
                requestUrl.toString(), request, DefaultApiResult.class);

        ResponseChecker.checkResponse(response);
    }

    private HttpUrl getVehicleRegistrationUrl() {
        return HttpUrl.parse(dataUpdaterApi.getRawdataApiBaseUrl()).newBuilder()
                .addPathSegment("vehicle").addPathSegment("register").build();
    }
}
