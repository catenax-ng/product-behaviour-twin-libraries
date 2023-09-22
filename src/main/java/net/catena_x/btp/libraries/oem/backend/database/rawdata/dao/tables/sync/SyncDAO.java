package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.sync;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "sync", uniqueConstraints={@UniqueConstraint(columnNames = {"id"})})
@NamedNativeQuery(name = "SyncDAO.init",
        query = "INSERT INTO sync (id, sync_counter, query_timestamp) VALUES (:id, 0, CURRENT_TIMESTAMP)")
@NamedNativeQuery(name = "SyncDAO.setCurrent",
        query = "UPDATE sync SET sync_counter=:sync_counter, query_timestamp=CURRENT_TIMESTAMP WHERE id=:id")
@NamedNativeQuery(name = "SyncDAO.deleteAll",
        query = "DELETE FROM sync")
@NamedNativeQuery(name = "SyncDAO.getCurrent",
        resultClass = SyncDAO.class,
        query = "SELECT id, sync_counter, query_timestamp FROM sync WHERE id=:id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SyncDAO {
    @Id
    @Column(name="id", nullable=false)
    private String id;

    @Column(name="sync_counter", nullable=false)
    private long syncCounter;

    @Column(name="query_timestamp")
    private Instant queryTimestamp;
}
