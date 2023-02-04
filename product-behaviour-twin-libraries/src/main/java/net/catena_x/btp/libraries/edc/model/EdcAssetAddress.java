package net.catena_x.btp.libraries.edc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.notification.dao.NotificationDAO;
import net.catena_x.btp.libraries.notification.dto.Notification;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EdcAssetAddress {
    private String connectorUrl;
    private String bpn;
    private String assetId;

    public static <T> EdcAssetAddress replyAddressFromNotification(@NotNull final Notification<T> notification)
            throws BtpException {
        assetNotNull(notification, "notification");
        assetNotNull(notification.getHeader(), "header");
        assetNotNull(notification.getHeader().getSenderAddress(), "sender address");
        assetNotNull(notification.getHeader().getSenderBPN(), "BPN");
        assetNotNull(notification.getHeader().getRespondAssetId(), "asset id");

        return new EdcAssetAddress(notification.getHeader().getSenderAddress(),
                                   notification.getHeader().getSenderBPN(),
                                   notification.getHeader().getRespondAssetId());
    }

    public static <T> EdcAssetAddress replyAddressFromNotification(@NotNull final NotificationDAO<T> notification)
            throws BtpException {
        assetNotNull(notification, "notification");
        assetNotNull(notification.getHeader(), "header");
        assetNotNull(notification.getHeader().getSenderAddress(), "sender address");
        assetNotNull(notification.getHeader().getSenderBPN(), "BPN");
        assetNotNull(notification.getHeader().getRespondAssetId(), "asset id");

        return new EdcAssetAddress(notification.getHeader().getSenderAddress(),
                                   notification.getHeader().getSenderBPN(),
                                   notification.getHeader().getRespondAssetId());
    }

    private static <T> void assetNotNull(@Nullable final T value, @Nullable final String element) throws BtpException {
        if(value == null) {
            throw new BtpException("Element " + element + " in notification not present!");
        }
    }
}
