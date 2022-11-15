package net.catena_x.btp.libraries.oem.backend.datasource.provider.dataupdaterapi;

import net.catena_x.btp.libraries.bamm.digitaltwin.DigitalTwin;
import net.catena_x.btp.libraries.bamm.testdata.TestData;
import net.catena_x.btp.libraries.oem.backend.datasource.model.api.DataUpdaterStatus;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputTelematicsData;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.VehicleState;
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

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class TelematicsDataUpdater {
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired DataUpdaterApi dataUpdaterApi;
    @Autowired VehilceDataLoader vehilceDataLoader;
    @Autowired TestDataCategorized testDataCategorized;

    public void updateFromTestData(@NotNull TestData testData) throws DataProviderException {
        testDataCategorized.initFromTestData(testData);
        updateFromTestDataCetegorized(testDataCategorized);
    }

    public void updateFromTestDataCetegorized(@NotNull TestDataCategorized testDataCategorized)
            throws DataProviderException {
        final HashMap<String, DigitalTwin> vehicles = testDataCategorized.getDigitalTwinsVehicles();

        final CatenaXIdToDigitalTwinType idToType = (String catenaXId)-> {
            return testDataCategorized.catenaXIdToType(catenaXId);
        };

        vehicles.entrySet().stream().forEachOrdered((vehicleEntry) -> {
            try {
                updateTelematicsData(vehicleEntry.getValue(), idToType);
            } catch (DataProviderException exception) {
                throw new UncheckedDataProviderException(exception);
            }
        });
    }

    private void updateTelematicsData(@NotNull final DigitalTwin vehicle,
                                      @NotNull final CatenaXIdToDigitalTwinType idToType)
            throws DataProviderException {



        //FA: MockUp:
        final List<String> loadSpectra = new ArrayList<>();
        final List<double[]> adaptionValues = new ArrayList<>();
        final Instant creationTimestamp = null;
        final float mileage = 0.0f;
        final long operatingSeconds = 0;



        callTelematicsDataUpdateService(new InputTelematicsData(
                new VehicleState(vehicle.getCatenaXId(),
                        creationTimestamp, mileage, operatingSeconds),
                loadSpectra, adaptionValues));
    }

    private void callTelematicsDataUpdateService(@NotNull final InputTelematicsData telematicsData)
            throws DataProviderException {

        final HttpUrl requestUrl = HttpUrl.parse(dataUpdaterApi.getRawdataApiBaseUrl() + "/telematicsdata/add");
        final HttpHeaders headers = dataUpdaterApi.generateDefaultHeaders();

        dataUpdaterApi.addAuthorizationHeaders(headers);

        final HttpEntity<InputTelematicsData> request = new HttpEntity<>(telematicsData, headers);

        final ResponseEntity<DataUpdaterStatus> response = restTemplate.postForEntity(
                requestUrl.toString(), request, DataUpdaterStatus.class);

        dataUpdaterApi.checkResponse(response);
    }
}
