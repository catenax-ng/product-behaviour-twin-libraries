package net.catena_x.btp.libraries.oem.backend.datasource.provider.dataupdaterapi;

import net.catena_x.btp.libraries.bamm.digitaltwin.DigitalTwin;
import net.catena_x.btp.libraries.bamm.testdata.TestData;
import net.catena_x.btp.libraries.oem.backend.datasource.model.api.ApiResult;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.model.TestDataCategorized;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.util.CatenaXIdToDigitalTwinType;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.util.VehicleDataLoader;
import net.catena_x.btp.libraries.oem.backend.datasource.model.registration.VehicleInfo;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.UncheckedDataProviderException;
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
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired private DataUpdaterApi dataUpdaterApi;
    @Autowired private VehicleDataLoader vehicleDataLoader;
    @Autowired private TestDataCategorized testDataCategorized;

    public void registerFromTestData(@NotNull TestData testData) throws DataProviderException {
        testDataCategorized.initFromTestData(testData);
        registerFromTestDataCetegorized(testDataCategorized);
    }

    public void registerFromTestDataCetegorized(@NotNull TestDataCategorized testDataCategorized)
            throws DataProviderException {
        final HashMap<String, DigitalTwin> vehicles = testDataCategorized.getDigitalTwinsVehicles();

        final CatenaXIdToDigitalTwinType idToType = (String catenaXId)-> {
            return testDataCategorized.catenaXIdToType(catenaXId);
        };

        vehicles.entrySet().stream().forEachOrdered((vehicleEntry) -> {
            try {
                registerVehicle(vehicleEntry.getValue(), idToType);
            } catch (final DataProviderException exception) {
                throw new UncheckedDataProviderException(exception);
            }
        });
    }

    private void registerVehicle(@NotNull final DigitalTwin vehicle, @NotNull final CatenaXIdToDigitalTwinType idToType)
            throws DataProviderException {

        callVehicleRegistrationService(new VehicleInfo(
                vehicleDataLoader.getCatenaXId(vehicle),
                vehicleDataLoader.getVan(vehicle),
                vehicleDataLoader.getGearboxID(vehicle, idToType),
                vehicleDataLoader.getProductionDate(vehicle)));
    }

    private void callVehicleRegistrationService(@NotNull final VehicleInfo vehicleInfo)
            throws DataProviderException {

        final HttpUrl requestUrl = HttpUrl.parse(dataUpdaterApi.getRawdataApiBaseUrl()).newBuilder()
                .addPathSegment("vehicle").addPathSegment("register").build();

        final HttpHeaders headers = dataUpdaterApi.generateDefaultHeaders();

        dataUpdaterApi.addAuthorizationHeaders(headers);

        final HttpEntity<VehicleInfo> request = new HttpEntity<>(vehicleInfo, headers);

        final ResponseEntity<ApiResult> response = restTemplate.postForEntity(
                requestUrl.toString(), request, ApiResult.class);

        dataUpdaterApi.checkResponse(response);
    }
}
