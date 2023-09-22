package net.catena_x.btp.libraries.oem.backend.datasource.testdata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.ClassifiedLoadSpectrum;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.items.LoadSpectrumType;
import net.catena_x.btp.libraries.bamm.digitaltwin.DigitalTwin;
import net.catena_x.btp.libraries.bamm.testdata.TestData;
import net.catena_x.btp.libraries.bamm.util.DigitalTwinConverter;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.RuLTestdataInputElement;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.TestdataCategorized;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.TestdataConfig;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import net.catena_x.btp.libraries.util.datahelper.DataHelper;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.libraries.util.json.ObjectMapperFactoryBtp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class TestDataExporter {
    @Autowired @Qualifier(ObjectMapperFactoryBtp.EXTENDED_OBJECT_MAPPER) private ObjectMapper objectMapper;
    @Autowired DigitalTwinConverter digitalTwinConverter;
    @Autowired private TestDataReader testDataReader;

    private static final String POSTGRES_TIMESTAMP = "yyyy-MM-dd HH:mm:ss.SSSSSS";
    private static final DateTimeFormatter postgresTimestampFormatter = DateTimeFormatter.ofPattern(POSTGRES_TIMESTAMP)
                                                                                    .withZone(ZoneId.systemDefault());

    public void export(@NotNull final TestdataCategorized testDataCategorized, @NotNull final Path filename,
                       @NotNull final boolean useOldBammVersion,
                       @NotNull final boolean onlyfirstLoadSpectrumPerType,
                       @NotNull final boolean exportDamageAndRul) throws DataProviderException {
        if(DataHelper.isNullOrEmpty(testDataCategorized.getDigitalTwinsVehicles())) {
            return;
        }

        final TestdataCategorized testdataCategorizedCopy = getCopy(testDataCategorized);

        final Collection<DigitalTwin> vehicleTwins = testdataCategorizedCopy.getDigitalTwinsVehicles().values();
        final TestData testDataExport = new TestData();
        testDataExport.setDigitalTwins(new ArrayList<>(vehicleTwins.size()));

        for (final DigitalTwin vehicleTwin: vehicleTwins) {
            testDataExport.getDigitalTwins().add(vehicleTwin);
            testDataExport.getDigitalTwins().add(
                    testdataCategorizedCopy.getGearboxTwinFromVehicleTwinMustExists(vehicleTwin));
        }

        try {
            export(testDataExport, filename.toString(), useOldBammVersion,
                    onlyfirstLoadSpectrumPerType, exportDamageAndRul);
        } catch (final IOException exception) {
            throw new DataProviderException(exception);
        }
    }

    public void exportLimited(@NotNull final TestdataCategorized testDataCategorized,
                              @NotNull final Path baseFilename,
                              @NotNull final String fileextension,
                              @NotNull final int maxVehicleCountPerFile,
                              @NotNull final boolean useOldBammVersion,
                              @NotNull final boolean onlyFirstLoadSpectrumPerType,
                              @NotNull final boolean exportDamageAndRul) throws DataProviderException {
        if(DataHelper.isNullOrEmpty(testDataCategorized.getDigitalTwinsVehicles())) {
            return;
        }

        final TestdataCategorized testdataCategorizedCopy = getCopy(testDataCategorized);

        try {
            final Collection<DigitalTwin> vehicleTwins = testdataCategorizedCopy.getDigitalTwinsVehicles().values();
            final int fileCount = ((vehicleTwins.size() - 1) / maxVehicleCountPerFile) + 1;

            final TestData testDataExport = new TestData();
            testDataExport.setDigitalTwins(new ArrayList<>(maxVehicleCountPerFile));

            int count = 0;
            int fileIndex = 1;
            for (final DigitalTwin vehicleTwin: vehicleTwins) {
                testDataExport.getDigitalTwins().add(vehicleTwin);
                testDataExport.getDigitalTwins().add(
                        testdataCategorizedCopy.getGearboxTwinFromVehicleTwinMustExists(vehicleTwin));

                ++count;
                if(count >= maxVehicleCountPerFile) {
                    writeAndResetTestData(buildExportFilename(baseFilename, fileextension, fileIndex, fileCount),
                            maxVehicleCountPerFile, testDataExport, useOldBammVersion,
                            onlyFirstLoadSpectrumPerType, exportDamageAndRul);

                    count = 0;
                    ++fileIndex;
                }
            }

            if(count > 0) {
                writeAndResetTestData(buildExportFilename(baseFilename, fileextension, fileIndex, fileCount),
                        maxVehicleCountPerFile, testDataExport, useOldBammVersion,
                        onlyFirstLoadSpectrumPerType, exportDamageAndRul);
            }
        } catch (final IOException exception) {
            throw new DataProviderException(exception);
        }
    }

    public String exportToSqlFromConfig(@NotNull final TestdataConfig config, @NotNull final String schemaName)
            throws DataProviderException {
        return toSql(testDataReader.loadFromConfig(config), config.rulTestDataFiles(), schemaName);
    }

    private String toSql(@NotNull final TestData testData,
                         @NotNull final List<RuLTestdataInputElement> testDataElements,
                         @NotNull final String schemaName) throws DataProviderException {
        final StringBuilder sql = new StringBuilder();
        final List<String> telematicsIds = new ArrayList<>(testDataElements.size());

        for (int i = 0; i < testDataElements.size(); i++) {
            telematicsIds.add(java.util.UUID.randomUUID().toString());
        }

        appendInfoToSql(sql, schemaName);
        appendSyncToSql(sql, schemaName);
        appendTelematicsDataToSql(sql, testData, testDataElements, telematicsIds, schemaName);
        appendVehiclesToSql(sql, testData, testDataElements, telematicsIds, schemaName);
        appendVinRelationsToSql(sql, testDataElements, schemaName);

        return sql.toString();
    }

    private void sqlAppendFirstValue(@NotNull final StringBuilder sql, final boolean quotes,
                                     @NotNull final String value) {
        if(quotes) {
            sql.append('\'');
        }

        sql.append(value);

        if(quotes) {
            sql.append('\'');
        }
    }

    private void sqlAppendNextValue(@NotNull final StringBuilder sql, final boolean quotes,
                                    @NotNull final String value) {
        sql.append(", ");
        sqlAppendFirstValue(sql, quotes, value);
    }

    private void sqlStartInsert(@NotNull final StringBuilder sql, @NotNull final String schemaName,
                                @NotNull final String tableAndSchema) {
        final String insert = "INSERT INTO " + schemaName + ".info (key, query_timestamp, value) VALUES(";
        sql.append("INSERT INTO ");
        sql.append(schemaName);
        sql.append('.');
        sql.append(tableAndSchema);
        sql.append(" VALUES(");
    }

    private void sqlEndInsert(@NotNull final StringBuilder sql) {
        sql.append(");\n");
    }

    private void appendInfoToSql(@NotNull final StringBuilder sql, @NotNull final String schemaName) {
        final String nameAndSchema = "info (key, query_timestamp, value)";

        sqlStartInsert(sql, schemaName, nameAndSchema);
        sqlAppendFirstValue(sql, true, "DATAVERSION");
        sqlAppendNextValue(sql, false, "NULL");
        sqlAppendNextValue(sql, true, "DV_0.0.99");
        sqlEndInsert(sql);

        sqlStartInsert(sql, schemaName, nameAndSchema);
        sqlAppendFirstValue(sql, true, "LOADSPECTRUMINFO");
        sqlAppendNextValue(sql, false, "NULL");
        sqlAppendNextValue(sql, true, "{}");
        sqlEndInsert(sql);

        sqlStartInsert(sql, schemaName, nameAndSchema);
        sqlAppendFirstValue(sql, true, "ADAPTIONVALUEINFO");
        sqlAppendNextValue(sql, false, "NULL");
        sqlAppendNextValue(sql, true, "{\"names\" : [ \"AV1\", \"AV2\", \"AV3\", \"AV4\" ]}");
        sqlEndInsert(sql);
    }

    private void appendSyncToSql(@NotNull final StringBuilder sql, @NotNull final String schemaName) {
        final String nameAndSchema = "sync (id, query_timestamp, sync_counter)";

        sqlStartInsert(sql, schemaName, nameAndSchema);
        sqlAppendFirstValue(sql, true, "DEFAULT");
        sqlAppendNextValue(sql, true, "2023-02-06 12:53:39.659029");
        sqlAppendNextValue(sql, false, "0");
        sqlEndInsert(sql);
    }

    private void appendTelematicsDataToSql(@NotNull final StringBuilder sql, @NotNull final TestData testData,
                                           @NotNull final List<RuLTestdataInputElement> testDataElements,
                                           @NotNull final List<String> telematicsIds, @NotNull final String schemaName)
            throws DataProviderException {
        if(testDataElements.isEmpty()) {
            return;
        }

        final String nameAndSchema = "telematics_data (id, adaption_values, load_spectra, storage_timestamp, " +
                                                      "sync_counter, vehicle_id)";

        for (int i = 0; i < testDataElements.size(); i++) {
            final RuLTestdataInputElement element = testDataElements.get(i);
            final DigitalTwin vehicleTwin = getVehicleTwin(testData, element.vehicle().catenaxId());

            final StringBuilder loadspectra = new StringBuilder();
            loadspectra.append("[");

            if(vehicleTwin.getClassifiedLoadSpectra() != null) {
                for (int j = 0; j < vehicleTwin.getClassifiedLoadSpectra().size(); j++) {
                    if(j != 0) {
                        loadspectra.append(",");
                    }
                    try {
                        loadspectra.append(objectMapper.writeValueAsString(vehicleTwin.getClassifiedLoadSpectra().get(j)));
                    } catch (final JsonProcessingException exception) {
                        throw new DataProviderException("Error while converting load spectrum to json: "
                                + exception.getMessage());
                    }
                }
            }

            loadspectra.append("]");

            sqlStartInsert(sql, schemaName, nameAndSchema);
            sqlAppendFirstValue(sql, true, telematicsIds.get(i));
            sqlAppendNextValue(sql, true,
                    "[{\"status\":{\"date\":\"2022-08-11T00:00:00Z\",\"operatingHours\":3213.9," +
                            "\"mileage\":65432}," +
                            "\"values\":[93.74205525044785,242.28128457642228,94.39678926825131,241.28308963116186]}]");

            sqlAppendNextValue(sql, true, loadspectra.toString());
            sqlAppendNextValue(sql, true, "2023-02-06 12:52:46.784295");
            sqlAppendNextValue(sql, false, "0");
            sqlAppendNextValue(sql, true, element.vehicle().catenaxId());
            sqlEndInsert(sql);
        }
    }

    private void appendVehiclesToSql(@NotNull final StringBuilder sql, @NotNull final TestData testData,
                                     @NotNull final List<RuLTestdataInputElement> testDataElements,
                                     @NotNull final List<String> telematicsIds, @NotNull final String schemaName) {
        if(testData.getDigitalTwins().isEmpty()) {
            return;
        }

        final String nameAndSchema = "vehicles (vehicle_id, gearbox_id, newest_telematics_id, production_date, " +
                                               "sync_counter, update_timestamp, van)";

        for (int i = 0; i < testDataElements.size(); i++) {
            final RuLTestdataInputElement element = testDataElements.get(i);
            sqlStartInsert(sql, schemaName, nameAndSchema);
            sqlAppendFirstValue(sql, true, element.vehicle().catenaxId());
            sqlAppendNextValue(sql, true, element.gearbox().catenaxId());
            sqlAppendNextValue(sql, true, telematicsIds.get(i));
            sqlAppendNextValue(sql, true, toPostgresTimestamp(element.vehicle().manufacturingDate()));
            sqlAppendNextValue(sql, false, "0");
            sqlAppendNextValue(sql, true, "2023-02-06 12:52:46.784295");
            sqlAppendNextValue(sql, true, element.vehicle().van());
            sqlEndInsert(sql);
        }
    }

    private DigitalTwin getVehicleTwin(@NotNull final TestData testData, @NotNull final String catenaXId)
            throws DataProviderException {

        for (final DigitalTwin digitalTwin : testData.getDigitalTwins()) {
            if(digitalTwin.getCatenaXId().equals(catenaXId)) {
                return digitalTwin;
            }
        }

        throw new DataProviderException("Vehicle twin with id \"" + catenaXId + "\" not found in testdata!");
    }

    private String toPostgresTimestamp(@NotNull final Instant timestamp) {
        return postgresTimestampFormatter.format(timestamp);
    }

    private void appendVinRelationsToSql(@NotNull final StringBuilder sql,
                                         @NotNull final List<RuLTestdataInputElement> testDataElements,
                                         @NotNull final String schemaName) {
        if(testDataElements.isEmpty()) {
            return;
        }

        final String nameAndSchema = "vinrelations (vin, no_data, ref_id)";

        for (final RuLTestdataInputElement element : testDataElements) {
            sqlStartInsert(sql, schemaName, nameAndSchema);
            sqlAppendFirstValue(sql, true, element.vehicle().vin());
            sqlAppendNextValue(sql, false, (element.filename() == null) ? "TRUE" : "FALSE");
            sqlAppendNextValue(sql, true, element.vehicle().catenaxId());
            sqlEndInsert(sql);
        }
    }

    private void writeAndResetTestData(@NotNull final String filename, @NotNull final int maxVehicleCountPerFile,
                                       @NotNull final TestData testDataExport,
                                       @NotNull final boolean useOldBammVersion,
                                       @NotNull final boolean onlyFirstLoadSpectrumPerType,
                                       @NotNull final boolean exportDamageAndRul)
            throws IOException, DataProviderException {
        export(testDataExport, filename, useOldBammVersion, onlyFirstLoadSpectrumPerType, exportDamageAndRul);
        testDataExport.setDigitalTwins(new ArrayList<>(maxVehicleCountPerFile));
    }

    private String buildExportFilename(@NotNull final Path baseFilename, @NotNull final String fileextension,
                                       @NotNull final int fileIndex, @NotNull final int fileCount) {
        return baseFilename.toString() + "_" + fileIndex + "_of_" + fileCount + "." + fileextension;
    }

    private void export(@NotNull final TestData testData, @NotNull final String filename,
                        @NotNull final boolean useOldBammVersion,
                        @NotNull final boolean onlyFirstLoadSpectrumPerType,
                        @NotNull final boolean exportDamageAndRul) throws IOException, DataProviderException {
        if(onlyFirstLoadSpectrumPerType) {
            removeUnusedLoadSpectra(testData);
        }

        if(!exportDamageAndRul) {
            removeDamageAndRuLAspects(testData);
        }

        convertBammVersion(testData, useOldBammVersion);

        final File destinationFile = new File(filename);
        if(useOldBammVersion) {
            exportOldBamm(destinationFile, testData);
        }
        else {
            exportNewBamm(destinationFile, testData);
        }
    }

    private void exportNewBamm(final File destinationFile, @NotNull final TestData testData) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(destinationFile, testData);
    }

    private void exportOldBamm(final File destinationFile, @NotNull final TestData testData) throws IOException {
        final String testDataAsString =
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(testData).replace("Spectrum", "Collective");

        final FileWriter writer = new FileWriter(destinationFile);
        writer.write(testDataAsString);
        writer.flush();
        writer.close();
    }

    private void removeUnusedLoadSpectra(@NotNull final TestData testData) throws DataProviderException {
        for (final DigitalTwin digitalTwin : testData.getDigitalTwins()) {
            removeUnusedLoadSpectra(digitalTwin);
        }
    }

    private void removeUnusedLoadSpectra(@NotNull final DigitalTwin digitalTwin) throws DataProviderException {
        final List<ClassifiedLoadSpectrum> loadSpectra = digitalTwin.getClassifiedLoadSpectra();

        if(loadSpectra == null) {
            return;
        }

        int pos = 1;
        while(pos < loadSpectra.size()) {
            final LoadSpectrumType type = getLoadSpectrumType(loadSpectra.get(pos));
            for (int i = 0; i < pos; i++) {
                if(getLoadSpectrumType(loadSpectra.get(i)) == type) {
                    loadSpectra.remove(pos);
                    /* Resume next turn with same position (after for loop, ++pos is called). */
                    --pos;
                    break;
                }
            }

            ++pos;
        }
    }

    private void removeDamageAndRuLAspects(@NotNull final TestData testData) throws DataProviderException {
        for (final DigitalTwin digitalTwin : testData.getDigitalTwins()) {
            removeDamageAndRuLAspects(digitalTwin);
        }
    }

    private void removeDamageAndRuLAspects(@NotNull final DigitalTwin digitalTwin) throws DataProviderException {
        digitalTwin.setDamages(null);
        digitalTwin.setRemainingUsefulLifes(null);
    }

    private void convertBammVersion(@NotNull final TestData testData,
                                    @NotNull final boolean useOldBammVersion) throws DataProviderException {
        try {
            for (final DigitalTwin digitalTwin : testData.getDigitalTwins()) {
                if (useOldBammVersion) {
                    digitalTwinConverter.convertToOldBAMM(digitalTwin);
                } else {
                    digitalTwinConverter.convertToNewBAMM(digitalTwin);
                }
            }
        } catch(final BtpException exception) {
            throw new DataProviderException(exception);
        }
    }

    private LoadSpectrumType getLoadSpectrumType(@NotNull final ClassifiedLoadSpectrum loadSpectrum)
            throws DataProviderException {
        assertMetadata(loadSpectrum);
        return loadSpectrum.getMetadata().getComponentDescription();
    }

    private void assertMetadata(@NotNull final ClassifiedLoadSpectrum loadSpectrum) throws DataProviderException {
        if(loadSpectrum.getMetadata() == null) {
            throw new DataProviderException("Metadata in load spectrum is null!");
        }
    }

    private TestData getCopy(@NotNull final TestData testData) throws DataProviderException {
        if(testData == null) {
                return null;
        }

        try {
            return objectMapper.readValue(objectMapper.writeValueAsString(testData), TestData.class);
        } catch(final JsonProcessingException exception) {
            throw new DataProviderException(exception);
        }
    }

    private TestdataCategorized getCopy(@NotNull final TestdataCategorized testData) throws DataProviderException {
        if(testData == null) {
            return null;
        }

        try {
            final TestdataCategorized testDataCategorized =
                    objectMapper.readValue(objectMapper.writeValueAsString(testData), TestdataCategorized.class);

            testDataCategorized.initAutowiredFrom(testData);

            return testDataCategorized;
        } catch(final JsonProcessingException exception) {
            throw new DataProviderException(exception);
        }
    }
}
