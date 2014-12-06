/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Controllers.exceptions.IllegalOrphanException;
import Controllers.exceptions.NonexistentEntityException;
import Controllers.exceptions.PreexistingEntityException;
import Controllers.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.UserEntity;
import Entities.ScriptEntity;
import Entities.ParameterEntity;
import Entities.ProjectEntity;
import java.util.ArrayList;
import java.util.Collection;
import Entities.RequestEntity;
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
        if (projectEntity.getParameterCollection() == null) {
            projectEntity.setParameterCollection(new ArrayList<ParameterEntity>());
        }
        if (projectEntity.getRequestCollection() == null) {
            projectEntity.setRequestCollection(new ArrayList<RequestEntity>());
        }
        if (projectEntity.getParameterEntityCollection() == null) {
            projectEntity.setParameterEntityCollection(new ArrayList<ParameterEntity>());
        }
        if (projectEntity.getRequestEntityCollection() == null) {
            projectEntity.setRequestEntityCollection(new ArrayList<RequestEntity>());
        }
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
            Collection<ParameterEntity> attachedParameterCollection = new ArrayList<ParameterEntity>();
            for (ParameterEntity parameterCollectionParameterEntityToAttach : projectEntity.getParameterCollection()) {
                parameterCollectionParameterEntityToAttach = em.getReference(parameterCollectionParameterEntityToAttach.getClass(), parameterCollectionParameterEntityToAttach.getParameterId());
                attachedParameterCollection.add(parameterCollectionParameterEntityToAttach);
            }
            projectEntity.setParameterCollection(attachedParameterCollection);
            Collection<RequestEntity> attachedRequestCollection = new ArrayList<RequestEntity>();
            for (RequestEntity requestCollectionRequestEntityToAttach : projectEntity.getRequestCollection()) {
                requestCollectionRequestEntityToAttach = em.getReference(requestCollectionRequestEntityToAttach.getClass(), requestCollectionRequestEntityToAttach.getProjectId());
                attachedRequestCollection.add(requestCollectionRequestEntityToAttach);
            }
            projectEntity.setRequestCollection(attachedRequestCollection);
            Collection<ParameterEntity> attachedParameterEntityCollection = new ArrayList<ParameterEntity>();
            for (ParameterEntity parameterEntityCollectionParameterEntityToAttach : projectEntity.getParameterEntityCollection()) {
                parameterEntityCollectionParameterEntityToAttach = em.getReference(parameterEntityCollectionParameterEntityToAttach.getClass(), parameterEntityCollectionParameterEntityToAttach.getParameterId());
                attachedParameterEntityCollection.add(parameterEntityCollectionParameterEntityToAttach);
            }
            projectEntity.setParameterEntityCollection(attachedParameterEntityCollection);
            Collection<RequestEntity> attachedRequestEntityCollection = new ArrayList<RequestEntity>();
            for (RequestEntity requestEntityCollectionRequestEntityToAttach : projectEntity.getRequestEntityCollection()) {
                requestEntityCollectionRequestEntityToAttach = em.getReference(requestEntityCollectionRequestEntityToAttach.getClass(), requestEntityCollectionRequestEntityToAttach.getProjectId());
                attachedRequestEntityCollection.add(requestEntityCollectionRequestEntityToAttach);
            }
            projectEntity.setRequestEntityCollection(attachedRequestEntityCollection);
            em.persist(projectEntity);
            if (userName != null) {
                userName.getProjectEntityCollection().add(projectEntity);
                userName = em.merge(userName);
            }
            if (scriptId != null) {
                scriptId.getProjectEntityCollection().add(projectEntity);
                scriptId = em.merge(scriptId);
            }
            for (ParameterEntity parameterCollectionParameterEntity : projectEntity.getParameterCollection()) {
                ProjectEntity oldProjectNameOfParameterCollectionParameterEntity = parameterCollectionParameterEntity.getProjectName();
                parameterCollectionParameterEntity.setProjectName(projectEntity);
                parameterCollectionParameterEntity = em.merge(parameterCollectionParameterEntity);
                if (oldProjectNameOfParameterCollectionParameterEntity != null) {
                    oldProjectNameOfParameterCollectionParameterEntity.getParameterCollection().remove(parameterCollectionParameterEntity);
                    oldProjectNameOfParameterCollectionParameterEntity = em.merge(oldProjectNameOfParameterCollectionParameterEntity);
                }
            }
            for (RequestEntity requestCollectionRequestEntity : projectEntity.getRequestCollection()) {
                ProjectEntity oldProjectNameOfRequestCollectionRequestEntity = requestCollectionRequestEntity.getProjectName();
                requestCollectionRequestEntity.setProjectName(projectEntity);
                requestCollectionRequestEntity = em.merge(requestCollectionRequestEntity);
                if (oldProjectNameOfRequestCollectionRequestEntity != null) {
                    oldProjectNameOfRequestCollectionRequestEntity.getRequestCollection().remove(requestCollectionRequestEntity);
                    oldProjectNameOfRequestCollectionRequestEntity = em.merge(oldProjectNameOfRequestCollectionRequestEntity);
                }
            }
            for (ParameterEntity parameterEntityCollectionParameterEntity : projectEntity.getParameterEntityCollection()) {
                ProjectEntity oldProjectNameOfParameterEntityCollectionParameterEntity = parameterEntityCollectionParameterEntity.getProjectName();
                parameterEntityCollectionParameterEntity.setProjectName(projectEntity);
                parameterEntityCollectionParameterEntity = em.merge(parameterEntityCollectionParameterEntity);
                if (oldProjectNameOfParameterEntityCollectionParameterEntity != null) {
                    oldProjectNameOfParameterEntityCollectionParameterEntity.getParameterEntityCollection().remove(parameterEntityCollectionParameterEntity);
                    oldProjectNameOfParameterEntityCollectionParameterEntity = em.merge(oldProjectNameOfParameterEntityCollectionParameterEntity);
                }
            }
            for (RequestEntity requestEntityCollectionRequestEntity : projectEntity.getRequestEntityCollection()) {
                ProjectEntity oldProjectNameOfRequestEntityCollectionRequestEntity = requestEntityCollectionRequestEntity.getProjectName();
                requestEntityCollectionRequestEntity.setProjectName(projectEntity);
                requestEntityCollectionRequestEntity = em.merge(requestEntityCollectionRequestEntity);
                if (oldProjectNameOfRequestEntityCollectionRequestEntity != null) {
                    oldProjectNameOfRequestEntityCollectionRequestEntity.getRequestEntityCollection().remove(requestEntityCollectionRequestEntity);
                    oldProjectNameOfRequestEntityCollectionRequestEntity = em.merge(oldProjectNameOfRequestEntityCollectionRequestEntity);
                }
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

    public void edit(ProjectEntity projectEntity) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ProjectEntity persistentProjectEntity = em.find(ProjectEntity.class, projectEntity.getName());
            UserEntity userNameOld = persistentProjectEntity.getUserName();
            UserEntity userNameNew = projectEntity.getUserName();
            ScriptEntity scriptIdOld = persistentProjectEntity.getScriptId();
            ScriptEntity scriptIdNew = projectEntity.getScriptId();
            Collection<ParameterEntity> parameterCollectionOld = persistentProjectEntity.getParameterCollection();
            Collection<ParameterEntity> parameterCollectionNew = projectEntity.getParameterCollection();
            Collection<RequestEntity> requestCollectionOld = persistentProjectEntity.getRequestCollection();
            Collection<RequestEntity> requestCollectionNew = projectEntity.getRequestCollection();
            Collection<ParameterEntity> parameterEntityCollectionOld = persistentProjectEntity.getParameterEntityCollection();
            Collection<ParameterEntity> parameterEntityCollectionNew = projectEntity.getParameterEntityCollection();
            Collection<RequestEntity> requestEntityCollectionOld = persistentProjectEntity.getRequestEntityCollection();
            Collection<RequestEntity> requestEntityCollectionNew = projectEntity.getRequestEntityCollection();
            List<String> illegalOrphanMessages = null;
            for (RequestEntity requestCollectionOldRequestEntity : requestCollectionOld) {
                if (!requestCollectionNew.contains(requestCollectionOldRequestEntity)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RequestEntity " + requestCollectionOldRequestEntity + " since its projectName field is not nullable.");
                }
            }
            for (RequestEntity requestEntityCollectionOldRequestEntity : requestEntityCollectionOld) {
                if (!requestEntityCollectionNew.contains(requestEntityCollectionOldRequestEntity)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RequestEntity " + requestEntityCollectionOldRequestEntity + " since its projectName field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (userNameNew != null) {
                userNameNew = em.getReference(userNameNew.getClass(), userNameNew.getName());
                projectEntity.setUserName(userNameNew);
            }
            if (scriptIdNew != null) {
                scriptIdNew = em.getReference(scriptIdNew.getClass(), scriptIdNew.getScriptId());
                projectEntity.setScriptId(scriptIdNew);
            }
            Collection<ParameterEntity> attachedParameterCollectionNew = new ArrayList<ParameterEntity>();
            for (ParameterEntity parameterCollectionNewParameterEntityToAttach : parameterCollectionNew) {
                parameterCollectionNewParameterEntityToAttach = em.getReference(parameterCollectionNewParameterEntityToAttach.getClass(), parameterCollectionNewParameterEntityToAttach.getParameterId());
                attachedParameterCollectionNew.add(parameterCollectionNewParameterEntityToAttach);
            }
            parameterCollectionNew = attachedParameterCollectionNew;
            projectEntity.setParameterCollection(parameterCollectionNew);
            Collection<RequestEntity> attachedRequestCollectionNew = new ArrayList<RequestEntity>();
            for (RequestEntity requestCollectionNewRequestEntityToAttach : requestCollectionNew) {
                requestCollectionNewRequestEntityToAttach = em.getReference(requestCollectionNewRequestEntityToAttach.getClass(), requestCollectionNewRequestEntityToAttach.getProjectId());
                attachedRequestCollectionNew.add(requestCollectionNewRequestEntityToAttach);
            }
            requestCollectionNew = attachedRequestCollectionNew;
            projectEntity.setRequestCollection(requestCollectionNew);
            Collection<ParameterEntity> attachedParameterEntityCollectionNew = new ArrayList<ParameterEntity>();
            for (ParameterEntity parameterEntityCollectionNewParameterEntityToAttach : parameterEntityCollectionNew) {
                parameterEntityCollectionNewParameterEntityToAttach = em.getReference(parameterEntityCollectionNewParameterEntityToAttach.getClass(), parameterEntityCollectionNewParameterEntityToAttach.getParameterId());
                attachedParameterEntityCollectionNew.add(parameterEntityCollectionNewParameterEntityToAttach);
            }
            parameterEntityCollectionNew = attachedParameterEntityCollectionNew;
            projectEntity.setParameterEntityCollection(parameterEntityCollectionNew);
            Collection<RequestEntity> attachedRequestEntityCollectionNew = new ArrayList<RequestEntity>();
            for (RequestEntity requestEntityCollectionNewRequestEntityToAttach : requestEntityCollectionNew) {
                requestEntityCollectionNewRequestEntityToAttach = em.getReference(requestEntityCollectionNewRequestEntityToAttach.getClass(), requestEntityCollectionNewRequestEntityToAttach.getProjectId());
                attachedRequestEntityCollectionNew.add(requestEntityCollectionNewRequestEntityToAttach);
            }
            requestEntityCollectionNew = attachedRequestEntityCollectionNew;
            projectEntity.setRequestEntityCollection(requestEntityCollectionNew);
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
            for (ParameterEntity parameterCollectionOldParameterEntity : parameterCollectionOld) {
                if (!parameterCollectionNew.contains(parameterCollectionOldParameterEntity)) {
                    parameterCollectionOldParameterEntity.setProjectName(null);
                    parameterCollectionOldParameterEntity = em.merge(parameterCollectionOldParameterEntity);
                }
            }
            for (ParameterEntity parameterCollectionNewParameterEntity : parameterCollectionNew) {
                if (!parameterCollectionOld.contains(parameterCollectionNewParameterEntity)) {
                    ProjectEntity oldProjectNameOfParameterCollectionNewParameterEntity = parameterCollectionNewParameterEntity.getProjectName();
                    parameterCollectionNewParameterEntity.setProjectName(projectEntity);
                    parameterCollectionNewParameterEntity = em.merge(parameterCollectionNewParameterEntity);
                    if (oldProjectNameOfParameterCollectionNewParameterEntity != null && !oldProjectNameOfParameterCollectionNewParameterEntity.equals(projectEntity)) {
                        oldProjectNameOfParameterCollectionNewParameterEntity.getParameterCollection().remove(parameterCollectionNewParameterEntity);
                        oldProjectNameOfParameterCollectionNewParameterEntity = em.merge(oldProjectNameOfParameterCollectionNewParameterEntity);
                    }
                }
            }
            for (RequestEntity requestCollectionNewRequestEntity : requestCollectionNew) {
                if (!requestCollectionOld.contains(requestCollectionNewRequestEntity)) {
                    ProjectEntity oldProjectNameOfRequestCollectionNewRequestEntity = requestCollectionNewRequestEntity.getProjectName();
                    requestCollectionNewRequestEntity.setProjectName(projectEntity);
                    requestCollectionNewRequestEntity = em.merge(requestCollectionNewRequestEntity);
                    if (oldProjectNameOfRequestCollectionNewRequestEntity != null && !oldProjectNameOfRequestCollectionNewRequestEntity.equals(projectEntity)) {
                        oldProjectNameOfRequestCollectionNewRequestEntity.getRequestCollection().remove(requestCollectionNewRequestEntity);
                        oldProjectNameOfRequestCollectionNewRequestEntity = em.merge(oldProjectNameOfRequestCollectionNewRequestEntity);
                    }
                }
            }
            for (ParameterEntity parameterEntityCollectionOldParameterEntity : parameterEntityCollectionOld) {
                if (!parameterEntityCollectionNew.contains(parameterEntityCollectionOldParameterEntity)) {
                    parameterEntityCollectionOldParameterEntity.setProjectName(null);
                    parameterEntityCollectionOldParameterEntity = em.merge(parameterEntityCollectionOldParameterEntity);
                }
            }
            for (ParameterEntity parameterEntityCollectionNewParameterEntity : parameterEntityCollectionNew) {
                if (!parameterEntityCollectionOld.contains(parameterEntityCollectionNewParameterEntity)) {
                    ProjectEntity oldProjectNameOfParameterEntityCollectionNewParameterEntity = parameterEntityCollectionNewParameterEntity.getProjectName();
                    parameterEntityCollectionNewParameterEntity.setProjectName(projectEntity);
                    parameterEntityCollectionNewParameterEntity = em.merge(parameterEntityCollectionNewParameterEntity);
                    if (oldProjectNameOfParameterEntityCollectionNewParameterEntity != null && !oldProjectNameOfParameterEntityCollectionNewParameterEntity.equals(projectEntity)) {
                        oldProjectNameOfParameterEntityCollectionNewParameterEntity.getParameterEntityCollection().remove(parameterEntityCollectionNewParameterEntity);
                        oldProjectNameOfParameterEntityCollectionNewParameterEntity = em.merge(oldProjectNameOfParameterEntityCollectionNewParameterEntity);
                    }
                }
            }
            for (RequestEntity requestEntityCollectionNewRequestEntity : requestEntityCollectionNew) {
                if (!requestEntityCollectionOld.contains(requestEntityCollectionNewRequestEntity)) {
                    ProjectEntity oldProjectNameOfRequestEntityCollectionNewRequestEntity = requestEntityCollectionNewRequestEntity.getProjectName();
                    requestEntityCollectionNewRequestEntity.setProjectName(projectEntity);
                    requestEntityCollectionNewRequestEntity = em.merge(requestEntityCollectionNewRequestEntity);
                    if (oldProjectNameOfRequestEntityCollectionNewRequestEntity != null && !oldProjectNameOfRequestEntityCollectionNewRequestEntity.equals(projectEntity)) {
                        oldProjectNameOfRequestEntityCollectionNewRequestEntity.getRequestEntityCollection().remove(requestEntityCollectionNewRequestEntity);
                        oldProjectNameOfRequestEntityCollectionNewRequestEntity = em.merge(oldProjectNameOfRequestEntityCollectionNewRequestEntity);
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

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
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
            List<String> illegalOrphanMessages = null;
            Collection<RequestEntity> requestCollectionOrphanCheck = projectEntity.getRequestCollection();
            for (RequestEntity requestCollectionOrphanCheckRequestEntity : requestCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ProjectEntity (" + projectEntity + ") cannot be destroyed since the RequestEntity " + requestCollectionOrphanCheckRequestEntity + " in its requestCollection field has a non-nullable projectName field.");
            }
            Collection<RequestEntity> requestEntityCollectionOrphanCheck = projectEntity.getRequestEntityCollection();
            for (RequestEntity requestEntityCollectionOrphanCheckRequestEntity : requestEntityCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ProjectEntity (" + projectEntity + ") cannot be destroyed since the RequestEntity " + requestEntityCollectionOrphanCheckRequestEntity + " in its requestEntityCollection field has a non-nullable projectName field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
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
            Collection<ParameterEntity> parameterCollection = projectEntity.getParameterCollection();
            for (ParameterEntity parameterCollectionParameterEntity : parameterCollection) {
                parameterCollectionParameterEntity.setProjectName(null);
                parameterCollectionParameterEntity = em.merge(parameterCollectionParameterEntity);
            }
            Collection<ParameterEntity> parameterEntityCollection = projectEntity.getParameterEntityCollection();
            for (ParameterEntity parameterEntityCollectionParameterEntity : parameterEntityCollection) {
                parameterEntityCollectionParameterEntity.setProjectName(null);
                parameterEntityCollectionParameterEntity = em.merge(parameterEntityCollectionParameterEntity);
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
