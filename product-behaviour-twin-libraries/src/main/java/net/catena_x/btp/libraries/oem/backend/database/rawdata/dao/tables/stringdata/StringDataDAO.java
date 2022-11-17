package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.stringdata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "string_data")
@NamedNativeQuery(name = "StringDataDAO.insert",
        query = "INSERT INTO string_data (base_id, index, value) " +
                "VALUES (:base_id, :index, :value)")
@NamedNativeQuery(name = "StringDataDAO.deleteAll",
        query = "DELETE FROM string_data")
@NamedNativeQuery(name = "StringDataDAO.deleteByBaseId",
        query = "DELETE FROM string_data WHERE base_id=:base_id")
@NamedNativeQuery(name = "StringDataDAO.queryAll", resultClass = StringDataDAO.class,
        query = "SELECT * FROM string_data GROUP BY base_id ORDER BY index")
@NamedNativeQuery(name = "StringDataDAO.queryByBaseId", resultClass = StringDataDAO.class,
        query = "SELECT * FROM string_data WHERE base_id=:base_id " +
                "GROUP BY base_id ORDER BY index")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StringDataDAO {
    public static final int MAX_STRING_LENGTH = 65535;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Column(name="base_id", length=50, nullable=false, unique=false)
    String baseId;

    @Column(name="index", nullable=false, unique=false)
    long index;

    @Column(name="value", length=StringDataDAO.MAX_STRING_LENGTH, nullable=false, unique=false)
    String value;
}
