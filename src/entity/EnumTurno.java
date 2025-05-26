/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author juanm
 */
@Entity
@Table(name = "enum_turno")
@NamedQueries({
    @NamedQuery(name = "EnumTurno.findAll", query = "SELECT e FROM EnumTurno e"),
    @NamedQuery(name = "EnumTurno.findByIdTurnoTipo", query = "SELECT e FROM EnumTurno e WHERE e.idTurnoTipo = :idTurnoTipo"),
    @NamedQuery(name = "EnumTurno.findByDescripcion", query = "SELECT e FROM EnumTurno e WHERE e.descripcion = :descripcion")})
public class EnumTurno implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idTurnoTipo")
    private Integer idTurnoTipo;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTurnoTipo")
    private Collection<Turno> turnoCollection;

    public EnumTurno() {
    }

    public EnumTurno(Integer idTurnoTipo) {
        this.idTurnoTipo = idTurnoTipo;
    }

    public EnumTurno(Integer idTurnoTipo, String descripcion) {
        this.idTurnoTipo = idTurnoTipo;
        this.descripcion = descripcion;
    }

    public Integer getIdTurnoTipo() {
        return idTurnoTipo;
    }

    public void setIdTurnoTipo(Integer idTurnoTipo) {
        this.idTurnoTipo = idTurnoTipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Collection<Turno> getTurnoCollection() {
        return turnoCollection;
    }

    public void setTurnoCollection(Collection<Turno> turnoCollection) {
        this.turnoCollection = turnoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTurnoTipo != null ? idTurnoTipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EnumTurno)) {
            return false;
        }
        EnumTurno other = (EnumTurno) object;
        if ((this.idTurnoTipo == null && other.idTurnoTipo != null) || (this.idTurnoTipo != null && !this.idTurnoTipo.equals(other.idTurnoTipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.EnumTurno[ idTurnoTipo=" + idTurnoTipo + " ]";
    }
    
}
