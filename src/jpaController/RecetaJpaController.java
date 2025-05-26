/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jpaController;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Producto;
import entity.Receta;
import java.util.ArrayList;
import java.util.Collection;
import entity.Recetainsumo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpaController.exceptions.IllegalOrphanException;
import jpaController.exceptions.NonexistentEntityException;

/**
 *
 * @author crist
 */
public class RecetaJpaController implements Serializable {

    public RecetaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Receta receta) {
        if (receta.getProductoCollection() == null) {
            receta.setProductoCollection(new ArrayList<Producto>());
        }
        if (receta.getRecetainsumoCollection() == null) {
            receta.setRecetainsumoCollection(new ArrayList<Recetainsumo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Producto> attachedProductoCollection = new ArrayList<Producto>();
            for (Producto productoCollectionProductoToAttach : receta.getProductoCollection()) {
                productoCollectionProductoToAttach = em.getReference(productoCollectionProductoToAttach.getClass(), productoCollectionProductoToAttach.getIdProducto());
                attachedProductoCollection.add(productoCollectionProductoToAttach);
            }
            receta.setProductoCollection(attachedProductoCollection);
            Collection<Recetainsumo> attachedRecetainsumoCollection = new ArrayList<Recetainsumo>();
            for (Recetainsumo recetainsumoCollectionRecetainsumoToAttach : receta.getRecetainsumoCollection()) {
                recetainsumoCollectionRecetainsumoToAttach = em.getReference(recetainsumoCollectionRecetainsumoToAttach.getClass(), recetainsumoCollectionRecetainsumoToAttach.getRecetainsumoPK());
                attachedRecetainsumoCollection.add(recetainsumoCollectionRecetainsumoToAttach);
            }
            receta.setRecetainsumoCollection(attachedRecetainsumoCollection);
            em.persist(receta);
            for (Producto productoCollectionProducto : receta.getProductoCollection()) {
                Receta oldIdRecetaOfProductoCollectionProducto = productoCollectionProducto.getIdReceta();
                productoCollectionProducto.setIdReceta(receta);
                productoCollectionProducto = em.merge(productoCollectionProducto);
                if (oldIdRecetaOfProductoCollectionProducto != null) {
                    oldIdRecetaOfProductoCollectionProducto.getProductoCollection().remove(productoCollectionProducto);
                    oldIdRecetaOfProductoCollectionProducto = em.merge(oldIdRecetaOfProductoCollectionProducto);
                }
            }
            for (Recetainsumo recetainsumoCollectionRecetainsumo : receta.getRecetainsumoCollection()) {
                Receta oldRecetaOfRecetainsumoCollectionRecetainsumo = recetainsumoCollectionRecetainsumo.getReceta();
                recetainsumoCollectionRecetainsumo.setReceta(receta);
                recetainsumoCollectionRecetainsumo = em.merge(recetainsumoCollectionRecetainsumo);
                if (oldRecetaOfRecetainsumoCollectionRecetainsumo != null) {
                    oldRecetaOfRecetainsumoCollectionRecetainsumo.getRecetainsumoCollection().remove(recetainsumoCollectionRecetainsumo);
                    oldRecetaOfRecetainsumoCollectionRecetainsumo = em.merge(oldRecetaOfRecetainsumoCollectionRecetainsumo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Receta receta) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Receta persistentReceta = em.find(Receta.class, receta.getIdReceta());
            Collection<Producto> productoCollectionOld = persistentReceta.getProductoCollection();
            Collection<Producto> productoCollectionNew = receta.getProductoCollection();
            Collection<Recetainsumo> recetainsumoCollectionOld = persistentReceta.getRecetainsumoCollection();
            Collection<Recetainsumo> recetainsumoCollectionNew = receta.getRecetainsumoCollection();
            List<String> illegalOrphanMessages = null;
            for (Producto productoCollectionOldProducto : productoCollectionOld) {
                if (!productoCollectionNew.contains(productoCollectionOldProducto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Producto " + productoCollectionOldProducto + " since its idReceta field is not nullable.");
                }
            }
            for (Recetainsumo recetainsumoCollectionOldRecetainsumo : recetainsumoCollectionOld) {
                if (!recetainsumoCollectionNew.contains(recetainsumoCollectionOldRecetainsumo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Recetainsumo " + recetainsumoCollectionOldRecetainsumo + " since its receta field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Producto> attachedProductoCollectionNew = new ArrayList<Producto>();
            for (Producto productoCollectionNewProductoToAttach : productoCollectionNew) {
                productoCollectionNewProductoToAttach = em.getReference(productoCollectionNewProductoToAttach.getClass(), productoCollectionNewProductoToAttach.getIdProducto());
                attachedProductoCollectionNew.add(productoCollectionNewProductoToAttach);
            }
            productoCollectionNew = attachedProductoCollectionNew;
            receta.setProductoCollection(productoCollectionNew);
            Collection<Recetainsumo> attachedRecetainsumoCollectionNew = new ArrayList<Recetainsumo>();
            for (Recetainsumo recetainsumoCollectionNewRecetainsumoToAttach : recetainsumoCollectionNew) {
                recetainsumoCollectionNewRecetainsumoToAttach = em.getReference(recetainsumoCollectionNewRecetainsumoToAttach.getClass(), recetainsumoCollectionNewRecetainsumoToAttach.getRecetainsumoPK());
                attachedRecetainsumoCollectionNew.add(recetainsumoCollectionNewRecetainsumoToAttach);
            }
            recetainsumoCollectionNew = attachedRecetainsumoCollectionNew;
            receta.setRecetainsumoCollection(recetainsumoCollectionNew);
            receta = em.merge(receta);
            for (Producto productoCollectionNewProducto : productoCollectionNew) {
                if (!productoCollectionOld.contains(productoCollectionNewProducto)) {
                    Receta oldIdRecetaOfProductoCollectionNewProducto = productoCollectionNewProducto.getIdReceta();
                    productoCollectionNewProducto.setIdReceta(receta);
                    productoCollectionNewProducto = em.merge(productoCollectionNewProducto);
                    if (oldIdRecetaOfProductoCollectionNewProducto != null && !oldIdRecetaOfProductoCollectionNewProducto.equals(receta)) {
                        oldIdRecetaOfProductoCollectionNewProducto.getProductoCollection().remove(productoCollectionNewProducto);
                        oldIdRecetaOfProductoCollectionNewProducto = em.merge(oldIdRecetaOfProductoCollectionNewProducto);
                    }
                }
            }
            for (Recetainsumo recetainsumoCollectionNewRecetainsumo : recetainsumoCollectionNew) {
                if (!recetainsumoCollectionOld.contains(recetainsumoCollectionNewRecetainsumo)) {
                    Receta oldRecetaOfRecetainsumoCollectionNewRecetainsumo = recetainsumoCollectionNewRecetainsumo.getReceta();
                    recetainsumoCollectionNewRecetainsumo.setReceta(receta);
                    recetainsumoCollectionNewRecetainsumo = em.merge(recetainsumoCollectionNewRecetainsumo);
                    if (oldRecetaOfRecetainsumoCollectionNewRecetainsumo != null && !oldRecetaOfRecetainsumoCollectionNewRecetainsumo.equals(receta)) {
                        oldRecetaOfRecetainsumoCollectionNewRecetainsumo.getRecetainsumoCollection().remove(recetainsumoCollectionNewRecetainsumo);
                        oldRecetaOfRecetainsumoCollectionNewRecetainsumo = em.merge(oldRecetaOfRecetainsumoCollectionNewRecetainsumo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = receta.getIdReceta();
                if (findReceta(id) == null) {
                    throw new NonexistentEntityException("The receta with id " + id + " no longer exists.");
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
            Receta receta;
            try {
                receta = em.getReference(Receta.class, id);
                receta.getIdReceta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The receta with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Producto> productoCollectionOrphanCheck = receta.getProductoCollection();
            for (Producto productoCollectionOrphanCheckProducto : productoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Receta (" + receta + ") cannot be destroyed since the Producto " + productoCollectionOrphanCheckProducto + " in its productoCollection field has a non-nullable idReceta field.");
            }
            Collection<Recetainsumo> recetainsumoCollectionOrphanCheck = receta.getRecetainsumoCollection();
            for (Recetainsumo recetainsumoCollectionOrphanCheckRecetainsumo : recetainsumoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Receta (" + receta + ") cannot be destroyed since the Recetainsumo " + recetainsumoCollectionOrphanCheckRecetainsumo + " in its recetainsumoCollection field has a non-nullable receta field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(receta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Receta> findRecetaEntities() {
        return findRecetaEntities(true, -1, -1);
    }

    public List<Receta> findRecetaEntities(int maxResults, int firstResult) {
        return findRecetaEntities(false, maxResults, firstResult);
    }

    private List<Receta> findRecetaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Receta.class));
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

    public Receta findReceta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Receta.class, id);
        } finally {
            em.close();
        }
    }

    public int getRecetaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Receta> rt = cq.from(Receta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
