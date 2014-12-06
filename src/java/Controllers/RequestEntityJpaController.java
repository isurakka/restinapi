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
import Entities.ScriptEntity;
import Entities.ProjectEntity;
import Entities.ParameterEntity;
import Entities.RequestEntity;
import java.util.ArrayList;
import java.util.Collection;
import Entities.TestcaseEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Matti
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
        if (requestEntity.getParameterEntityCollection() == null) {
            requestEntity.setParameterEntityCollection(new ArrayList<ParameterEntity>());
        }
        if (requestEntity.getTestcaseEntityCollection() == null) {
            requestEntity.setTestcaseEntityCollection(new ArrayList<TestcaseEntity>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ScriptEntity scriptId = requestEntity.getScriptId();
            if (scriptId != null) {
                scriptId = em.getReference(scriptId.getClass(), scriptId.getScriptId());
                requestEntity.setScriptId(scriptId);
            }
            ProjectEntity projectName = requestEntity.getProjectName();
            if (projectName != null) {
                projectName = em.getReference(projectName.getClass(), projectName.getName());
                requestEntity.setProjectName(projectName);
            }
            Collection<ParameterEntity> attachedParameterEntityCollection = new ArrayList<ParameterEntity>();
            for (ParameterEntity parameterEntityCollectionParameterEntityToAttach : requestEntity.getParameterEntityCollection()) {
                parameterEntityCollectionParameterEntityToAttach = em.getReference(parameterEntityCollectionParameterEntityToAttach.getClass(), parameterEntityCollectionParameterEntityToAttach.getParameterId());
                attachedParameterEntityCollection.add(parameterEntityCollectionParameterEntityToAttach);
            }
            requestEntity.setParameterEntityCollection(attachedParameterEntityCollection);
            Collection<TestcaseEntity> attachedTestcaseEntityCollection = new ArrayList<TestcaseEntity>();
            for (TestcaseEntity testcaseEntityCollectionTestcaseEntityToAttach : requestEntity.getTestcaseEntityCollection()) {
                testcaseEntityCollectionTestcaseEntityToAttach = em.getReference(testcaseEntityCollectionTestcaseEntityToAttach.getClass(), testcaseEntityCollectionTestcaseEntityToAttach.getTestcaseId());
                attachedTestcaseEntityCollection.add(testcaseEntityCollectionTestcaseEntityToAttach);
            }
            requestEntity.setTestcaseEntityCollection(attachedTestcaseEntityCollection);
            em.persist(requestEntity);
            if (scriptId != null) {
                scriptId.getRequestCollection().add(requestEntity);
                scriptId = em.merge(scriptId);
            }
            if (projectName != null) {
                projectName.getRequestCollection().add(requestEntity);
                projectName = em.merge(projectName);
            }
            for (ParameterEntity parameterEntityCollectionParameterEntity : requestEntity.getParameterEntityCollection()) {
                RequestEntity oldRequestIdOfParameterEntityCollectionParameterEntity = parameterEntityCollectionParameterEntity.getRequestId();
                parameterEntityCollectionParameterEntity.setRequestId(requestEntity);
                parameterEntityCollectionParameterEntity = em.merge(parameterEntityCollectionParameterEntity);
                if (oldRequestIdOfParameterEntityCollectionParameterEntity != null) {
                    oldRequestIdOfParameterEntityCollectionParameterEntity.getParameterEntityCollection().remove(parameterEntityCollectionParameterEntity);
                    oldRequestIdOfParameterEntityCollectionParameterEntity = em.merge(oldRequestIdOfParameterEntityCollectionParameterEntity);
                }
            }
            for (TestcaseEntity testcaseEntityCollectionTestcaseEntity : requestEntity.getTestcaseEntityCollection()) {
                RequestEntity oldRequestIdOfTestcaseEntityCollectionTestcaseEntity = testcaseEntityCollectionTestcaseEntity.getRequestId();
                testcaseEntityCollectionTestcaseEntity.setRequestId(requestEntity);
                testcaseEntityCollectionTestcaseEntity = em.merge(testcaseEntityCollectionTestcaseEntity);
                if (oldRequestIdOfTestcaseEntityCollectionTestcaseEntity != null) {
                    oldRequestIdOfTestcaseEntityCollectionTestcaseEntity.getTestcaseEntityCollection().remove(testcaseEntityCollectionTestcaseEntity);
                    oldRequestIdOfTestcaseEntityCollectionTestcaseEntity = em.merge(oldRequestIdOfTestcaseEntityCollectionTestcaseEntity);
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
            ScriptEntity scriptIdOld = persistentRequestEntity.getScriptId();
            ScriptEntity scriptIdNew = requestEntity.getScriptId();
            ProjectEntity projectNameOld = persistentRequestEntity.getProjectName();
            ProjectEntity projectNameNew = requestEntity.getProjectName();
            Collection<ParameterEntity> parameterEntityCollectionOld = persistentRequestEntity.getParameterEntityCollection();
            Collection<ParameterEntity> parameterEntityCollectionNew = requestEntity.getParameterEntityCollection();
            Collection<TestcaseEntity> testcaseEntityCollectionOld = persistentRequestEntity.getTestcaseEntityCollection();
            Collection<TestcaseEntity> testcaseEntityCollectionNew = requestEntity.getTestcaseEntityCollection();
            List<String> illegalOrphanMessages = null;
            for (TestcaseEntity testcaseEntityCollectionOldTestcaseEntity : testcaseEntityCollectionOld) {
                if (!testcaseEntityCollectionNew.contains(testcaseEntityCollectionOldTestcaseEntity)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TestcaseEntity " + testcaseEntityCollectionOldTestcaseEntity + " since its requestId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (scriptIdNew != null) {
                scriptIdNew = em.getReference(scriptIdNew.getClass(), scriptIdNew.getScriptId());
                requestEntity.setScriptId(scriptIdNew);
            }
            if (projectNameNew != null) {
                projectNameNew = em.getReference(projectNameNew.getClass(), projectNameNew.getName());
                requestEntity.setProjectName(projectNameNew);
            }
            Collection<ParameterEntity> attachedParameterEntityCollectionNew = new ArrayList<ParameterEntity>();
            for (ParameterEntity parameterEntityCollectionNewParameterEntityToAttach : parameterEntityCollectionNew) {
                parameterEntityCollectionNewParameterEntityToAttach = em.getReference(parameterEntityCollectionNewParameterEntityToAttach.getClass(), parameterEntityCollectionNewParameterEntityToAttach.getParameterId());
                attachedParameterEntityCollectionNew.add(parameterEntityCollectionNewParameterEntityToAttach);
            }
            parameterEntityCollectionNew = attachedParameterEntityCollectionNew;
            requestEntity.setParameterEntityCollection(parameterEntityCollectionNew);
            Collection<TestcaseEntity> attachedTestcaseEntityCollectionNew = new ArrayList<TestcaseEntity>();
            for (TestcaseEntity testcaseEntityCollectionNewTestcaseEntityToAttach : testcaseEntityCollectionNew) {
                testcaseEntityCollectionNewTestcaseEntityToAttach = em.getReference(testcaseEntityCollectionNewTestcaseEntityToAttach.getClass(), testcaseEntityCollectionNewTestcaseEntityToAttach.getTestcaseId());
                attachedTestcaseEntityCollectionNew.add(testcaseEntityCollectionNewTestcaseEntityToAttach);
            }
            testcaseEntityCollectionNew = attachedTestcaseEntityCollectionNew;
            requestEntity.setTestcaseEntityCollection(testcaseEntityCollectionNew);
            requestEntity = em.merge(requestEntity);
            if (scriptIdOld != null && !scriptIdOld.equals(scriptIdNew)) {
                scriptIdOld.getRequestCollection().remove(requestEntity);
                scriptIdOld = em.merge(scriptIdOld);
            }
            if (scriptIdNew != null && !scriptIdNew.equals(scriptIdOld)) {
                scriptIdNew.getRequestCollection().add(requestEntity);
                scriptIdNew = em.merge(scriptIdNew);
            }
            if (projectNameOld != null && !projectNameOld.equals(projectNameNew)) {
                projectNameOld.getRequestCollection().remove(requestEntity);
                projectNameOld = em.merge(projectNameOld);
            }
            if (projectNameNew != null && !projectNameNew.equals(projectNameOld)) {
                projectNameNew.getRequestCollection().add(requestEntity);
                projectNameNew = em.merge(projectNameNew);
            }
            for (ParameterEntity parameterEntityCollectionOldParameterEntity : parameterEntityCollectionOld) {
                if (!parameterEntityCollectionNew.contains(parameterEntityCollectionOldParameterEntity)) {
                    parameterEntityCollectionOldParameterEntity.setRequestId(null);
                    parameterEntityCollectionOldParameterEntity = em.merge(parameterEntityCollectionOldParameterEntity);
                }
            }
            for (ParameterEntity parameterEntityCollectionNewParameterEntity : parameterEntityCollectionNew) {
                if (!parameterEntityCollectionOld.contains(parameterEntityCollectionNewParameterEntity)) {
                    RequestEntity oldRequestIdOfParameterEntityCollectionNewParameterEntity = parameterEntityCollectionNewParameterEntity.getRequestId();
                    parameterEntityCollectionNewParameterEntity.setRequestId(requestEntity);
                    parameterEntityCollectionNewParameterEntity = em.merge(parameterEntityCollectionNewParameterEntity);
                    if (oldRequestIdOfParameterEntityCollectionNewParameterEntity != null && !oldRequestIdOfParameterEntityCollectionNewParameterEntity.equals(requestEntity)) {
                        oldRequestIdOfParameterEntityCollectionNewParameterEntity.getParameterEntityCollection().remove(parameterEntityCollectionNewParameterEntity);
                        oldRequestIdOfParameterEntityCollectionNewParameterEntity = em.merge(oldRequestIdOfParameterEntityCollectionNewParameterEntity);
                    }
                }
            }
            for (TestcaseEntity testcaseEntityCollectionNewTestcaseEntity : testcaseEntityCollectionNew) {
                if (!testcaseEntityCollectionOld.contains(testcaseEntityCollectionNewTestcaseEntity)) {
                    RequestEntity oldRequestIdOfTestcaseEntityCollectionNewTestcaseEntity = testcaseEntityCollectionNewTestcaseEntity.getRequestId();
                    testcaseEntityCollectionNewTestcaseEntity.setRequestId(requestEntity);
                    testcaseEntityCollectionNewTestcaseEntity = em.merge(testcaseEntityCollectionNewTestcaseEntity);
                    if (oldRequestIdOfTestcaseEntityCollectionNewTestcaseEntity != null && !oldRequestIdOfTestcaseEntityCollectionNewTestcaseEntity.equals(requestEntity)) {
                        oldRequestIdOfTestcaseEntityCollectionNewTestcaseEntity.getTestcaseEntityCollection().remove(testcaseEntityCollectionNewTestcaseEntity);
                        oldRequestIdOfTestcaseEntityCollectionNewTestcaseEntity = em.merge(oldRequestIdOfTestcaseEntityCollectionNewTestcaseEntity);
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
            Collection<TestcaseEntity> testcaseEntityCollectionOrphanCheck = requestEntity.getTestcaseEntityCollection();
            for (TestcaseEntity testcaseEntityCollectionOrphanCheckTestcaseEntity : testcaseEntityCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This RequestEntity (" + requestEntity + ") cannot be destroyed since the TestcaseEntity " + testcaseEntityCollectionOrphanCheckTestcaseEntity + " in its testcaseEntityCollection field has a non-nullable requestId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            ScriptEntity scriptId = requestEntity.getScriptId();
            if (scriptId != null) {
                scriptId.getRequestCollection().remove(requestEntity);
                scriptId = em.merge(scriptId);
            }
            ProjectEntity projectName = requestEntity.getProjectName();
            if (projectName != null) {
                projectName.getRequestCollection().remove(requestEntity);
                projectName = em.merge(projectName);
            }
            Collection<ParameterEntity> parameterEntityCollection = requestEntity.getParameterEntityCollection();
            for (ParameterEntity parameterEntityCollectionParameterEntity : parameterEntityCollection) {
                parameterEntityCollectionParameterEntity.setRequestId(null);
                parameterEntityCollectionParameterEntity = em.merge(parameterEntityCollectionParameterEntity);
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
