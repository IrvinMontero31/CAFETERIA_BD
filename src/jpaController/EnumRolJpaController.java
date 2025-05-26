/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jpaController;

import Clases_Tabla.exceptions.IllegalOrphanException;
import Clases_Tabla.exceptions.NonexistentEntityException;
import entity.EnumRol;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Usuario;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author juanm
 */
public class EnumRolJpaController implements Serializable {

    public EnumRolJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EnumRol enumRol) {
        if (enumRol.getUsuarioCollection() == null) {
            enumRol.setUsuarioCollection(new ArrayList<Usuario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Usuario> attachedUsuarioCollection = new ArrayList<Usuario>();
            for (Usuario usuarioCollectionUsuarioToAttach : enumRol.getUsuarioCollection()) {
                usuarioCollectionUsuarioToAttach = em.getReference(usuarioCollectionUsuarioToAttach.getClass(), usuarioCollectionUsuarioToAttach.getIdUsuario());
                attachedUsuarioCollection.add(usuarioCollectionUsuarioToAttach);
            }
            enumRol.setUsuarioCollection(attachedUsuarioCollection);
            em.persist(enumRol);
            for (Usuario usuarioCollectionUsuario : enumRol.getUsuarioCollection()) {
                EnumRol oldIdRolOfUsuarioCollectionUsuario = usuarioCollectionUsuario.getIdRol();
                usuarioCollectionUsuario.setIdRol(enumRol);
                usuarioCollectionUsuario = em.merge(usuarioCollectionUsuario);
                if (oldIdRolOfUsuarioCollectionUsuario != null) {
                    oldIdRolOfUsuarioCollectionUsuario.getUsuarioCollection().remove(usuarioCollectionUsuario);
                    oldIdRolOfUsuarioCollectionUsuario = em.merge(oldIdRolOfUsuarioCollectionUsuario);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EnumRol enumRol) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EnumRol persistentEnumRol = em.find(EnumRol.class, enumRol.getIdRol());
            Collection<Usuario> usuarioCollectionOld = persistentEnumRol.getUsuarioCollection();
            Collection<Usuario> usuarioCollectionNew = enumRol.getUsuarioCollection();
            List<String> illegalOrphanMessages = null;
            for (Usuario usuarioCollectionOldUsuario : usuarioCollectionOld) {
                if (!usuarioCollectionNew.contains(usuarioCollectionOldUsuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Usuario " + usuarioCollectionOldUsuario + " since its idRol field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Usuario> attachedUsuarioCollectionNew = new ArrayList<Usuario>();
            for (Usuario usuarioCollectionNewUsuarioToAttach : usuarioCollectionNew) {
                usuarioCollectionNewUsuarioToAttach = em.getReference(usuarioCollectionNewUsuarioToAttach.getClass(), usuarioCollectionNewUsuarioToAttach.getIdUsuario());
                attachedUsuarioCollectionNew.add(usuarioCollectionNewUsuarioToAttach);
            }
            usuarioCollectionNew = attachedUsuarioCollectionNew;
            enumRol.setUsuarioCollection(usuarioCollectionNew);
            enumRol = em.merge(enumRol);
            for (Usuario usuarioCollectionNewUsuario : usuarioCollectionNew) {
                if (!usuarioCollectionOld.contains(usuarioCollectionNewUsuario)) {
                    EnumRol oldIdRolOfUsuarioCollectionNewUsuario = usuarioCollectionNewUsuario.getIdRol();
                    usuarioCollectionNewUsuario.setIdRol(enumRol);
                    usuarioCollectionNewUsuario = em.merge(usuarioCollectionNewUsuario);
                    if (oldIdRolOfUsuarioCollectionNewUsuario != null && !oldIdRolOfUsuarioCollectionNewUsuario.equals(enumRol)) {
                        oldIdRolOfUsuarioCollectionNewUsuario.getUsuarioCollection().remove(usuarioCollectionNewUsuario);
                        oldIdRolOfUsuarioCollectionNewUsuario = em.merge(oldIdRolOfUsuarioCollectionNewUsuario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = enumRol.getIdRol();
                if (findEnumRol(id) == null) {
                    throw new NonexistentEntityException("The enumRol with id " + id + " no longer exists.");
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
            EnumRol enumRol;
            try {
                enumRol = em.getReference(EnumRol.class, id);
                enumRol.getIdRol();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The enumRol with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Usuario> usuarioCollectionOrphanCheck = enumRol.getUsuarioCollection();
            for (Usuario usuarioCollectionOrphanCheckUsuario : usuarioCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EnumRol (" + enumRol + ") cannot be destroyed since the Usuario " + usuarioCollectionOrphanCheckUsuario + " in its usuarioCollection field has a non-nullable idRol field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(enumRol);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EnumRol> findEnumRolEntities() {
        return findEnumRolEntities(true, -1, -1);
    }

    public List<EnumRol> findEnumRolEntities(int maxResults, int firstResult) {
        return findEnumRolEntities(false, maxResults, firstResult);
    }

    private List<EnumRol> findEnumRolEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EnumRol.class));
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

    public EnumRol findEnumRol(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EnumRol.class, id);
        } finally {
            em.close();
        }
    }

    public int getEnumRolCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EnumRol> rt = cq.from(EnumRol.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
