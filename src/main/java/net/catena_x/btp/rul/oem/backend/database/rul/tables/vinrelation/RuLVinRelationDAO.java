package net.catena_x.btp.rul.oem.backend.database.rul.tables.vinrelation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "vinrelations")
@NamedNativeQuery(name = "RuLVinRelationDAO.insert",
        query = "INSERT INTO vinrelations (vin, ref_id, no_data) VALUES (:vin, :ref_id, :no_data)")
@NamedNativeQuery(name = "RuLVinRelationDAO.deleteAll",
        query = "DELETE FROM vinrelations")
@NamedNativeQuery(name = "RuLVinRelationDAO.deleteByVin",
        query = "DELETE FROM vinrelations WHERE vin=:vin")
@NamedNativeQuery(name = "RuLVinRelationDAO.deleteByRefId",
        query = "DELETE FROM vinrelations WHERE ref_id=:ref_id")
@NamedNativeQuery(name = "RuLVinRelationDAO.deleteByNoData",
        query = "DELETE FROM vinrelations WHERE no_data=:no_data")
@NamedNativeQuery(name = "RuLVinRelationDAO.queryAll", resultClass = RuLVinRelationDAO.class,
        query = "SELECT * FROM vinrelations")
@NamedNativeQuery(name = "RuLVinRelationDAO.queryByVin", resultClass = RuLVinRelationDAO.class,
        query = "SELECT * FROM vinrelations WHERE vin=:vin")
@NamedNativeQuery(name = "RuLVinRelationDAO.queryByRefId", resultClass = RuLVinRelationDAO.class,
        query = "SELECT * FROM vinrelations WHERE ref_id=:ref_id")
@NamedNativeQuery(name = "RuLVinRelationDAO.queryByNoData", resultClass = RuLVinRelationDAO.class,
        query = "SELECT * FROM vinrelations WHERE no_data=:no_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RuLVinRelationDAO {
    @Id
    @Column(name="vin", length=50, updatable=false, nullable=false, unique=true)
    private String vin;

    @Column(name="ref_id", length=50, updatable=false, nullable=false, unique=true)
    private String refId;

    @Column(name="no_data",updatable=false, nullable=false)
    private boolean noData;
}
