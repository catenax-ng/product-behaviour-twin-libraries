package net.catena_x.btp.rul.oem.backend.database.rul.annotations;

import net.catena_x.btp.rul.oem.backend.database.rul.config.PersistenceRuLConfiguration;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Transactional(transactionManager= PersistenceRuLConfiguration.TRANSACTION_MANAGER,
               rollbackFor = Exception.class, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRES_NEW)
public @interface RuLTransactionDefaultCreateNew {
}
