package net.catena_x.btp.libraries.oem.backend.datasource.provider.dataupdaterapi;

import net.catena_x.btp.libraries.bamm.digitaltwin.DigitalTwin;
import net.catena_x.btp.libraries.bamm.testdata.TestData;
import net.catena_x.btp.libraries.oem.backend.datasource.model.api.ApiResult;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputTelematicsData;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.model.TestDataCategorized;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.util.CatenaXIdToDigitalTwinType;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.UncheckedDataProviderException;
import okhttp3.HttpUrl;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Component
public class TelematicsDataUpdater {
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired private DataUpdaterApi dataUpdaterApi;
    @Autowired private TestDataCategorized testDataCategorized;

    public void updateFromTestData(@NotNull TestData testData) throws DataProviderException {
        testDataCategorized.initFromTestData(testData);
        updateFromTestDataCetegorized(testDataCategorized);
    }

    public void updateFromTestDataCetegorized(@NotNull TestDataCategorized testDataCategorized)
            throws DataProviderException {
        final HashMap<String, DigitalTwin> vehicles = testDataCategorized.getDigitalTwinsVehicles();

        final CatenaXIdToDigitalTwinType idToType = (String catenaXId)-> testDataCategorized.catenaXIdToType(catenaXId);

        vehicles.entrySet().stream().forEachOrdered((vehicleEntry) -> {
            try {
                updateTelematicsData(vehicleEntry.getValue(), idToType);
            } catch (final DataProviderException exception) {
                throw new UncheckedDataProviderException(exception);
            }
        });
    }

    private void updateTelematicsData(@NotNull final DigitalTwin vehicle,
                                      @NotNull final CatenaXIdToDigitalTwinType idToType)
            throws DataProviderException {

        assertTelematicsDataPresent(vehicle);

        callTelematicsDataUpdateService(new InputTelematicsData(
                vehicle.getCatenaXId(), vehicle.getClassifiedLoadSpectra(),
                vehicle.getAdaptionValues()));
    }

    private void assertTelematicsDataPresent(@NotNull final DigitalTwin vehicle) throws DataProviderException {
        if((vehicle.getClassifiedLoadSpectra() == null) || (vehicle.getAdaptionValues() == null)) {
            throw new DataProviderException("Missing telematics data in vehicle twin!");
        }

        if(vehicle.getClassifiedLoadSpectra().isEmpty() || vehicle.getAdaptionValues().isEmpty()) {
            throw new DataProviderException("Missing telematics data in vehicle twin!");
        }
    }

    private void callTelematicsDataUpdateService(@NotNull final InputTelematicsData telematicsData)
            throws DataProviderException {

        final HttpUrl requestUrl = HttpUrl.parse(
                dataUpdaterApi.getRawdataApiBaseUrl()).newBuilder().
                addPathSegment("telematicsdata").addPathSegment("add").build();

        final HttpHeaders headers = dataUpdaterApi.generateDefaultHeaders();

        dataUpdaterApi.addAuthorizationHeaders(headers);

        final HttpEntity<InputTelematicsData> request = new HttpEntity<>(telematicsData, headers);

        final ResponseEntity<ApiResult> response = restTemplate.postForEntity(
                requestUrl.toString(), request, ApiResult.class);

        dataUpdaterApi.checkResponse(response);
    }
}
