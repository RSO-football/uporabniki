package rso.football.uporabniki.models.entities;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "uporabniki_metadata")
@NamedQueries(value =
        {
                @NamedQuery(name = "UporabnikiMetadataEntity.getAll",
                        query = "SELECT im FROM UporabnikiMetadataEntity im")
        })
public class UporabnikiMetadataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uporabnikID")
    private Integer uporabnikID;

    @Column(name = "role")
    private String role;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUporabnikID() {
        return uporabnikID;
    }

    public void setUporabnikID(Integer uporabnikID) {
        this.uporabnikID = uporabnikID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}