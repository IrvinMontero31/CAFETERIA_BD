/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jpaController;

import Clases_Tabla.exceptions.IllegalOrphanException;
import Clases_Tabla.exceptions.NonexistentEntityException;
import entity.EnumTurno;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Turno;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author juanm
 */
public class EnumTurnoJpaController implements Serializable {

    public EnumTurnoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EnumTurno enumTurno) {
        if (enumTurno.getTurnoCollection() == null) {
            enumTurno.setTurnoCollection(new ArrayList<Turno>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Turno> attachedTurnoCollection = new ArrayList<Turno>();
            for (Turno turnoCollectionTurnoToAttach : enumTurno.getTurnoCollection()) {
                turnoCollectionTurnoToAttach = em.getReference(turnoCollectionTurnoToAttach.getClass(), turnoCollectionTurnoToAttach.getIdTurno());
                attachedTurnoCollection.add(turnoCollectionTurnoToAttach);
            }
            enumTurno.setTurnoCollection(attachedTurnoCollection);
            em.persist(enumTurno);
            for (Turno turnoCollectionTurno : enumTurno.getTurnoCollection()) {
                EnumTurno oldIdTurnoTipoOfTurnoCollectionTurno = turnoCollectionTurno.getIdTurnoTipo();
                turnoCollectionTurno.setIdTurnoTipo(enumTurno);
                turnoCollectionTurno = em.merge(turnoCollectionTurno);
                if (oldIdTurnoTipoOfTurnoCollectionTurno != null) {
                    oldIdTurnoTipoOfTurnoCollectionTurno.getTurnoCollection().remove(turnoCollectionTurno);
                    oldIdTurnoTipoOfTurnoCollectionTurno = em.merge(oldIdTurnoTipoOfTurnoCollectionTurno);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EnumTurno enumTurno) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EnumTurno persistentEnumTurno = em.find(EnumTurno.class, enumTurno.getIdTurnoTipo());
            Collection<Turno> turnoCollectionOld = persistentEnumTurno.getTurnoCollection();
            Collection<Turno> turnoCollectionNew = enumTurno.getTurnoCollection();
            List<String> illegalOrphanMessages = null;
            for (Turno turnoCollectionOldTurno : turnoCollectionOld) {
                if (!turnoCollectionNew.contains(turnoCollectionOldTurno)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Turno " + turnoCollectionOldTurno + " since its idTurnoTipo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Turno> attachedTurnoCollectionNew = new ArrayList<Turno>();
            for (Turno turnoCollectionNewTurnoToAttach : turnoCollectionNew) {
                turnoCollectionNewTurnoToAttach = em.getReference(turnoCollectionNewTurnoToAttach.getClass(), turnoCollectionNewTurnoToAttach.getIdTurno());
                attachedTurnoCollectionNew.add(turnoCollectionNewTurnoToAttach);
            }
            turnoCollectionNew = attachedTurnoCollectionNew;
            enumTurno.setTurnoCollection(turnoCollectionNew);
            enumTurno = em.merge(enumTurno);
            for (Turno turnoCollectionNewTurno : turnoCollectionNew) {
                if (!turnoCollectionOld.contains(turnoCollectionNewTurno)) {
                    EnumTurno oldIdTurnoTipoOfTurnoCollectionNewTurno = turnoCollectionNewTurno.getIdTurnoTipo();
                    turnoCollectionNewTurno.setIdTurnoTipo(enumTurno);
                    turnoCollectionNewTurno = em.merge(turnoCollectionNewTurno);
                    if (oldIdTurnoTipoOfTurnoCollectionNewTurno != null && !oldIdTurnoTipoOfTurnoCollectionNewTurno.equals(enumTurno)) {
                        oldIdTurnoTipoOfTurnoCollectionNewTurno.getTurnoCollection().remove(turnoCollectionNewTurno);
                        oldIdTurnoTipoOfTurnoCollectionNewTurno = em.merge(oldIdTurnoTipoOfTurnoCollectionNewTurno);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = enumTurno.getIdTurnoTipo();
                if (findEnumTurno(id) == null) {
                    throw new NonexistentEntityException("The enumTurno with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EnumTurno enumTurno;
            try {
                enumTurno = em.getReference(EnumTurno.class, id);
                enumTurno.getIdTurnoTipo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The enumTurno with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Turno> turnoCollectionOrphanCheck = enumTurno.getTurnoCollection();
            for (Turno turnoCollectionOrphanCheckTurno : turnoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EnumTurno (" + enumTurno + ") cannot be destroyed since the Turno " + turnoCollectionOrphanCheckTurno + " in its turnoCollection field has a non-nullable idTurnoTipo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(enumTurno);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EnumTurno> findEnumTurnoEntities() {
        return findEnumTurnoEntities(true, -1, -1);
    }

    public List<EnumTurno> findEnumTurnoEntities(int maxResults, int firstResult) {
        return findEnumTurnoEntities(false, maxResults, firstResult);
    }

    private List<EnumTurno> findEnumTurnoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EnumTurno.class));
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

    public EnumTurno findEnumTurno(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EnumTurno.class, id);
        } finally {
            em.close();
        }
    }

    public int getEnumTurnoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EnumTurno> rt = cq.from(EnumTurno.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
