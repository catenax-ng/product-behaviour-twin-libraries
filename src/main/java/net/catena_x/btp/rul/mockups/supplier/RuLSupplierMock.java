package net.catena_x.btp.rul.mockups.supplier;

import net.catena_x.btp.libraries.bamm.common.BammLoaddataSource;
import net.catena_x.btp.libraries.bamm.common.BammStatus;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.ClassifiedLoadSpectrum;
import net.catena_x.btp.libraries.bamm.custom.remainingusefullife.RemainingUsefulLife;
import net.catena_x.btp.libraries.notification.dao.NotificationDAO;
import net.catena_x.btp.libraries.notification.dto.Notification;
import net.catena_x.btp.libraries.notification.dto.items.NotificationHeader;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.ResponseChecker;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dao.supplierservice.RuLNotificationFromSupplierContentDAO;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dao.supplierservice.RuLNotificationToSupplierContentDAO;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dao.supplierservice.items.RuLInputDAO;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dao.supplierservice.items.RuLOutputDAO;
import okhttp3.HttpUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.*;

@Component
public class RuLSupplierMock {
    @Autowired private ApiHelper apiHelper;
    @Autowired private RestTemplate restTemplate;

    @Value("${security.api.username}") private String apiUsername;
    @Value("${security.api.password}") private String apiPassword;

    private final Logger logger = LoggerFactory.getLogger(RuLSupplierMock.class);

    public ResponseEntity<DefaultApiResult> runRuLCalculationMock(
                @NotNull final NotificationDAO<RuLNotificationToSupplierContentDAO> data) {

        try {
            final List<RuLInputDAO> rulInputs = data.getContent().getEndurancePredictorInputs();
            rulInputs.sort(Comparator.comparing(RuLInputDAO::getComponentId));

            final List<RuLOutputDAO> outputs = new ArrayList<>(rulInputs.size());
            for (final RuLInputDAO inputData : rulInputs) {
                outputs.add(new RuLOutputDAO(inputData.getComponentId(),
                        getComponentType(inputData), generateRemainingUsefulLife(inputData)));
            }

            final Notification<RuLNotificationFromSupplierContentDAO> notification = new Notification<>();
            notification.setHeader(new NotificationHeader());
            notification.getHeader().setReferencedNotificationID(data.getContent().getRequestRefId());

            notification.setContent(new RuLNotificationFromSupplierContentDAO());
            notification.getContent().setRequestRefId(data.getContent().getRequestRefId());
            notification.getContent().setComponentType("GearBox");
            notification.getContent().setEndurancePredictorOutputs(outputs);

            final boolean usesKnowledgeAgent = data.getHeader().getSenderAddress().toLowerCase().startsWith("edcs://")
                                   || data.getHeader().getSenderAddress().toLowerCase().startsWith("edc://");
            final HttpUrl respondToUrl = usesKnowledgeAgent ?
                    HttpUrl.parse(data.getHeader().getRespondAssetId()).newBuilder().build()
                    : HttpUrl.parse("http://localhost:25554/")
                    .newBuilder().addPathSegment("ruldatareceiver").addPathSegment("notifyresult").build();

            new Thread(() ->
            {
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException exception) {
                }

                final HttpHeaders headers = generateDefaultHeaders();
                addAuthorizationHeaders(headers);

                final HttpEntity<Notification<RuLNotificationFromSupplierContentDAO>> request =
                        new HttpEntity<>(notification, headers);

                final ResponseEntity<DefaultApiResult> response = restTemplate.postForEntity(
                        respondToUrl.toString(), request, DefaultApiResult.class);

                checkResponse(response);
            }).start();
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            return apiHelper.failed(exception.getMessage());
        }

