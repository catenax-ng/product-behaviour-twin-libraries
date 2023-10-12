package net.catena_x.btp.libraries.oem.backend.database.rawdata.annotations;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.config.PersistenceRawDataConfiguration;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Transactional(transactionManager=PersistenceRawDataConfiguration.TRANSACTION_MANAGER,
               rollbackFor = Exception.class, isolation = Isolation.DEFAULT, propagation = Propagation.MANDATORY)
public @interface RDTransactionDefaultUseExisting {
}
