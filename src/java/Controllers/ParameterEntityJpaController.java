/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Controllers.exceptions.NonexistentEntityException;
import Controllers.exceptions.PreexistingEntityException;
import Controllers.exceptions.RollbackFailureException;
import Entities.ParameterEntity;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.RequestEntity;
import Entities.ProjectEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Matti
 */
public class ParameterEntityJpaController implements Serializable {

    public ParameterEntityJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ParameterEntity parameterEntity) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            RequestEntity requestId = parameterEntity.getRequestId();
            if (requestId != null) {
                requestId = em.getReference(requestId.getClass(), requestId.getProjectId());
                parameterEntity.setRequestId(requestId);
            }
            ProjectEntity projectName = parameterEntity.getProjectName();
            if (projectName != null) {
                projectName = em.getReference(projectName.getClass(), projectName.getName());
                parameterEntity.setProjectName(projectName);
            }
            em.persist(parameterEntity);
            if (requestId != null) {
                requestId.getParameterEntityCollection().add(parameterEntity);
                requestId = em.merge(requestId);
            }
            if (projectName != null) {
                projectName.getParameterCollection().add(parameterEntity);
                projectName = em.merge(projectName);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findParameterEntity(parameterEntity.getParameterId()) != null) {
                throw new PreexistingEntityException("ParameterEntity " + parameterEntity + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ParameterEntity parameterEntity) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ParameterEntity persistentParameterEntity = em.find(ParameterEntity.class, parameterEntity.getParameterId());
            RequestEntity requestIdOld = persistentParameterEntity.getRequestId();
            RequestEntity requestIdNew = parameterEntity.getRequestId();
            ProjectEntity projectNameOld = persistentParameterEntity.getProjectName();
            ProjectEntity projectNameNew = parameterEntity.getProjectName();
            if (requestIdNew != null) {
                requestIdNew = em.getReference(requestIdNew.getClass(), requestIdNew.getProjectId());
                parameterEntity.setRequestId(requestIdNew);
            }
            if (projectNameNew != null) {
                projectNameNew = em.getReference(projectNameNew.getClass(), projectNameNew.getName());
                parameterEntity.setProjectName(projectNameNew);
            }
            parameterEntity = em.merge(parameterEntity);
            if (requestIdOld != null && !requestIdOld.equals(requestIdNew)) {
                requestIdOld.getParameterEntityCollection().remove(parameterEntity);
                requestIdOld = em.merge(requestIdOld);
            }
            if (requestIdNew != null && !requestIdNew.equals(requestIdOld)) {
                requestIdNew.getParameterEntityCollection().add(parameterEntity);
                requestIdNew = em.merge(requestIdNew);
            }
            if (projectNameOld != null && !projectNameOld.equals(projectNameNew)) {
                projectNameOld.getParameterCollection().remove(parameterEntity);
                projectNameOld = em.merge(projectNameOld);
            }
            if (projectNameNew != null && !projectNameNew.equals(projectNameOld)) {
                projectNameNew.getParameterCollection().add(parameterEntity);
                projectNameNew = em.merge(projectNameNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = parameterEntity.getParameterId();
                if (findParameterEntity(id) == null) {
                    throw new NonexistentEntityException("The parameterEntity with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ParameterEntity parameterEntity;
            try {
                parameterEntity = em.getReference(ParameterEntity.class, id);
                parameterEntity.getParameterId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The parameterEntity with id " + id + " no longer exists.", enfe);
            }
            RequestEntity requestId = parameterEntity.getRequestId();
            if (requestId != null) {
                requestId.getParameterEntityCollection().remove(parameterEntity);
                requestId = em.merge(requestId);
            }
            ProjectEntity projectName = parameterEntity.getProjectName();
            if (projectName != null) {
                projectName.getParameterCollection().remove(parameterEntity);
                projectName = em.merge(projectName);
            }
            em.remove(parameterEntity);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ParameterEntity> findParameterEntityEntities() {
        return findParameterEntityEntities(true, -1, -1);
    }

    public List<ParameterEntity> findParameterEntityEntities(int maxResults, int firstResult) {
        return findParameterEntityEntities(false, maxResults, firstResult);
    }

    private List<ParameterEntity> findParameterEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ParameterEntity.class));
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

    public ParameterEntity findParameterEntity(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ParameterEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getParameterEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ParameterEntity> rt = cq.from(ParameterEntity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
