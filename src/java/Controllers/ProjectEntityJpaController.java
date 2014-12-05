/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Controllers.exceptions.NonexistentEntityException;
import Controllers.exceptions.PreexistingEntityException;
import Controllers.exceptions.RollbackFailureException;
import Entities.ProjectEntity;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.UserEntity;
import Entities.ScriptEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Matti
 */
public class ProjectEntityJpaController implements Serializable {

    public ProjectEntityJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ProjectEntity projectEntity) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            UserEntity userName = projectEntity.getUserName();
            if (userName != null) {
                userName = em.getReference(userName.getClass(), userName.getName());
                projectEntity.setUserName(userName);
            }
            ScriptEntity scriptId = projectEntity.getScriptId();
            if (scriptId != null) {
                scriptId = em.getReference(scriptId.getClass(), scriptId.getScriptId());
                projectEntity.setScriptId(scriptId);
            }
            em.persist(projectEntity);
            if (userName != null) {
                userName.getProjectEntityCollection().add(projectEntity);
                userName = em.merge(userName);
            }
            if (scriptId != null) {
                scriptId.getProjectEntityCollection().add(projectEntity);
                scriptId = em.merge(scriptId);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findProjectEntity(projectEntity.getName()) != null) {
                throw new PreexistingEntityException("ProjectEntity " + projectEntity + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ProjectEntity projectEntity) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ProjectEntity persistentProjectEntity = em.find(ProjectEntity.class, projectEntity.getName());
            UserEntity userNameOld = persistentProjectEntity.getUserName();
            UserEntity userNameNew = projectEntity.getUserName();
            ScriptEntity scriptIdOld = persistentProjectEntity.getScriptId();
            ScriptEntity scriptIdNew = projectEntity.getScriptId();
            if (userNameNew != null) {
                userNameNew = em.getReference(userNameNew.getClass(), userNameNew.getName());
                projectEntity.setUserName(userNameNew);
            }
            if (scriptIdNew != null) {
                scriptIdNew = em.getReference(scriptIdNew.getClass(), scriptIdNew.getScriptId());
                projectEntity.setScriptId(scriptIdNew);
            }
            projectEntity = em.merge(projectEntity);
            if (userNameOld != null && !userNameOld.equals(userNameNew)) {
                userNameOld.getProjectEntityCollection().remove(projectEntity);
                userNameOld = em.merge(userNameOld);
            }
            if (userNameNew != null && !userNameNew.equals(userNameOld)) {
                userNameNew.getProjectEntityCollection().add(projectEntity);
                userNameNew = em.merge(userNameNew);
            }
            if (scriptIdOld != null && !scriptIdOld.equals(scriptIdNew)) {
                scriptIdOld.getProjectEntityCollection().remove(projectEntity);
                scriptIdOld = em.merge(scriptIdOld);
            }
            if (scriptIdNew != null && !scriptIdNew.equals(scriptIdOld)) {
                scriptIdNew.getProjectEntityCollection().add(projectEntity);
                scriptIdNew = em.merge(scriptIdNew);
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
                String id = projectEntity.getName();
                if (findProjectEntity(id) == null) {
                    throw new NonexistentEntityException("The projectEntity with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ProjectEntity projectEntity;
            try {
                projectEntity = em.getReference(ProjectEntity.class, id);
                projectEntity.getName();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The projectEntity with id " + id + " no longer exists.", enfe);
            }
            UserEntity userName = projectEntity.getUserName();
            if (userName != null) {
                userName.getProjectEntityCollection().remove(projectEntity);
                userName = em.merge(userName);
            }
            ScriptEntity scriptId = projectEntity.getScriptId();
            if (scriptId != null) {
                scriptId.getProjectEntityCollection().remove(projectEntity);
                scriptId = em.merge(scriptId);
            }
            em.remove(projectEntity);
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

    public List<ProjectEntity> findProjectEntityEntities() {
        return findProjectEntityEntities(true, -1, -1);
    }

    public List<ProjectEntity> findProjectEntityEntities(int maxResults, int firstResult) {
        return findProjectEntityEntities(false, maxResults, firstResult);
    }

    private List<ProjectEntity> findProjectEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProjectEntity.class));
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

    public ProjectEntity findProjectEntity(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProjectEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getProjectEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProjectEntity> rt = cq.from(ProjectEntity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
