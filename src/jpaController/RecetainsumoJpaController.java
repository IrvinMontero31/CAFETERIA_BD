/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jpaController;

import Clases_Tabla.exceptions.NonexistentEntityException;
import Clases_Tabla.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Insumo;
import entity.Receta;
import entity.Recetainsumo;
import entity.RecetainsumoPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author juanm
 */
public class RecetainsumoJpaController implements Serializable {

    public RecetainsumoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Recetainsumo recetainsumo) throws PreexistingEntityException, Exception {
        if (recetainsumo.getRecetainsumoPK() == null) {
            recetainsumo.setRecetainsumoPK(new RecetainsumoPK());
        }
        recetainsumo.getRecetainsumoPK().setIdReceta(recetainsumo.getReceta().getIdReceta());
        recetainsumo.getRecetainsumoPK().setIdInsumo(recetainsumo.getInsumo().getIdInsumo());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Insumo insumo = recetainsumo.getInsumo();
            if (insumo != null) {
                insumo = em.getReference(insumo.getClass(), insumo.getIdInsumo());
                recetainsumo.setInsumo(insumo);
            }
            Receta receta = recetainsumo.getReceta();
            if (receta != null) {
                receta = em.getReference(receta.getClass(), receta.getIdReceta());
                recetainsumo.setReceta(receta);
            }
            em.persist(recetainsumo);
            if (insumo != null) {
                insumo.getRecetainsumoCollection().add(recetainsumo);
                insumo = em.merge(insumo);
            }
            if (receta != null) {
                receta.getRecetainsumoCollection().add(recetainsumo);
                receta = em.merge(receta);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRecetainsumo(recetainsumo.getRecetainsumoPK()) != null) {
                throw new PreexistingEntityException("Recetainsumo " + recetainsumo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Recetainsumo recetainsumo) throws NonexistentEntityException, Exception {
        recetainsumo.getRecetainsumoPK().setIdReceta(recetainsumo.getReceta().getIdReceta());
        recetainsumo.getRecetainsumoPK().setIdInsumo(recetainsumo.getInsumo().getIdInsumo());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Recetainsumo persistentRecetainsumo = em.find(Recetainsumo.class, recetainsumo.getRecetainsumoPK());
            Insumo insumoOld = persistentRecetainsumo.getInsumo();
            Insumo insumoNew = recetainsumo.getInsumo();
            Receta recetaOld = persistentRecetainsumo.getReceta();
            Receta recetaNew = recetainsumo.getReceta();
            if (insumoNew != null) {
                insumoNew = em.getReference(insumoNew.getClass(), insumoNew.getIdInsumo());
                recetainsumo.setInsumo(insumoNew);
            }
            if (recetaNew != null) {
                recetaNew = em.getReference(recetaNew.getClass(), recetaNew.getIdReceta());
                recetainsumo.setReceta(recetaNew);
            }
            recetainsumo = em.merge(recetainsumo);
            if (insumoOld != null && !insumoOld.equals(insumoNew)) {
                insumoOld.getRecetainsumoCollection().remove(recetainsumo);
                insumoOld = em.merge(insumoOld);
            }
            if (insumoNew != null && !insumoNew.equals(insumoOld)) {
                insumoNew.getRecetainsumoCollection().add(recetainsumo);
                insumoNew = em.merge(insumoNew);
            }
            if (recetaOld != null && !recetaOld.equals(recetaNew)) {
                recetaOld.getRecetainsumoCollection().remove(recetainsumo);
                recetaOld = em.merge(recetaOld);
            }
            if (recetaNew != null && !recetaNew.equals(recetaOld)) {
                recetaNew.getRecetainsumoCollection().add(recetainsumo);
                recetaNew = em.merge(recetaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                RecetainsumoPK id = recetainsumo.getRecetainsumoPK();
                if (findRecetainsumo(id) == null) {
                    throw new NonexistentEntityException("The recetainsumo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(RecetainsumoPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Recetainsumo recetainsumo;
            try {
                recetainsumo = em.getReference(Recetainsumo.class, id);
                recetainsumo.getRecetainsumoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The recetainsumo with id " + id + " no longer exists.", enfe);
            }
            Insumo insumo = recetainsumo.getInsumo();
            if (insumo != null) {
                insumo.getRecetainsumoCollection().remove(recetainsumo);
                insumo = em.merge(insumo);
            }
            Receta receta = recetainsumo.getReceta();
            if (receta != null) {
                receta.getRecetainsumoCollection().remove(recetainsumo);
                receta = em.merge(receta);
            }
            em.remove(recetainsumo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Recetainsumo> findRecetainsumoEntities() {
        return findRecetainsumoEntities(true, -1, -1);
    }

    public List<Recetainsumo> findRecetainsumoEntities(int maxResults, int firstResult) {
        return findRecetainsumoEntities(false, maxResults, firstResult);
    }

    private List<Recetainsumo> findRecetainsumoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Recetainsumo.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Recetainsumo findRecetainsumo(RecetainsumoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Recetainsumo.class, id);
        } finally {
            em.close();
        }
    }

    public int getRecetainsumoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Recetainsumo> rt = cq.from(Recetainsumo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
