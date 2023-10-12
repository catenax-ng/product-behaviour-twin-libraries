package net.catena_x.btp.sedc.apps.oem.database.annotations;

import net.catena_x.btp.sedc.apps.oem.database.config.PersistencePeakLoadConfiguration;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Transactional(transactionManager= PersistencePeakLoadConfiguration.TRANSACTION_MANAGER,
               rollbackFor = Exception.class, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRES_NEW)
public @interface PeakLoadTransactionDefaultCreateNew {
}
