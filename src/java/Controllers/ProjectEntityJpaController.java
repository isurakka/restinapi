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
import Entities.RequestEntity;
import java.util.ArrayList;
import java.util.List;
import Entities.ParameterEntity;
import Entities.ProjectEntity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Administrator
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
        if (projectEntity.getRequestEntityList() == null) {
            projectEntity.setRequestEntityList(new ArrayList<RequestEntity>());
        }
        if (projectEntity.getParameterEntityList() == null) {
            projectEntity.setParameterEntityList(new ArrayList<ParameterEntity>());
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
            List<RequestEntity> attachedRequestEntityList = new ArrayList<RequestEntity>();
            for (RequestEntity requestEntityListRequestEntityToAttach : projectEntity.getRequestEntityList()) {
                requestEntityListRequestEntityToAttach = em.getReference(requestEntityListRequestEntityToAttach.getClass(), requestEntityListRequestEntityToAttach.getProjectId());
                attachedRequestEntityList.add(requestEntityListRequestEntityToAttach);
            }
            projectEntity.setRequestEntityList(attachedRequestEntityList);
            List<ParameterEntity> attachedParameterEntityList = new ArrayList<ParameterEntity>();
            for (ParameterEntity parameterEntityListParameterEntityToAttach : projectEntity.getParameterEntityList()) {
                parameterEntityListParameterEntityToAttach = em.getReference(parameterEntityListParameterEntityToAttach.getClass(), parameterEntityListParameterEntityToAttach.getParameterId());
                attachedParameterEntityList.add(parameterEntityListParameterEntityToAttach);
            }
            projectEntity.setParameterEntityList(attachedParameterEntityList);
            em.persist(projectEntity);
            if (userName != null) {
                userName.getProjectEntityList().add(projectEntity);
                userName = em.merge(userName);
            }
            if (scriptId != null) {
                scriptId.getProjectEntityList().add(projectEntity);
                scriptId = em.merge(scriptId);
            }
            for (RequestEntity requestEntityListRequestEntity : projectEntity.getRequestEntityList()) {
                ProjectEntity oldProjectNameOfRequestEntityListRequestEntity = requestEntityListRequestEntity.getProjectName();
                requestEntityListRequestEntity.setProjectName(projectEntity);
                requestEntityListRequestEntity = em.merge(requestEntityListRequestEntity);
                if (oldProjectNameOfRequestEntityListRequestEntity != null) {
                    oldProjectNameOfRequestEntityListRequestEntity.getRequestEntityList().remove(requestEntityListRequestEntity);
                    oldProjectNameOfRequestEntityListRequestEntity = em.merge(oldProjectNameOfRequestEntityListRequestEntity);
                }
            }
            for (ParameterEntity parameterEntityListParameterEntity : projectEntity.getParameterEntityList()) {
                ProjectEntity oldProjectNameOfParameterEntityListParameterEntity = parameterEntityListParameterEntity.getProjectName();
                parameterEntityListParameterEntity.setProjectName(projectEntity);
                parameterEntityListParameterEntity = em.merge(parameterEntityListParameterEntity);
                if (oldProjectNameOfParameterEntityListParameterEntity != null) {
                    oldProjectNameOfParameterEntityListParameterEntity.getParameterEntityList().remove(parameterEntityListParameterEntity);
                    oldProjectNameOfParameterEntityListParameterEntity = em.merge(oldProjectNameOfParameterEntityListParameterEntity);
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
            List<RequestEntity> requestEntityListOld = persistentProjectEntity.getRequestEntityList();
            List<RequestEntity> requestEntityListNew = projectEntity.getRequestEntityList();
            List<ParameterEntity> parameterEntityListOld = persistentProjectEntity.getParameterEntityList();
            List<ParameterEntity> parameterEntityListNew = projectEntity.getParameterEntityList();
            List<String> illegalOrphanMessages = null;
            for (RequestEntity requestEntityListOldRequestEntity : requestEntityListOld) {
                if (!requestEntityListNew.contains(requestEntityListOldRequestEntity)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RequestEntity " + requestEntityListOldRequestEntity + " since its projectName field is not nullable.");
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
            List<RequestEntity> attachedRequestEntityListNew = new ArrayList<RequestEntity>();
            for (RequestEntity requestEntityListNewRequestEntityToAttach : requestEntityListNew) {
                requestEntityListNewRequestEntityToAttach = em.getReference(requestEntityListNewRequestEntityToAttach.getClass(), requestEntityListNewRequestEntityToAttach.getProjectId());
                attachedRequestEntityListNew.add(requestEntityListNewRequestEntityToAttach);
            }
            requestEntityListNew = attachedRequestEntityListNew;
            projectEntity.setRequestEntityList(requestEntityListNew);
            List<ParameterEntity> attachedParameterEntityListNew = new ArrayList<ParameterEntity>();
            for (ParameterEntity parameterEntityListNewParameterEntityToAttach : parameterEntityListNew) {
                parameterEntityListNewParameterEntityToAttach = em.getReference(parameterEntityListNewParameterEntityToAttach.getClass(), parameterEntityListNewParameterEntityToAttach.getParameterId());
                attachedParameterEntityListNew.add(parameterEntityListNewParameterEntityToAttach);
            }
            parameterEntityListNew = attachedParameterEntityListNew;
            projectEntity.setParameterEntityList(parameterEntityListNew);
            projectEntity = em.merge(projectEntity);
            if (userNameOld != null && !userNameOld.equals(userNameNew)) {
                userNameOld.getProjectEntityList().remove(projectEntity);
                userNameOld = em.merge(userNameOld);
            }
            if (userNameNew != null && !userNameNew.equals(userNameOld)) {
                userNameNew.getProjectEntityList().add(projectEntity);
                userNameNew = em.merge(userNameNew);
            }
            if (scriptIdOld != null && !scriptIdOld.equals(scriptIdNew)) {
                scriptIdOld.getProjectEntityList().remove(projectEntity);
                scriptIdOld = em.merge(scriptIdOld);
            }
            if (scriptIdNew != null && !scriptIdNew.equals(scriptIdOld)) {
                scriptIdNew.getProjectEntityList().add(projectEntity);
                scriptIdNew = em.merge(scriptIdNew);
            }
            for (RequestEntity requestEntityListNewRequestEntity : requestEntityListNew) {
                if (!requestEntityListOld.contains(requestEntityListNewRequestEntity)) {
                    ProjectEntity oldProjectNameOfRequestEntityListNewRequestEntity = requestEntityListNewRequestEntity.getProjectName();
                    requestEntityListNewRequestEntity.setProjectName(projectEntity);
                    requestEntityListNewRequestEntity = em.merge(requestEntityListNewRequestEntity);
                    if (oldProjectNameOfRequestEntityListNewRequestEntity != null && !oldProjectNameOfRequestEntityListNewRequestEntity.equals(projectEntity)) {
                        oldProjectNameOfRequestEntityListNewRequestEntity.getRequestEntityList().remove(requestEntityListNewRequestEntity);
                        oldProjectNameOfRequestEntityListNewRequestEntity = em.merge(oldProjectNameOfRequestEntityListNewRequestEntity);
                    }
                }
            }
            for (ParameterEntity parameterEntityListOldParameterEntity : parameterEntityListOld) {
                if (!parameterEntityListNew.contains(parameterEntityListOldParameterEntity)) {
                    parameterEntityListOldParameterEntity.setProjectName(null);
                    parameterEntityListOldParameterEntity = em.merge(parameterEntityListOldParameterEntity);
                }
            }
            for (ParameterEntity parameterEntityListNewParameterEntity : parameterEntityListNew) {
                if (!parameterEntityListOld.contains(parameterEntityListNewParameterEntity)) {
                    ProjectEntity oldProjectNameOfParameterEntityListNewParameterEntity = parameterEntityListNewParameterEntity.getProjectName();
                    parameterEntityListNewParameterEntity.setProjectName(projectEntity);
                    parameterEntityListNewParameterEntity = em.merge(parameterEntityListNewParameterEntity);
                    if (oldProjectNameOfParameterEntityListNewParameterEntity != null && !oldProjectNameOfParameterEntityListNewParameterEntity.equals(projectEntity)) {
                        oldProjectNameOfParameterEntityListNewParameterEntity.getParameterEntityList().remove(parameterEntityListNewParameterEntity);
                        oldProjectNameOfParameterEntityListNewParameterEntity = em.merge(oldProjectNameOfParameterEntityListNewParameterEntity);
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
            List<RequestEntity> requestEntityListOrphanCheck = projectEntity.getRequestEntityList();
            for (RequestEntity requestEntityListOrphanCheckRequestEntity : requestEntityListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ProjectEntity (" + projectEntity + ") cannot be destroyed since the RequestEntity " + requestEntityListOrphanCheckRequestEntity + " in its requestEntityList field has a non-nullable projectName field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            UserEntity userName = projectEntity.getUserName();
            if (userName != null) {
                userName.getProjectEntityList().remove(projectEntity);
                userName = em.merge(userName);
            }
            ScriptEntity scriptId = projectEntity.getScriptId();
            if (scriptId != null) {
                scriptId.getProjectEntityList().remove(projectEntity);
                scriptId = em.merge(scriptId);
            }
            List<ParameterEntity> parameterEntityList = projectEntity.getParameterEntityList();
            for (ParameterEntity parameterEntityListParameterEntity : parameterEntityList) {
                parameterEntityListParameterEntity.setProjectName(null);
                parameterEntityListParameterEntity = em.merge(parameterEntityListParameterEntity);
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
