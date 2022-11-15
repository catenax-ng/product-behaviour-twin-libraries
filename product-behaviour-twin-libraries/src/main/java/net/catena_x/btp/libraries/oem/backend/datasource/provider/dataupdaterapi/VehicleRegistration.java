package net.catena_x.btp.libraries.oem.backend.datasource.provider.dataupdaterapi;

import net.catena_x.btp.libraries.bamm.digitaltwin.DigitalTwin;
import net.catena_x.btp.libraries.bamm.testdata.TestData;
import net.catena_x.btp.libraries.oem.backend.datasource.model.api.DataUpdaterStatus;
import net.catena_x.btp.libraries.oem.backend.datasource.model.registration.VehicleInfo;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.testdata.model.TestDataCategorized;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.testdata.util.CatenaXIdToDigitalTwinType;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.testdata.util.VehilceDataLoader;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.UncheckedDataProviderException;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Component
public class VehicleRegistration extends DataUpdaterApiBase {
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired VehilceDataLoader vehilceDataLoader;
    @Autowired TestDataCategorized testDataCategorized;

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
            } catch (DataProviderException exception) {
                throw new UncheckedDataProviderException(exception);
            }
        });
    }

    private void registerVehicle(@NotNull final DigitalTwin vehicle, @NotNull final CatenaXIdToDigitalTwinType idToType)
            throws DataProviderException {

        callVehicleRegistrationService(new VehicleInfo(
                vehilceDataLoader.getCatenaXId(vehicle),
                vehilceDataLoader.getVan(vehicle),
                vehilceDataLoader.getGearboxID(vehicle, idToType),
                vehilceDataLoader.getProductionDate(vehicle)));
    }

    private void callVehicleRegistrationService(@NotNull final VehicleInfo vehicleInfo)
            throws DataProviderException {

        final HttpUrl requestUrl = HttpUrl.parse(getRawdataApiBaseUrl() + "/vehicle/register");
        final HttpHeaders headers = generateDefaultHeaders();

        addAuthorizationHeaders(headers);

        final HttpEntity<VehicleInfo> request = new HttpEntity<>(vehicleInfo, headers);

        final ResponseEntity<DataUpdaterStatus> response = restTemplate.postForEntity(
                requestUrl.toString(), request, DataUpdaterStatus.class);

        checkResponse(response);
    }
}
