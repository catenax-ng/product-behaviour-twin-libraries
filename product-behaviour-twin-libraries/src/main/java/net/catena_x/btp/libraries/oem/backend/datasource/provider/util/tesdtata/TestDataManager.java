package net.catena_x.btp.libraries.oem.backend.datasource.provider.util.tesdtata;

import net.catena_x.btp.libraries.bamm.testdata.TestData;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.TestDataReader;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.testdata.model.TestDataCategorized;
import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.nio.file.Path;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class TestDataManager {
    @Autowired private TestDataReader testDataReader;
    @Autowired private TestDataCategorized testDataCategorized;

    @Value("${services.dataprovider.testdata.file}")
    private String testDataFile;

    @Value("${services.dataprovider.testdata.clutchSpectrumGreen:#{null}}")
    private String clutchSpectrumGreen;

    @Value("${services.dataprovider.testdata.clutchSpectrumYellow:#{null}}")
    private String clutchSpectrumYellow;

    @Value("${services.dataprovider.testdata.clutchSpectrumRed:#{null}}")
    private String clutchSpectrumRed;

    private ReentrantLock testDataMutex = new ReentrantLock();
    private TestData testData = null;

    public void lock() {
        testDataMutex.lock();
    }

    public void unlock() {
        testDataMutex.unlock();
    }

    public TestData getTestData() throws DataProviderException {
        getTestDataCategorized(null, true);
        return testData;
    }

    public synchronized void reset() {
        lock();
        testDataCategorized.reset();
        testData = null;
        unlock();
    }

    public synchronized TestDataCategorized getTestDataCategorized(@Nullable final String testDataJson,
                                                                   @NotNull boolean resetTestdata)
            throws DataProviderException {

        if(testDataJson != null) {
            return getDataCategorizedFromJson(testDataJson, resetTestdata);
        }

        if(!testDataCategorized.isInitialized()) {
            initTestDataCategorized();
        }

        return testDataCategorized;
    }

    private void initTestDataCategorized() throws DataProviderException {
        testData = testDataReader.loadFromFiles(Path.of(testDataFile),
                Path.of(clutchSpectrumGreen), Path.of(clutchSpectrumYellow), Path.of(clutchSpectrumRed));

        testDataCategorized.initFromTestData(testData);
    }

    private TestDataCategorized getDataCategorizedFromJson(
            @NotNull final String testDataJson, @NotNull final boolean resetTestdata) throws DataProviderException {

        if(resetTestdata || testData == null) {
            testData = testDataReader.loadFromJson(testDataJson);
        } else {
            testDataReader.appendFromJson(testData, testDataJson);
        }

        testDataCategorized.reset();
        testDataCategorized.initFromTestData(testData);
        return testDataCategorized;
    }
}
