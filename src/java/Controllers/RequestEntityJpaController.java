/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Controllers.exceptions.IllegalOrphanException;
import Controllers.exceptions.NonexistentEntityException;
import Controllers.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entities.ProjectEntity;
import Entities.ScriptEntity;
import Entities.ParameterEntity;
import Entities.RequestEntity;
import java.util.ArrayList;
import java.util.List;
import Entities.TestcaseEntity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Administrator
 */
public class RequestEntityJpaController implements Serializable {

    public RequestEntityJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RequestEntity requestEntity) throws RollbackFailureException, Exception {
        if (requestEntity.getParameterEntityList() == null) {
            requestEntity.setParameterEntityList(new ArrayList<ParameterEntity>());
        }
        if (requestEntity.getTestcaseEntityList() == null) {
            requestEntity.setTestcaseEntityList(new ArrayList<TestcaseEntity>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ProjectEntity projectName = requestEntity.getProjectName();
            if (projectName != null) {
                projectName = em.getReference(projectName.getClass(), projectName.getName());
                requestEntity.setProjectName(projectName);
            }
            ScriptEntity scriptId = requestEntity.getScriptId();
            if (scriptId != null) {
                scriptId = em.getReference(scriptId.getClass(), scriptId.getScriptId());
                requestEntity.setScriptId(scriptId);
            }
            List<ParameterEntity> attachedParameterEntityList = new ArrayList<ParameterEntity>();
            for (ParameterEntity parameterEntityListParameterEntityToAttach : requestEntity.getParameterEntityList()) {
                parameterEntityListParameterEntityToAttach = em.getReference(parameterEntityListParameterEntityToAttach.getClass(), parameterEntityListParameterEntityToAttach.getParameterId());
                attachedParameterEntityList.add(parameterEntityListParameterEntityToAttach);
            }
            requestEntity.setParameterEntityList(attachedParameterEntityList);
            List<TestcaseEntity> attachedTestcaseEntityList = new ArrayList<TestcaseEntity>();
            for (TestcaseEntity testcaseEntityListTestcaseEntityToAttach : requestEntity.getTestcaseEntityList()) {
                testcaseEntityListTestcaseEntityToAttach = em.getReference(testcaseEntityListTestcaseEntityToAttach.getClass(), testcaseEntityListTestcaseEntityToAttach.getTestcaseId());
                attachedTestcaseEntityList.add(testcaseEntityListTestcaseEntityToAttach);
            }
            requestEntity.setTestcaseEntityList(attachedTestcaseEntityList);
            em.persist(requestEntity);
            if (projectName != null) {
                projectName.getRequestEntityList().add(requestEntity);
                projectName = em.merge(projectName);
            }
            if (scriptId != null) {
                scriptId.getRequestEntityList().add(requestEntity);
                scriptId = em.merge(scriptId);
            }
            for (ParameterEntity parameterEntityListParameterEntity : requestEntity.getParameterEntityList()) {
                RequestEntity oldRequestIdOfParameterEntityListParameterEntity = parameterEntityListParameterEntity.getRequestId();
                parameterEntityListParameterEntity.setRequestId(requestEntity);
                parameterEntityListParameterEntity = em.merge(parameterEntityListParameterEntity);
                if (oldRequestIdOfParameterEntityListParameterEntity != null) {
                    oldRequestIdOfParameterEntityListParameterEntity.getParameterEntityList().remove(parameterEntityListParameterEntity);
                    oldRequestIdOfParameterEntityListParameterEntity = em.merge(oldRequestIdOfParameterEntityListParameterEntity);
                }
            }
            for (TestcaseEntity testcaseEntityListTestcaseEntity : requestEntity.getTestcaseEntityList()) {
                RequestEntity oldRequestIdOfTestcaseEntityListTestcaseEntity = testcaseEntityListTestcaseEntity.getRequestId();
                testcaseEntityListTestcaseEntity.setRequestId(requestEntity);
                testcaseEntityListTestcaseEntity = em.merge(testcaseEntityListTestcaseEntity);
                if (oldRequestIdOfTestcaseEntityListTestcaseEntity != null) {
                    oldRequestIdOfTestcaseEntityListTestcaseEntity.getTestcaseEntityList().remove(testcaseEntityListTestcaseEntity);
                    oldRequestIdOfTestcaseEntityListTestcaseEntity = em.merge(oldRequestIdOfTestcaseEntityListTestcaseEntity);
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

    public void edit(RequestEntity requestEntity) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            RequestEntity persistentRequestEntity = em.find(RequestEntity.class, requestEntity.getProjectId());
            ProjectEntity projectNameOld = persistentRequestEntity.getProjectName();
            ProjectEntity projectNameNew = requestEntity.getProjectName();
            ScriptEntity scriptIdOld = persistentRequestEntity.getScriptId();
            ScriptEntity scriptIdNew = requestEntity.getScriptId();
            List<ParameterEntity> parameterEntityListOld = persistentRequestEntity.getParameterEntityList();
            List<ParameterEntity> parameterEntityListNew = requestEntity.getParameterEntityList();
            List<TestcaseEntity> testcaseEntityListOld = persistentRequestEntity.getTestcaseEntityList();
            List<TestcaseEntity> testcaseEntityListNew = requestEntity.getTestcaseEntityList();
            List<String> illegalOrphanMessages = null;
            for (TestcaseEntity testcaseEntityListOldTestcaseEntity : testcaseEntityListOld) {
                if (!testcaseEntityListNew.contains(testcaseEntityListOldTestcaseEntity)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TestcaseEntity " + testcaseEntityListOldTestcaseEntity + " since its requestId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (projectNameNew != null) {
                projectNameNew = em.getReference(projectNameNew.getClass(), projectNameNew.getName());
                requestEntity.setProjectName(projectNameNew);
            }
            if (scriptIdNew != null) {
                scriptIdNew = em.getReference(scriptIdNew.getClass(), scriptIdNew.getScriptId());
                requestEntity.setScriptId(scriptIdNew);
            }
            List<ParameterEntity> attachedParameterEntityListNew = new ArrayList<ParameterEntity>();
            for (ParameterEntity parameterEntityListNewParameterEntityToAttach : parameterEntityListNew) {
                parameterEntityListNewParameterEntityToAttach = em.getReference(parameterEntityListNewParameterEntityToAttach.getClass(), parameterEntityListNewParameterEntityToAttach.getParameterId());
                attachedParameterEntityListNew.add(parameterEntityListNewParameterEntityToAttach);
            }
            parameterEntityListNew = attachedParameterEntityListNew;
            requestEntity.setParameterEntityList(parameterEntityListNew);
            List<TestcaseEntity> attachedTestcaseEntityListNew = new ArrayList<TestcaseEntity>();
            for (TestcaseEntity testcaseEntityListNewTestcaseEntityToAttach : testcaseEntityListNew) {
                testcaseEntityListNewTestcaseEntityToAttach = em.getReference(testcaseEntityListNewTestcaseEntityToAttach.getClass(), testcaseEntityListNewTestcaseEntityToAttach.getTestcaseId());
                attachedTestcaseEntityListNew.add(testcaseEntityListNewTestcaseEntityToAttach);
            }
            testcaseEntityListNew = attachedTestcaseEntityListNew;
            requestEntity.setTestcaseEntityList(testcaseEntityListNew);
            requestEntity = em.merge(requestEntity);
            if (projectNameOld != null && !projectNameOld.equals(projectNameNew)) {
                projectNameOld.getRequestEntityList().remove(requestEntity);
                projectNameOld = em.merge(projectNameOld);
            }
            if (projectNameNew != null && !projectNameNew.equals(projectNameOld)) {
                projectNameNew.getRequestEntityList().add(requestEntity);
                projectNameNew = em.merge(projectNameNew);
            }
            if (scriptIdOld != null && !scriptIdOld.equals(scriptIdNew)) {
                scriptIdOld.getRequestEntityList().remove(requestEntity);
                scriptIdOld = em.merge(scriptIdOld);
            }
            if (scriptIdNew != null && !scriptIdNew.equals(scriptIdOld)) {
                scriptIdNew.getRequestEntityList().add(requestEntity);
                scriptIdNew = em.merge(scriptIdNew);
            }
            for (ParameterEntity parameterEntityListOldParameterEntity : parameterEntityListOld) {
                if (!parameterEntityListNew.contains(parameterEntityListOldParameterEntity)) {
                    parameterEntityListOldParameterEntity.setRequestId(null);
                    parameterEntityListOldParameterEntity = em.merge(parameterEntityListOldParameterEntity);
                }
            }
            for (ParameterEntity parameterEntityListNewParameterEntity : parameterEntityListNew) {
                if (!parameterEntityListOld.contains(parameterEntityListNewParameterEntity)) {
                    RequestEntity oldRequestIdOfParameterEntityListNewParameterEntity = parameterEntityListNewParameterEntity.getRequestId();
                    parameterEntityListNewParameterEntity.setRequestId(requestEntity);
                    parameterEntityListNewParameterEntity = em.merge(parameterEntityListNewParameterEntity);
                    if (oldRequestIdOfParameterEntityListNewParameterEntity != null && !oldRequestIdOfParameterEntityListNewParameterEntity.equals(requestEntity)) {
                        oldRequestIdOfParameterEntityListNewParameterEntity.getParameterEntityList().remove(parameterEntityListNewParameterEntity);
                        oldRequestIdOfParameterEntityListNewParameterEntity = em.merge(oldRequestIdOfParameterEntityListNewParameterEntity);
                    }
                }
            }
            for (TestcaseEntity testcaseEntityListNewTestcaseEntity : testcaseEntityListNew) {
                if (!testcaseEntityListOld.contains(testcaseEntityListNewTestcaseEntity)) {
                    RequestEntity oldRequestIdOfTestcaseEntityListNewTestcaseEntity = testcaseEntityListNewTestcaseEntity.getRequestId();
                    testcaseEntityListNewTestcaseEntity.setRequestId(requestEntity);
                    testcaseEntityListNewTestcaseEntity = em.merge(testcaseEntityListNewTestcaseEntity);
                    if (oldRequestIdOfTestcaseEntityListNewTestcaseEntity != null && !oldRequestIdOfTestcaseEntityListNewTestcaseEntity.equals(requestEntity)) {
                        oldRequestIdOfTestcaseEntityListNewTestcaseEntity.getTestcaseEntityList().remove(testcaseEntityListNewTestcaseEntity);
                        oldRequestIdOfTestcaseEntityListNewTestcaseEntity = em.merge(oldRequestIdOfTestcaseEntityListNewTestcaseEntity);
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
                Integer id = requestEntity.getProjectId();
                if (findRequestEntity(id) == null) {
                    throw new NonexistentEntityException("The requestEntity with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            RequestEntity requestEntity;
            try {
                requestEntity = em.getReference(RequestEntity.class, id);
                requestEntity.getProjectId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The requestEntity with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<TestcaseEntity> testcaseEntityListOrphanCheck = requestEntity.getTestcaseEntityList();
            for (TestcaseEntity testcaseEntityListOrphanCheckTestcaseEntity : testcaseEntityListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This RequestEntity (" + requestEntity + ") cannot be destroyed since the TestcaseEntity " + testcaseEntityListOrphanCheckTestcaseEntity + " in its testcaseEntityList field has a non-nullable requestId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            ProjectEntity projectName = requestEntity.getProjectName();
            if (projectName != null) {
                projectName.getRequestEntityList().remove(requestEntity);
                projectName = em.merge(projectName);
            }
            ScriptEntity scriptId = requestEntity.getScriptId();
            if (scriptId != null) {
                scriptId.getRequestEntityList().remove(requestEntity);
                scriptId = em.merge(scriptId);
            }
            List<ParameterEntity> parameterEntityList = requestEntity.getParameterEntityList();
            for (ParameterEntity parameterEntityListParameterEntity : parameterEntityList) {
                parameterEntityListParameterEntity.setRequestId(null);
                parameterEntityListParameterEntity = em.merge(parameterEntityListParameterEntity);
            }
            em.remove(requestEntity);
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

    public List<RequestEntity> findRequestEntityEntities() {
        return findRequestEntityEntities(true, -1, -1);
    }

    public List<RequestEntity> findRequestEntityEntities(int maxResults, int firstResult) {
        return findRequestEntityEntities(false, maxResults, firstResult);
    }

    private List<RequestEntity> findRequestEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RequestEntity.class));
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

    public RequestEntity findRequestEntity(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RequestEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getRequestEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RequestEntity> rt = cq.from(RequestEntity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
