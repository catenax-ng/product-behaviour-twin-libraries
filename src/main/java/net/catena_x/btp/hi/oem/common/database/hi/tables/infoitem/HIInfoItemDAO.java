package net.catena_x.btp.hi.oem.common.database.hi.tables.infoitem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "hiinfo", uniqueConstraints={@UniqueConstraint(columnNames = {"key"})})
@NamedNativeQuery(name = "HIInfoItemDAO.insert",
        query = "INSERT INTO hiinfo (key, value) VALUES (:key, :value)")
@NamedNativeQuery(name = "HIInfoItemDAO.update",
        query = "UPDATE hiinfo SET value=:value WHERE key=:key")
@NamedNativeQuery(name = "HIInfoItemDAO.deleteAll",
        query = "DELETE FROM hiinfo")
@NamedNativeQuery(name = "HIInfoItemDAO.delete",
        query = "DELETE FROM hiinfo WHERE key=:key")
@NamedNativeQuery(name = "HIInfoItemDAO.queryAll", resultClass = HIInfoItemDAO.class,
        query = "SELECT key, value FROM hiinfo")
@NamedNativeQuery(name = "HIInfoItemDAO.queryByKey", resultClass = HIInfoItemDAO.class,
        query = "SELECT key, value FROM hiinfo WHERE key=:key")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HIInfoItemDAO {
    @Id
    @Column(name="key", length=50, nullable=false)
    private String key;

    @Column(name="value", length=20000, nullable=false)
    private String value;
}