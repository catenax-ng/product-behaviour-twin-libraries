package net.catena_x.btp.rul.oem.backend.util.enums;

import net.catena_x.btp.rul.oem.backend.model.dto.serviceoption.RuLServiceOptionTable;
import net.catena_x.btp.rul.oem.backend.model.enums.RuLServiceOptionType;
import net.catena_x.btp.rul.oem.util.exceptions.OemRuLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RuLServiceOptionHelper {
    @Autowired RuLServiceOptionTable rulServiceOptionTable;

    public boolean isShowInputFromRequester() throws OemRuLException {
        return readBooleanSetIfNull(RuLServiceOptionType.SHOW_INPUT_FROM_REQUESTER, true);
    }

    public void setShowInputFromRequester(final boolean value) throws OemRuLException {
        setBoolean(RuLServiceOptionType.SHOW_INPUT_FROM_REQUESTER, value);
    }

    public boolean isShowOutputToRequester() throws OemRuLException {
        return readBooleanSetIfNull(RuLServiceOptionType.SHOW_OUTPUT_TO_REQUESTER, true);
    }

    public void setShowOutputToRequester(final boolean value) throws OemRuLException {
        setBoolean(RuLServiceOptionType.SHOW_OUTPUT_TO_REQUESTER, value);
    }

    public boolean isShowInputToSupplier() throws OemRuLException {
        return readBooleanSetIfNull(RuLServiceOptionType.SHOW_INPUT_TO_SUPPLIER, true);
    }

    public void setShowInputToSupplier(final boolean value) throws OemRuLException {
        setBoolean(RuLServiceOptionType.SHOW_INPUT_TO_SUPPLIER, value);
    }

    public boolean isShowOutputFromSupplier() throws OemRuLException {
        return readBooleanSetIfNull(RuLServiceOptionType.SHOW_OUTPUT_FROM_SUPPLIER, true);
    }

    public void setShowOutputFromSupplier(final boolean value) throws OemRuLException {
        setBoolean(RuLServiceOptionType.SHOW_OUTPUT_FROM_SUPPLIER, value);
    }

    private boolean readBooleanSetIfNull(final RuLServiceOptionType type, final boolean valueIfNull)
            throws OemRuLException{
        final String value = rulServiceOptionTable.getServiceOptionValueNewTransaction(type);

        if(value == null) {
            setBoolean(type, valueIfNull);
            return valueIfNull;
        }

        return Boolean.parseBoolean(value);
    }

    private void setBoolean(final RuLServiceOptionType type, final boolean value) throws OemRuLException {
        rulServiceOptionTable.setServiceOptionNewTransaction(type, String.valueOf(value));
    }
}
