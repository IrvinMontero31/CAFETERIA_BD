/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jpaController;

import Clases_Tabla.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Empleado;
import entity.EnumTurno;
import entity.Turno;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author juanm
 */
public class TurnoJpaController implements Serializable {

    public TurnoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Turno turno) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado idEmpleado = turno.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado = em.getReference(idEmpleado.getClass(), idEmpleado.getIdEmpleado());
                turno.setIdEmpleado(idEmpleado);
            }
            EnumTurno idTurnoTipo = turno.getIdTurnoTipo();
            if (idTurnoTipo != null) {
                idTurnoTipo = em.getReference(idTurnoTipo.getClass(), idTurnoTipo.getIdTurnoTipo());
                turno.setIdTurnoTipo(idTurnoTipo);
            }
            em.persist(turno);
            if (idEmpleado != null) {
                idEmpleado.getTurnoCollection().add(turno);
                idEmpleado = em.merge(idEmpleado);
            }
            if (idTurnoTipo != null) {
                idTurnoTipo.getTurnoCollection().add(turno);
                idTurnoTipo = em.merge(idTurnoTipo);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Turno turno) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Turno persistentTurno = em.find(Turno.class, turno.getIdTurno());
            Empleado idEmpleadoOld = persistentTurno.getIdEmpleado();
            Empleado idEmpleadoNew = turno.getIdEmpleado();
            EnumTurno idTurnoTipoOld = persistentTurno.getIdTurnoTipo();
            EnumTurno idTurnoTipoNew = turno.getIdTurnoTipo();
            if (idEmpleadoNew != null) {
                idEmpleadoNew = em.getReference(idEmpleadoNew.getClass(), idEmpleadoNew.getIdEmpleado());
                turno.setIdEmpleado(idEmpleadoNew);
            }
            if (idTurnoTipoNew != null) {
                idTurnoTipoNew = em.getReference(idTurnoTipoNew.getClass(), idTurnoTipoNew.getIdTurnoTipo());
                turno.setIdTurnoTipo(idTurnoTipoNew);
            }
            turno = em.merge(turno);
            if (idEmpleadoOld != null && !idEmpleadoOld.equals(idEmpleadoNew)) {
                idEmpleadoOld.getTurnoCollection().remove(turno);
                idEmpleadoOld = em.merge(idEmpleadoOld);
            }
            if (idEmpleadoNew != null && !idEmpleadoNew.equals(idEmpleadoOld)) {
                idEmpleadoNew.getTurnoCollection().add(turno);
                idEmpleadoNew = em.merge(idEmpleadoNew);
            }
            if (idTurnoTipoOld != null && !idTurnoTipoOld.equals(idTurnoTipoNew)) {
                idTurnoTipoOld.getTurnoCollection().remove(turno);
                idTurnoTipoOld = em.merge(idTurnoTipoOld);
            }
            if (idTurnoTipoNew != null && !idTurnoTipoNew.equals(idTurnoTipoOld)) {
                idTurnoTipoNew.getTurnoCollection().add(turno);
                idTurnoTipoNew = em.merge(idTurnoTipoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = turno.getIdTurno();
                if (findTurno(id) == null) {
                    throw new NonexistentEntityException("The turno with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Turno turno;
            try {
                turno = em.getReference(Turno.class, id);
                turno.getIdTurno();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The turno with id " + id + " no longer exists.", enfe);
            }
            Empleado idEmpleado = turno.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado.getTurnoCollection().remove(turno);
                idEmpleado = em.merge(idEmpleado);
            }
            EnumTurno idTurnoTipo = turno.getIdTurnoTipo();
            if (idTurnoTipo != null) {
                idTurnoTipo.getTurnoCollection().remove(turno);
                idTurnoTipo = em.merge(idTurnoTipo);
            }
            em.remove(turno);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Turno> findTurnoEntities() {
        return findTurnoEntities(true, -1, -1);
    }

    public List<Turno> findTurnoEntities(int maxResults, int firstResult) {
        return findTurnoEntities(false, maxResults, firstResult);
    }

    private List<Turno> findTurnoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Turno.class));
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

    public Turno findTurno(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Turno.class, id);
        } finally {
            em.close();
        }
    }

    public int getTurnoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Turno> rt = cq.from(Turno.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
