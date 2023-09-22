package net.catena_x.btp.rul.oem.backend.database.rul.tables.serviceoption;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "rulserviceoptions", uniqueConstraints={@UniqueConstraint(columnNames = {"key"})})
@NamedNativeQuery(name = "RuLServiceOptionDAO.insert",
        query = "INSERT INTO rulserviceoptions (key, value) VALUES (:key, :value)")
@NamedNativeQuery(name = "RuLServiceOptionDAO.update",
        query = "UPDATE rulserviceoptions SET value=:value WHERE key=:key")
@NamedNativeQuery(name = "RuLServiceOptionDAO.deleteAll",
        query = "DELETE FROM rulserviceoptions")
@NamedNativeQuery(name = "RuLServiceOptionDAO.delete",
        query = "DELETE FROM rulserviceoptions WHERE key=:key")
@NamedNativeQuery(name = "RuLServiceOptionDAO.queryAll", resultClass = RuLServiceOptionDAO.class,
        query = "SELECT key, value FROM rulserviceoptions")
@NamedNativeQuery(name = "RuLServiceOptionDAO.queryByKey", resultClass = RuLServiceOptionDAO.class,
        query = "SELECT key, value FROM rulserviceoptions WHERE key=:key")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RuLServiceOptionDAO {
    @Id
    @Column(name="key", length=50, nullable=false)
    private String key;

    @Column(name="value", length=20000, nullable=false)
    private String value;
}
