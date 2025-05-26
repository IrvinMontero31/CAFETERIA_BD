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
import entity.Pedido;
import entity.Pedidoproducto;
import entity.Producto;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author juanm
 */
public class PedidoproductoJpaController implements Serializable {

    public PedidoproductoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pedidoproducto pedidoproducto) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pedido idPedido = pedidoproducto.getIdPedido();
            if (idPedido != null) {
                idPedido = em.getReference(idPedido.getClass(), idPedido.getIdPedido());
                pedidoproducto.setIdPedido(idPedido);
            }
            Producto idProducto = pedidoproducto.getIdProducto();
            if (idProducto != null) {
                idProducto = em.getReference(idProducto.getClass(), idProducto.getIdProducto());
                pedidoproducto.setIdProducto(idProducto);
            }
            em.persist(pedidoproducto);
            if (idPedido != null) {
                idPedido.getPedidoproductoCollection().add(pedidoproducto);
                idPedido = em.merge(idPedido);
            }
            if (idProducto != null) {
                idProducto.getPedidoproductoCollection().add(pedidoproducto);
                idProducto = em.merge(idProducto);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pedidoproducto pedidoproducto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pedidoproducto persistentPedidoproducto = em.find(Pedidoproducto.class, pedidoproducto.getIdDetalle());
            Pedido idPedidoOld = persistentPedidoproducto.getIdPedido();
            Pedido idPedidoNew = pedidoproducto.getIdPedido();
            Producto idProductoOld = persistentPedidoproducto.getIdProducto();
            Producto idProductoNew = pedidoproducto.getIdProducto();
            if (idPedidoNew != null) {
                idPedidoNew = em.getReference(idPedidoNew.getClass(), idPedidoNew.getIdPedido());
                pedidoproducto.setIdPedido(idPedidoNew);
            }
            if (idProductoNew != null) {
                idProductoNew = em.getReference(idProductoNew.getClass(), idProductoNew.getIdProducto());
                pedidoproducto.setIdProducto(idProductoNew);
            }
            pedidoproducto = em.merge(pedidoproducto);
            if (idPedidoOld != null && !idPedidoOld.equals(idPedidoNew)) {
                idPedidoOld.getPedidoproductoCollection().remove(pedidoproducto);
                idPedidoOld = em.merge(idPedidoOld);
            }
            if (idPedidoNew != null && !idPedidoNew.equals(idPedidoOld)) {
                idPedidoNew.getPedidoproductoCollection().add(pedidoproducto);
                idPedidoNew = em.merge(idPedidoNew);
            }
            if (idProductoOld != null && !idProductoOld.equals(idProductoNew)) {
                idProductoOld.getPedidoproductoCollection().remove(pedidoproducto);
                idProductoOld = em.merge(idProductoOld);
            }
            if (idProductoNew != null && !idProductoNew.equals(idProductoOld)) {
                idProductoNew.getPedidoproductoCollection().add(pedidoproducto);
                idProductoNew = em.merge(idProductoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pedidoproducto.getIdDetalle();
                if (findPedidoproducto(id) == null) {
                    throw new NonexistentEntityException("The pedidoproducto with id " + id + " no longer exists.");
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
            Pedidoproducto pedidoproducto;
            try {
                pedidoproducto = em.getReference(Pedidoproducto.class, id);
                pedidoproducto.getIdDetalle();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pedidoproducto with id " + id + " no longer exists.", enfe);
            }
            Pedido idPedido = pedidoproducto.getIdPedido();
            if (idPedido != null) {
                idPedido.getPedidoproductoCollection().remove(pedidoproducto);
                idPedido = em.merge(idPedido);
            }
            Producto idProducto = pedidoproducto.getIdProducto();
            if (idProducto != null) {
                idProducto.getPedidoproductoCollection().remove(pedidoproducto);
                idProducto = em.merge(idProducto);
            }
            em.remove(pedidoproducto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pedidoproducto> findPedidoproductoEntities() {
        return findPedidoproductoEntities(true, -1, -1);
    }

    public List<Pedidoproducto> findPedidoproductoEntities(int maxResults, int firstResult) {
        return findPedidoproductoEntities(false, maxResults, firstResult);
    }

    private List<Pedidoproducto> findPedidoproductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pedidoproducto.class));
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

    public Pedidoproducto findPedidoproducto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pedidoproducto.class, id);
        } finally {
            em.close();
        }
    }

    public int getPedidoproductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pedidoproducto> rt = cq.from(Pedidoproducto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
