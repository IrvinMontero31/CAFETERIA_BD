/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jpaController;

import Clases_Tabla.exceptions.IllegalOrphanException;
import Clases_Tabla.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Empleado;
import entity.EnumRol;
import entity.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author juanm
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado empleado = usuario.getEmpleado();
            if (empleado != null) {
                empleado = em.getReference(empleado.getClass(), empleado.getIdEmpleado());
                usuario.setEmpleado(empleado);
            }
            EnumRol idRol = usuario.getIdRol();
            if (idRol != null) {
                idRol = em.getReference(idRol.getClass(), idRol.getIdRol());
                usuario.setIdRol(idRol);
            }
            em.persist(usuario);
            if (empleado != null) {
                Usuario oldIdUsuarioOfEmpleado = empleado.getIdUsuario();
                if (oldIdUsuarioOfEmpleado != null) {
                    oldIdUsuarioOfEmpleado.setEmpleado(null);
                    oldIdUsuarioOfEmpleado = em.merge(oldIdUsuarioOfEmpleado);
                }
                empleado.setIdUsuario(usuario);
                empleado = em.merge(empleado);
            }
            if (idRol != null) {
                idRol.getUsuarioCollection().add(usuario);
                idRol = em.merge(idRol);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdUsuario());
            Empleado empleadoOld = persistentUsuario.getEmpleado();
            Empleado empleadoNew = usuario.getEmpleado();
            EnumRol idRolOld = persistentUsuario.getIdRol();
            EnumRol idRolNew = usuario.getIdRol();
            List<String> illegalOrphanMessages = null;
            if (empleadoOld != null && !empleadoOld.equals(empleadoNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Empleado " + empleadoOld + " since its idUsuario field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (empleadoNew != null) {
                empleadoNew = em.getReference(empleadoNew.getClass(), empleadoNew.getIdEmpleado());
                usuario.setEmpleado(empleadoNew);
            }
            if (idRolNew != null) {
                idRolNew = em.getReference(idRolNew.getClass(), idRolNew.getIdRol());
                usuario.setIdRol(idRolNew);
            }
            usuario = em.merge(usuario);
            if (empleadoNew != null && !empleadoNew.equals(empleadoOld)) {
                Usuario oldIdUsuarioOfEmpleado = empleadoNew.getIdUsuario();
                if (oldIdUsuarioOfEmpleado != null) {
                    oldIdUsuarioOfEmpleado.setEmpleado(null);
                    oldIdUsuarioOfEmpleado = em.merge(oldIdUsuarioOfEmpleado);
                }
                empleadoNew.setIdUsuario(usuario);
                empleadoNew = em.merge(empleadoNew);
            }
            if (idRolOld != null && !idRolOld.equals(idRolNew)) {
                idRolOld.getUsuarioCollection().remove(usuario);
                idRolOld = em.merge(idRolOld);
            }
            if (idRolNew != null && !idRolNew.equals(idRolOld)) {
                idRolNew.getUsuarioCollection().add(usuario);
                idRolNew = em.merge(idRolNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getIdUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Empleado empleadoOrphanCheck = usuario.getEmpleado();
            if (empleadoOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Empleado " + empleadoOrphanCheck + " in its empleado field has a non-nullable idUsuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            EnumRol idRol = usuario.getIdRol();
            if (idRol != null) {
                idRol.getUsuarioCollection().remove(usuario);
                idRol = em.merge(idRol);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    } 
   
    
public Usuario validarCredenciales(String username, String password) {
    EntityManager em = getEntityManager();
    try {
        // Buscar usuario por nombre de usuario
        Usuario usuario = em.createNamedQuery("Usuario.findByNombreU", Usuario.class)
                          .setParameter("nombreU", username)
                          .getSingleResult();

        // Si no se encuentra el usuario o no tiene contraseña
        if (usuario == null || usuario.getContrasena() == null) {
            return null;
        }

        String storedPassword = usuario.getContrasena();
        
        // Caso 1: Es un hash BCrypt (empieza con $2a$, $2b$ o $2y$)
        if (storedPassword.startsWith("$2a$") || 
            storedPassword.startsWith("$2b$") || 
            storedPassword.startsWith("$2y$")) {
            
            if (BCrypt.checkpw(password, storedPassword)) {
                return usuario;
            }
        } 
        // Caso 2: Es texto plano (durante transición)
        else if (storedPassword.equals(password)) {
            // Opcional: Actualizar a BCrypt automáticamente al validar
            
            return usuario;
        }
        
        return null;
    } catch (NoResultException e) {
        return null; // Usuario no encontrado
    } catch (IllegalArgumentException e) {
        System.err.println("Error validando contraseña para " + username + ": " + e.getMessage());
        return null;
    } finally {
        em.close();
    }
}
    
}
