package net.catena_x.btp.hi.oem.common.database.hi.annotations;

import net.catena_x.btp.hi.oem.common.database.hi.config.PersistenceHealthIndicatorConfiguration;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Transactional(transactionManager= PersistenceHealthIndicatorConfiguration.TRANSACTION_MANAGER,
               rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE, propagation = Propagation.MANDATORY)
public @interface HITransactionSerializableUseExisting {
}
