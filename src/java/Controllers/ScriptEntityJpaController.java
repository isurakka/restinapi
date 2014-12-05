/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Controllers.exceptions.NonexistentEntityException;
import Controllers.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.ProjectEntity;
import Entities.ScriptEntity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Matti
 */
public class ScriptEntityJpaController implements Serializable {

    public ScriptEntityJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ScriptEntity scriptEntity) throws RollbackFailureException, Exception {
        if (scriptEntity.getProjectEntityCollection() == null) {
            scriptEntity.setProjectEntityCollection(new ArrayList<ProjectEntity>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<ProjectEntity> attachedProjectEntityCollection = new ArrayList<ProjectEntity>();
            for (ProjectEntity projectEntityCollectionProjectEntityToAttach : scriptEntity.getProjectEntityCollection()) {
                projectEntityCollectionProjectEntityToAttach = em.getReference(projectEntityCollectionProjectEntityToAttach.getClass(), projectEntityCollectionProjectEntityToAttach.getName());
                attachedProjectEntityCollection.add(projectEntityCollectionProjectEntityToAttach);
            }
            scriptEntity.setProjectEntityCollection(attachedProjectEntityCollection);
            em.persist(scriptEntity);
            for (ProjectEntity projectEntityCollectionProjectEntity : scriptEntity.getProjectEntityCollection()) {
                ScriptEntity oldScriptIdOfProjectEntityCollectionProjectEntity = projectEntityCollectionProjectEntity.getScriptId();
                projectEntityCollectionProjectEntity.setScriptId(scriptEntity);
                projectEntityCollectionProjectEntity = em.merge(projectEntityCollectionProjectEntity);
                if (oldScriptIdOfProjectEntityCollectionProjectEntity != null) {
                    oldScriptIdOfProjectEntityCollectionProjectEntity.getProjectEntityCollection().remove(projectEntityCollectionProjectEntity);
                    oldScriptIdOfProjectEntityCollectionProjectEntity = em.merge(oldScriptIdOfProjectEntityCollectionProjectEntity);
                }
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

    public void edit(ScriptEntity scriptEntity) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ScriptEntity persistentScriptEntity = em.find(ScriptEntity.class, scriptEntity.getScriptId());
            Collection<ProjectEntity> projectEntityCollectionOld = persistentScriptEntity.getProjectEntityCollection();
            Collection<ProjectEntity> projectEntityCollectionNew = scriptEntity.getProjectEntityCollection();
            Collection<ProjectEntity> attachedProjectEntityCollectionNew = new ArrayList<ProjectEntity>();
            for (ProjectEntity projectEntityCollectionNewProjectEntityToAttach : projectEntityCollectionNew) {
                projectEntityCollectionNewProjectEntityToAttach = em.getReference(projectEntityCollectionNewProjectEntityToAttach.getClass(), projectEntityCollectionNewProjectEntityToAttach.getName());
                attachedProjectEntityCollectionNew.add(projectEntityCollectionNewProjectEntityToAttach);
            }
            projectEntityCollectionNew = attachedProjectEntityCollectionNew;
            scriptEntity.setProjectEntityCollection(projectEntityCollectionNew);
            scriptEntity = em.merge(scriptEntity);
            for (ProjectEntity projectEntityCollectionOldProjectEntity : projectEntityCollectionOld) {
                if (!projectEntityCollectionNew.contains(projectEntityCollectionOldProjectEntity)) {
                    projectEntityCollectionOldProjectEntity.setScriptId(null);
                    projectEntityCollectionOldProjectEntity = em.merge(projectEntityCollectionOldProjectEntity);
                }
            }
            for (ProjectEntity projectEntityCollectionNewProjectEntity : projectEntityCollectionNew) {
                if (!projectEntityCollectionOld.contains(projectEntityCollectionNewProjectEntity)) {
                    ScriptEntity oldScriptIdOfProjectEntityCollectionNewProjectEntity = projectEntityCollectionNewProjectEntity.getScriptId();
                    projectEntityCollectionNewProjectEntity.setScriptId(scriptEntity);
                    projectEntityCollectionNewProjectEntity = em.merge(projectEntityCollectionNewProjectEntity);
                    if (oldScriptIdOfProjectEntityCollectionNewProjectEntity != null && !oldScriptIdOfProjectEntityCollectionNewProjectEntity.equals(scriptEntity)) {
                        oldScriptIdOfProjectEntityCollectionNewProjectEntity.getProjectEntityCollection().remove(projectEntityCollectionNewProjectEntity);
                        oldScriptIdOfProjectEntityCollectionNewProjectEntity = em.merge(oldScriptIdOfProjectEntityCollectionNewProjectEntity);
                    }
                }
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
                Integer id = scriptEntity.getScriptId();
                if (findScriptEntity(id) == null) {
                    throw new NonexistentEntityException("The scriptEntity with id " + id + " no longer exists.");
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
            ScriptEntity scriptEntity;
            try {
                scriptEntity = em.getReference(ScriptEntity.class, id);
                scriptEntity.getScriptId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The scriptEntity with id " + id + " no longer exists.", enfe);
            }
            Collection<ProjectEntity> projectEntityCollection = scriptEntity.getProjectEntityCollection();
            for (ProjectEntity projectEntityCollectionProjectEntity : projectEntityCollection) {
                projectEntityCollectionProjectEntity.setScriptId(null);
                projectEntityCollectionProjectEntity = em.merge(projectEntityCollectionProjectEntity);
            }
            em.remove(scriptEntity);
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

    public List<ScriptEntity> findScriptEntityEntities() {
        return findScriptEntityEntities(true, -1, -1);
    }

    public List<ScriptEntity> findScriptEntityEntities(int maxResults, int firstResult) {
        return findScriptEntityEntities(false, maxResults, firstResult);
    }

    private List<ScriptEntity> findScriptEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ScriptEntity.class));
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

    public ScriptEntity findScriptEntity(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ScriptEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getScriptEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ScriptEntity> rt = cq.from(ScriptEntity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
