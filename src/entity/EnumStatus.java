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
@Table(name = "enum_status")
@NamedQueries({
    @NamedQuery(name = "EnumStatus.findAll", query = "SELECT e FROM EnumStatus e"),
    @NamedQuery(name = "EnumStatus.findByIdStatus", query = "SELECT e FROM EnumStatus e WHERE e.idStatus = :idStatus"),
    @NamedQuery(name = "EnumStatus.findByDescripcion", query = "SELECT e FROM EnumStatus e WHERE e.descripcion = :descripcion")})
public class EnumStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idStatus")
    private Integer idStatus;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idStatus")
    private Collection<Pedido> pedidoCollection;

    public EnumStatus() {
    }

    public EnumStatus(Integer idStatus) {
        this.idStatus = idStatus;
    }

    public EnumStatus(Integer idStatus, String descripcion) {
        this.idStatus = idStatus;
        this.descripcion = descripcion;
    }

    public Integer getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(Integer idStatus) {
        this.idStatus = idStatus;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Collection<Pedido> getPedidoCollection() {
        return pedidoCollection;
    }

    public void setPedidoCollection(Collection<Pedido> pedidoCollection) {
        this.pedidoCollection = pedidoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idStatus != null ? idStatus.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EnumStatus)) {
            return false;
        }
        EnumStatus other = (EnumStatus) object;
        if ((this.idStatus == null && other.idStatus != null) || (this.idStatus != null && !this.idStatus.equals(other.idStatus))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.EnumStatus[ idStatus=" + idStatus + " ]";
    }
    
}
