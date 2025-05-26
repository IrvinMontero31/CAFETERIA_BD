/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author juanm
 */
@Embeddable
public class RecetainsumoPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "idReceta")
    private int idReceta;
    @Basic(optional = false)
    @Column(name = "idInsumo")
    private int idInsumo;

    public RecetainsumoPK() {
    }

    public RecetainsumoPK(int idReceta, int idInsumo) {
        this.idReceta = idReceta;
        this.idInsumo = idInsumo;
    }

    public int getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(int idReceta) {
        this.idReceta = idReceta;
    }

    public int getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(int idInsumo) {
        this.idInsumo = idInsumo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idReceta;
        hash += (int) idInsumo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecetainsumoPK)) {
            return false;
        }
        RecetainsumoPK other = (RecetainsumoPK) object;
        if (this.idReceta != other.idReceta) {
            return false;
        }
        if (this.idInsumo != other.idInsumo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RecetainsumoPK[ idReceta=" + idReceta + ", idInsumo=" + idInsumo + " ]";
    }
    
}
