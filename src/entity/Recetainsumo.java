/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author juanm
 */
@Entity
@Table(name = "recetainsumo")
@NamedQueries({
    @NamedQuery(name = "Recetainsumo.findAll", query = "SELECT r FROM Recetainsumo r"),
    @NamedQuery(name = "Recetainsumo.findByIdReceta", query = "SELECT r FROM Recetainsumo r WHERE r.recetainsumoPK.idReceta = :idReceta"),
    @NamedQuery(name = "Recetainsumo.findByIdInsumo", query = "SELECT r FROM Recetainsumo r WHERE r.recetainsumoPK.idInsumo = :idInsumo"),
    @NamedQuery(name = "Recetainsumo.findByCantidad", query = "SELECT r FROM Recetainsumo r WHERE r.cantidad = :cantidad")})
public class Recetainsumo implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RecetainsumoPK recetainsumoPK;
    @Basic(optional = false)
    @Column(name = "cantidad")
    private double cantidad;
    @JoinColumn(name = "idInsumo", referencedColumnName = "idInsumo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Insumo insumo;
    @JoinColumn(name = "idReceta", referencedColumnName = "idReceta", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Receta receta;

    public Recetainsumo() {
    }

    public Recetainsumo(RecetainsumoPK recetainsumoPK) {
        this.recetainsumoPK = recetainsumoPK;
    }

    public Recetainsumo(RecetainsumoPK recetainsumoPK, double cantidad) {
        this.recetainsumoPK = recetainsumoPK;
        this.cantidad = cantidad;
    }

    public Recetainsumo(int idReceta, int idInsumo) {
        this.recetainsumoPK = new RecetainsumoPK(idReceta, idInsumo);
    }

    public RecetainsumoPK getRecetainsumoPK() {
        return recetainsumoPK;
    }

    public void setRecetainsumoPK(RecetainsumoPK recetainsumoPK) {
        this.recetainsumoPK = recetainsumoPK;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public Insumo getInsumo() {
        return insumo;
    }

    public void setInsumo(Insumo insumo) {
        this.insumo = insumo;
    }

    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recetainsumoPK != null ? recetainsumoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Recetainsumo)) {
            return false;
        }
        Recetainsumo other = (Recetainsumo) object;
        if ((this.recetainsumoPK == null && other.recetainsumoPK != null) || (this.recetainsumoPK != null && !this.recetainsumoPK.equals(other.recetainsumoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Recetainsumo[ recetainsumoPK=" + recetainsumoPK + " ]";
    }
    
}
