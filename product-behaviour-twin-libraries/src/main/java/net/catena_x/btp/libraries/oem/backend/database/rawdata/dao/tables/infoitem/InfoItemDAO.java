package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.infoitem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "info", uniqueConstraints={@UniqueConstraint(columnNames = {"key"})})
@NamedNativeQuery(name = "InfoItemDAO.insert",
        query = "INSERT INTO info (key, value) VALUES (:key, :value)")
@NamedNativeQuery(name = "InfoItemDAO.update",
        query = "UPDATE info SET value=:value WHERE key=:key")
@NamedNativeQuery(name = "InfoItemDAO.deleteAll",
        query = "DELETE FROM info")
@NamedNativeQuery(name = "InfoItemDAO.delete",
        query = "DELETE FROM info WHERE key=:key")
@NamedNativeQuery(name = "InfoItemDAO.queryAll", resultClass = InfoItemDAO.class,
        query = "SELECT key, value, CURRENT_TIMESTAMP AS query_timestamp FROM info")
@NamedNativeQuery(name = "InfoItemDAO.queryByKey", resultClass = InfoItemDAO.class,
        query = "SELECT key, value, CURRENT_TIMESTAMP AS query_timestamp FROM info WHERE key=:key")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InfoItemDAO {
    @Id
    @Column(name="key", length=50, nullable=false)
    private String key;

    @Column(name="value", length=20000, nullable=false)
    private String value;

    @Column(name="query_timestamp")
    private Instant queryTimestamp;
}
