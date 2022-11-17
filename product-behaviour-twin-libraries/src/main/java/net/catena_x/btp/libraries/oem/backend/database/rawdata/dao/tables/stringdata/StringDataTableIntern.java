package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.stringdata;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.table.RawTableBase;
import net.catena_x.btp.libraries.oem.backend.database.util.annotations.TransactionDefaultCreateNew;
import net.catena_x.btp.libraries.oem.backend.database.util.annotations.TransactionDefaultUseExisting;
import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Component
public class StringDataTableIntern extends RawTableBase {
    @Autowired
    private StringDataRepository stringDataRepository;

    @TransactionDefaultUseExisting
    public void insertExternTransaction(@NotNull final String baseId, @NotNull final String value)
            throws OemDatabaseException {
        try {
            final String[] list = value.split("(?<=\\G.{" + StringDataDAO.MAX_STRING_LENGTH + "})");

            for (int i = 0; i < list.length; i++) {
                stringDataRepository.insert(baseId, i, list[i]);
            }
        } catch (Exception exception) {
            throw failed("Failed to insert new string data!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public void insertNewTransaction(@NotNull final String baseId, @NotNull final String value)
            throws OemDatabaseException {
        insertExternTransaction(baseId, value);
    }

    @TransactionDefaultUseExisting
    public void deleteAllExternTransaction() throws OemDatabaseException {
        try {
            stringDataRepository.deleteAll();
        } catch (Exception exception) {
            throw failed("Failed to delete all string data!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public void deleteAllNewTransaction() throws OemDatabaseException {
        deleteAllExternTransaction();
    }

    @TransactionDefaultUseExisting
    public void deleteByIdExternTransaction(@NotNull final String baseId) throws OemDatabaseException {
        try {
            stringDataRepository.deleteByBaseId(baseId);
        } catch (Exception exception) {
            throw failed("Failed to delete string data by id!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public void deleteByIdNewTransaction(@NotNull final String baseId) throws OemDatabaseException {
        deleteByIdExternTransaction(baseId);
    }

    @TransactionDefaultUseExisting
    public List<String> getAllExternTransaction() throws OemDatabaseException {
        try {
            final List<StringDataDAO> list = stringDataRepository.queryAll();
            if(list == null) {
                return null;
            }

            return reassembleList(list);
        } catch (Exception exception) {
            throw failed("Failed to query all string data!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public List<String> getAllNewTransaction() throws OemDatabaseException {
        return getAllExternTransaction();
    }

    @TransactionDefaultUseExisting
    public String queryByIdExternTransaction(@NotNull final String baseId) throws OemDatabaseException {
        try {
            final List<StringDataDAO> list = stringDataRepository.queryByBaseId(baseId);
            if(list == null) {
                return null;
            }

            return reassemble(list);
        } catch (Exception exception) {
            throw failed("Failed to query string data by id!", exception);
        }
    }

    @TransactionDefaultCreateNew
    public String queryByIdNewTransaction(@NotNull final String baseId) throws OemDatabaseException {
        return queryByIdExternTransaction(baseId);
    }

    private List<String> reassembleList(@NotNull final List<StringDataDAO> list) throws OemDatabaseException {
        final int size = list.size();

        if(size == 0) {
            return new ArrayList<>();
        }

        List<String> result = new ArrayList<>();

        int start = 0;
        for (int i = 0; i < size; i++) {
            if(!list.get(i).getBaseId().equals(list.get(start).getBaseId())) {
                result.add(reassemble(list, start, i));
                start = i;
            }
        }

        result.add(reassemble(list, start, size));

        return result;
    }

    private String reassemble(@NotNull final List<StringDataDAO> list) throws OemDatabaseException {
        if(list.isEmpty()) {
            return null;
        }
        return reassemble(list, 0, list.size());
    }

    private String reassemble(@NotNull final List<StringDataDAO> list,
                              int start, int stop) throws OemDatabaseException {
        String result = "";
        for (int i = start; i < stop; i++) {
            result += loadAndCheckItem(list, start, i);
        }
        return result;
    }

    private String loadAndCheckItem(@NotNull final List<StringDataDAO> list,
                                    int start, int index) throws OemDatabaseException {
        final StringDataDAO item = list.get(index);
        if (item.getIndex() != (index - start)) {
            throw new OemDatabaseException("String data not consistent!");
        }
        return item.getValue();
    }
}
