/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Controllers.exceptions.NonexistentEntityException;
import Controllers.exceptions.RollbackFailureException;
import Entities.ParameterEntity;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.ProjectEntity;
import Entities.RequestEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Administrator
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

    public void create(ParameterEntity parameterEntity) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ProjectEntity projectName = parameterEntity.getProjectName();
            if (projectName != null) {
                projectName = em.getReference(projectName.getClass(), projectName.getName());
                parameterEntity.setProjectName(projectName);
            }
            RequestEntity requestId = parameterEntity.getRequestId();
            if (requestId != null) {
                requestId = em.getReference(requestId.getClass(), requestId.getRequestId());
                parameterEntity.setRequestId(requestId);
            }
            em.persist(parameterEntity);
            if (projectName != null) {
                projectName.getParameterEntityList().add(parameterEntity);
                projectName = em.merge(projectName);
            }
            if (requestId != null) {
                requestId.getParameterEntityList().add(parameterEntity);
                requestId = em.merge(requestId);
            }
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

    public void edit(ParameterEntity parameterEntity) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ParameterEntity persistentParameterEntity = em.find(ParameterEntity.class, parameterEntity.getParameterId());
            ProjectEntity projectNameOld = persistentParameterEntity.getProjectName();
            ProjectEntity projectNameNew = parameterEntity.getProjectName();
            RequestEntity requestIdOld = persistentParameterEntity.getRequestId();
            RequestEntity requestIdNew = parameterEntity.getRequestId();
            if (projectNameNew != null) {
                projectNameNew = em.getReference(projectNameNew.getClass(), projectNameNew.getName());
                parameterEntity.setProjectName(projectNameNew);
            }
            if (requestIdNew != null) {
                requestIdNew = em.getReference(requestIdNew.getClass(), requestIdNew.getRequestId());
                parameterEntity.setRequestId(requestIdNew);
            }
            parameterEntity = em.merge(parameterEntity);
            if (projectNameOld != null && !projectNameOld.equals(projectNameNew)) {
                projectNameOld.getParameterEntityList().remove(parameterEntity);
                projectNameOld = em.merge(projectNameOld);
            }
            if (projectNameNew != null && !projectNameNew.equals(projectNameOld)) {
                projectNameNew.getParameterEntityList().add(parameterEntity);
                projectNameNew = em.merge(projectNameNew);
            }
            if (requestIdOld != null && !requestIdOld.equals(requestIdNew)) {
                requestIdOld.getParameterEntityList().remove(parameterEntity);
                requestIdOld = em.merge(requestIdOld);
            }
            if (requestIdNew != null && !requestIdNew.equals(requestIdOld)) {
                requestIdNew.getParameterEntityList().add(parameterEntity);
                requestIdNew = em.merge(requestIdNew);
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
            ProjectEntity projectName = parameterEntity.getProjectName();
            if (projectName != null) {
                projectName.getParameterEntityList().remove(parameterEntity);
                projectName = em.merge(projectName);
            }
            RequestEntity requestId = parameterEntity.getRequestId();
            if (requestId != null) {
                requestId.getParameterEntityList().remove(parameterEntity);
                requestId = em.merge(requestId);
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
