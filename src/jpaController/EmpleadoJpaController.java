/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jpaController;

import Clases_Tabla.exceptions.IllegalOrphanException;
import Clases_Tabla.exceptions.NonexistentEntityException;
import entity.Empleado;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Usuario;
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
public class EmpleadoJpaController implements Serializable {

    public EmpleadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empleado empleado) throws IllegalOrphanException {
        if (empleado.getTurnoCollection() == null) {
            empleado.setTurnoCollection(new ArrayList<Turno>());
        }
        List<String> illegalOrphanMessages = null;
        Usuario idUsuarioOrphanCheck = empleado.getIdUsuario();
        if (idUsuarioOrphanCheck != null) {
            Empleado oldEmpleadoOfIdUsuario = idUsuarioOrphanCheck.getEmpleado();
            if (oldEmpleadoOfIdUsuario != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Usuario " + idUsuarioOrphanCheck + " already has an item of type Empleado whose idUsuario column cannot be null. Please make another selection for the idUsuario field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario idUsuario = empleado.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getIdUsuario());
                empleado.setIdUsuario(idUsuario);
            }
            Collection<Turno> attachedTurnoCollection = new ArrayList<Turno>();
            for (Turno turnoCollectionTurnoToAttach : empleado.getTurnoCollection()) {
                turnoCollectionTurnoToAttach = em.getReference(turnoCollectionTurnoToAttach.getClass(), turnoCollectionTurnoToAttach.getIdTurno());
                attachedTurnoCollection.add(turnoCollectionTurnoToAttach);
            }
            empleado.setTurnoCollection(attachedTurnoCollection);
            em.persist(empleado);
            if (idUsuario != null) {
                idUsuario.setEmpleado(empleado);
                idUsuario = em.merge(idUsuario);
            }
            for (Turno turnoCollectionTurno : empleado.getTurnoCollection()) {
                Empleado oldIdEmpleadoOfTurnoCollectionTurno = turnoCollectionTurno.getIdEmpleado();
                turnoCollectionTurno.setIdEmpleado(empleado);
                turnoCollectionTurno = em.merge(turnoCollectionTurno);
                if (oldIdEmpleadoOfTurnoCollectionTurno != null) {
                    oldIdEmpleadoOfTurnoCollectionTurno.getTurnoCollection().remove(turnoCollectionTurno);
                    oldIdEmpleadoOfTurnoCollectionTurno = em.merge(oldIdEmpleadoOfTurnoCollectionTurno);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empleado empleado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado persistentEmpleado = em.find(Empleado.class, empleado.getIdEmpleado());
            Usuario idUsuarioOld = persistentEmpleado.getIdUsuario();
            Usuario idUsuarioNew = empleado.getIdUsuario();
            Collection<Turno> turnoCollectionOld = persistentEmpleado.getTurnoCollection();
            Collection<Turno> turnoCollectionNew = empleado.getTurnoCollection();
            List<String> illegalOrphanMessages = null;
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                Empleado oldEmpleadoOfIdUsuario = idUsuarioNew.getEmpleado();
                if (oldEmpleadoOfIdUsuario != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Usuario " + idUsuarioNew + " already has an item of type Empleado whose idUsuario column cannot be null. Please make another selection for the idUsuario field.");
                }
            }
            for (Turno turnoCollectionOldTurno : turnoCollectionOld) {
                if (!turnoCollectionNew.contains(turnoCollectionOldTurno)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Turno " + turnoCollectionOldTurno + " since its idEmpleado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                empleado.setIdUsuario(idUsuarioNew);
            }
            Collection<Turno> attachedTurnoCollectionNew = new ArrayList<Turno>();
            for (Turno turnoCollectionNewTurnoToAttach : turnoCollectionNew) {
                turnoCollectionNewTurnoToAttach = em.getReference(turnoCollectionNewTurnoToAttach.getClass(), turnoCollectionNewTurnoToAttach.getIdTurno());
                attachedTurnoCollectionNew.add(turnoCollectionNewTurnoToAttach);
            }
            turnoCollectionNew = attachedTurnoCollectionNew;
            empleado.setTurnoCollection(turnoCollectionNew);
            empleado = em.merge(empleado);
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.setEmpleado(null);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.setEmpleado(empleado);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            for (Turno turnoCollectionNewTurno : turnoCollectionNew) {
                if (!turnoCollectionOld.contains(turnoCollectionNewTurno)) {
                    Empleado oldIdEmpleadoOfTurnoCollectionNewTurno = turnoCollectionNewTurno.getIdEmpleado();
                    turnoCollectionNewTurno.setIdEmpleado(empleado);
                    turnoCollectionNewTurno = em.merge(turnoCollectionNewTurno);
                    if (oldIdEmpleadoOfTurnoCollectionNewTurno != null && !oldIdEmpleadoOfTurnoCollectionNewTurno.equals(empleado)) {
                        oldIdEmpleadoOfTurnoCollectionNewTurno.getTurnoCollection().remove(turnoCollectionNewTurno);
                        oldIdEmpleadoOfTurnoCollectionNewTurno = em.merge(oldIdEmpleadoOfTurnoCollectionNewTurno);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = empleado.getIdEmpleado();
                if (findEmpleado(id) == null) {
                    throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.");
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
            Empleado empleado;
            try {
                empleado = em.getReference(Empleado.class, id);
                empleado.getIdEmpleado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Turno> turnoCollectionOrphanCheck = empleado.getTurnoCollection();
            for (Turno turnoCollectionOrphanCheckTurno : turnoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empleado (" + empleado + ") cannot be destroyed since the Turno " + turnoCollectionOrphanCheckTurno + " in its turnoCollection field has a non-nullable idEmpleado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuario idUsuario = empleado.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.setEmpleado(null);
                idUsuario = em.merge(idUsuario);
            }
            em.remove(empleado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empleado> findEmpleadoEntities() {
        return findEmpleadoEntities(true, -1, -1);
    }

    public List<Empleado> findEmpleadoEntities(int maxResults, int firstResult) {
        return findEmpleadoEntities(false, maxResults, firstResult);
    }

    private List<Empleado> findEmpleadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empleado.class));
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

    public Empleado findEmpleado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empleado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpleadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empleado> rt = cq.from(Empleado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