        return apiHelper.accepted("Accepted.");
    }

    private void checkResponse(final ResponseEntity<DefaultApiResult> response) {
        try {
            ResponseChecker.checkResponse(response);
        } catch (final BtpException exception) {
            logger.error(exception.getMessage());
        }
    }

    private void addAuthorizationHeaders(final HttpHeaders headers) {
        headers.add("Authorization", getAuthString());
    }

    private String getAuthString() {
        StringBuilder sb = new StringBuilder();

        String authStr = sb.append(apiUsername).append(":").append(apiPassword).toString();
        sb.setLength(0);
        sb.append("Basic ").append(Base64.getEncoder().encodeToString(authStr.getBytes()));
        return sb.toString();
    }

    protected HttpHeaders generateDefaultHeaders() {
        final HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    private String getComponentType(@NotNull final RuLInputDAO inputData) {
        if(inputData.getClassifiedLoadSpectrumGearOil() != null) {
            if(inputData.getClassifiedLoadSpectrumGearSet() != null) {
                return "GearBox";
            }
            else {
                return "GearOil";
            }
        } else {
            return "GearSet";
        }
    }

    private RemainingUsefulLife generateRemainingUsefulLife(@NotNull final RuLInputDAO inputData) {
        BammStatus status = null;

        RemainingUsefulLifeData gearSetRuLData = new RemainingUsefulLifeData(Float.MAX_VALUE, Long.MAX_VALUE);
        RemainingUsefulLifeData gearOilRuLData = new RemainingUsefulLifeData(Float.MAX_VALUE, Long.MAX_VALUE);

        if(inputData.getClassifiedLoadSpectrumGearSet() != null) {
            status = inputData.getClassifiedLoadSpectrumGearSet().getMetadata().getStatus();
            gearSetRuLData = calculateRuLGearSet(inputData.getClassifiedLoadSpectrumGearSet());
        }

        if(inputData.getClassifiedLoadSpectrumGearOil() != null) {
            if(status == null) {
                status = inputData.getClassifiedLoadSpectrumGearOil().getMetadata().getStatus();
            }
            gearSetRuLData = calculateRuLGearOil(inputData.getClassifiedLoadSpectrumGearOil());
        }

        return new RemainingUsefulLife(
                Float.min(gearSetRuLData.remainingOperatingHours(), gearOilRuLData.remainingOperatingHours()),
                null,
                Long.min(gearSetRuLData.remainingRunningDistance(), gearOilRuLData.remainingRunningDistance()),
                new BammLoaddataSource("loggedOEM", "loggedOEM", null),
                status);
    }

    private RemainingUsefulLifeData calculateRuLGearSet(@NotNull final ClassifiedLoadSpectrum loadSpectrum) {
        final RuLCalculationConfig config = new RuLCalculationConfig(
                -0.0000018625596309f, 30040.2264135787000000f,
                -0.0000148671745762f, 300321.0920624090000000f);
        return calculateRuL(loadSpectrum, config);
    }

    private RemainingUsefulLifeData calculateRuLGearOil(@NotNull final ClassifiedLoadSpectrum loadSpectrum) {
        final RuLCalculationConfig config = new RuLCalculationConfig(
                -0.0004245150632680f, 788.4096902595900000f,
                -0.0175255800973219f, 316695.5546034490000000f );
        return calculateRuL(loadSpectrum, config);
    }

    private RemainingUsefulLifeData calculateRuL(@NotNull final ClassifiedLoadSpectrum loadSpectrum,
                                                 @NotNull final RuLCalculationConfig config) {
        final double sum = Arrays.stream(loadSpectrum.getBody().getCounts().getCountsList()).sum();

        final float factorHours = loadSpectrum.getMetadata().getStatus().getOperatingHours() / 4964.1f;
        final float factorMilage = loadSpectrum.getMetadata().getStatus().getMileage() / 147258.0f;

        return new RemainingUsefulLifeData(
                Float.min(5000f, Float.max(10f,
                        factorHours * (float)(config.m_CountToHours() * sum + config.t_CountToHours()))),
                Long.min(300000, Long.max(10,
                        (long)(factorMilage * (config.m_CountToMilage() * sum + config.t_CountToMilage())))));
    }
}
