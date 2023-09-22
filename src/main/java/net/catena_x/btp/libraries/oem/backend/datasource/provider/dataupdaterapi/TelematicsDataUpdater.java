package net.catena_x.btp.libraries.oem.backend.datasource.provider.dataupdaterapi;

import net.catena_x.btp.libraries.bamm.digitaltwin.DigitalTwin;
import net.catena_x.btp.libraries.bamm.testdata.TestData;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputTelematicsData;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.TestdataCategorized;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.UncheckedDataProviderException;
import net.catena_x.btp.libraries.util.apihelper.ResponseChecker;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import net.catena_x.btp.libraries.util.datahelper.DataHelper;
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
public class TelematicsDataUpdater {
    @Autowired private RestTemplate restTemplate;
    @Autowired private DataUpdaterApi dataUpdaterApi;
    @Autowired private TestdataCategorized testDataCategorized;

    public void updateFromTestData(@NotNull TestData testData) throws DataProviderException {
        testDataCategorized.initFromTestData(testData);
        updateFromTestDataCetegorized(testDataCategorized);
    }

    public void updateFromTestDataCetegorized(@NotNull TestdataCategorized testDataCategorized)
            throws DataProviderException {
        final HashMap<String, DigitalTwin> vehicles = testDataCategorized.getDigitalTwinsVehicles();

        try {
            vehicles.entrySet().stream().forEachOrdered((vehicleEntry) -> {
                try {
                    updateTelematicsData(vehicleEntry.getValue());
                } catch (final Exception exception) {
                    throw new UncheckedDataProviderException(new DataProviderException(exception));
                }
            });
        } catch(final Exception exception) {
            throw new DataProviderException(exception);
        }
    }

    private void updateTelematicsData(@NotNull final DigitalTwin vehicle) throws BtpException {

        assertTelematicsDataPresent(vehicle);

        callTelematicsDataUpdateService(new InputTelematicsData(
                vehicle.getCatenaXId(), vehicle.getClassifiedLoadSpectra(),
                vehicle.getAdaptionValues()));
    }

    private void assertTelematicsDataPresent(@NotNull final DigitalTwin vehicle) throws DataProviderException {
        if(DataHelper.isNullOrEmpty(vehicle.getClassifiedLoadSpectra())
                        || DataHelper.isNullOrEmpty(vehicle.getAdaptionValues())) {
            throw new DataProviderException("Missing telematics data in vehicle twin!");
        }
    }

    private void callTelematicsDataUpdateService(@NotNull final InputTelematicsData telematicsData)
            throws BtpException {

        final HttpUrl requestUrl = HttpUrl.parse(
                dataUpdaterApi.getRawdataApiBaseUrl()).newBuilder().
                addPathSegment("telematicsdata").addPathSegment("add").build();

        final HttpHeaders headers = dataUpdaterApi.generateDefaultHeaders();

        dataUpdaterApi.addAuthorizationHeaders(headers);

        final HttpEntity<InputTelematicsData> request = new HttpEntity<>(telematicsData, headers);

        final ResponseEntity<DefaultApiResult> response = restTemplate.postForEntity(
                requestUrl.toString(), request, DefaultApiResult.class);

        ResponseChecker.checkResponse(response);
    }
}
