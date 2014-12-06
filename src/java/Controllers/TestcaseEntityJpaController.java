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
import Entities.RequestEntity;
import Entities.TestcaseEntity;
import Entities.TestrunEntity;
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
public class TestcaseEntityJpaController implements Serializable {

    public TestcaseEntityJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TestcaseEntity testcaseEntity) throws RollbackFailureException, Exception {
        if (testcaseEntity.getTestrunEntityCollection() == null) {
            testcaseEntity.setTestrunEntityCollection(new ArrayList<TestrunEntity>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ScriptEntity scriptId = testcaseEntity.getScriptId();
            if (scriptId != null) {
                scriptId = em.getReference(scriptId.getClass(), scriptId.getScriptId());
                testcaseEntity.setScriptId(scriptId);
            }
            RequestEntity requestId = testcaseEntity.getRequestId();
            if (requestId != null) {
                requestId = em.getReference(requestId.getClass(), requestId.getProjectId());
                testcaseEntity.setRequestId(requestId);
            }
            Collection<TestrunEntity> attachedTestrunEntityCollection = new ArrayList<TestrunEntity>();
            for (TestrunEntity testrunEntityCollectionTestrunEntityToAttach : testcaseEntity.getTestrunEntityCollection()) {
                testrunEntityCollectionTestrunEntityToAttach = em.getReference(testrunEntityCollectionTestrunEntityToAttach.getClass(), testrunEntityCollectionTestrunEntityToAttach.getTestrunId());
                attachedTestrunEntityCollection.add(testrunEntityCollectionTestrunEntityToAttach);
            }
            testcaseEntity.setTestrunEntityCollection(attachedTestrunEntityCollection);
            em.persist(testcaseEntity);
            if (scriptId != null) {
                scriptId.getTestcaseCollection().add(testcaseEntity);
                scriptId = em.merge(scriptId);
            }
            if (requestId != null) {
                requestId.getTestcaseEntityCollection().add(testcaseEntity);
                requestId = em.merge(requestId);
            }
            for (TestrunEntity testrunEntityCollectionTestrunEntity : testcaseEntity.getTestrunEntityCollection()) {
                TestcaseEntity oldTestcaseIdOfTestrunEntityCollectionTestrunEntity = testrunEntityCollectionTestrunEntity.getTestcaseId();
                testrunEntityCollectionTestrunEntity.setTestcaseId(testcaseEntity);
                testrunEntityCollectionTestrunEntity = em.merge(testrunEntityCollectionTestrunEntity);
                if (oldTestcaseIdOfTestrunEntityCollectionTestrunEntity != null) {
                    oldTestcaseIdOfTestrunEntityCollectionTestrunEntity.getTestrunEntityCollection().remove(testrunEntityCollectionTestrunEntity);
                    oldTestcaseIdOfTestrunEntityCollectionTestrunEntity = em.merge(oldTestcaseIdOfTestrunEntityCollectionTestrunEntity);
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

    public void edit(TestcaseEntity testcaseEntity) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TestcaseEntity persistentTestcaseEntity = em.find(TestcaseEntity.class, testcaseEntity.getTestcaseId());
            ScriptEntity scriptIdOld = persistentTestcaseEntity.getScriptId();
            ScriptEntity scriptIdNew = testcaseEntity.getScriptId();
            RequestEntity requestIdOld = persistentTestcaseEntity.getRequestId();
            RequestEntity requestIdNew = testcaseEntity.getRequestId();
            Collection<TestrunEntity> testrunEntityCollectionOld = persistentTestcaseEntity.getTestrunEntityCollection();
            Collection<TestrunEntity> testrunEntityCollectionNew = testcaseEntity.getTestrunEntityCollection();
            List<String> illegalOrphanMessages = null;
            for (TestrunEntity testrunEntityCollectionOldTestrunEntity : testrunEntityCollectionOld) {
                if (!testrunEntityCollectionNew.contains(testrunEntityCollectionOldTestrunEntity)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TestrunEntity " + testrunEntityCollectionOldTestrunEntity + " since its testcaseId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (scriptIdNew != null) {
                scriptIdNew = em.getReference(scriptIdNew.getClass(), scriptIdNew.getScriptId());
                testcaseEntity.setScriptId(scriptIdNew);
            }
            if (requestIdNew != null) {
                requestIdNew = em.getReference(requestIdNew.getClass(), requestIdNew.getProjectId());
                testcaseEntity.setRequestId(requestIdNew);
            }
            Collection<TestrunEntity> attachedTestrunEntityCollectionNew = new ArrayList<TestrunEntity>();
            for (TestrunEntity testrunEntityCollectionNewTestrunEntityToAttach : testrunEntityCollectionNew) {
                testrunEntityCollectionNewTestrunEntityToAttach = em.getReference(testrunEntityCollectionNewTestrunEntityToAttach.getClass(), testrunEntityCollectionNewTestrunEntityToAttach.getTestrunId());
                attachedTestrunEntityCollectionNew.add(testrunEntityCollectionNewTestrunEntityToAttach);
            }
            testrunEntityCollectionNew = attachedTestrunEntityCollectionNew;
            testcaseEntity.setTestrunEntityCollection(testrunEntityCollectionNew);
            testcaseEntity = em.merge(testcaseEntity);
            if (scriptIdOld != null && !scriptIdOld.equals(scriptIdNew)) {
                scriptIdOld.getTestcaseCollection().remove(testcaseEntity);
                scriptIdOld = em.merge(scriptIdOld);
            }
            if (scriptIdNew != null && !scriptIdNew.equals(scriptIdOld)) {
                scriptIdNew.getTestcaseCollection().add(testcaseEntity);
                scriptIdNew = em.merge(scriptIdNew);
            }
            if (requestIdOld != null && !requestIdOld.equals(requestIdNew)) {
                requestIdOld.getTestcaseEntityCollection().remove(testcaseEntity);
                requestIdOld = em.merge(requestIdOld);
            }
            if (requestIdNew != null && !requestIdNew.equals(requestIdOld)) {
                requestIdNew.getTestcaseEntityCollection().add(testcaseEntity);
                requestIdNew = em.merge(requestIdNew);
            }
            for (TestrunEntity testrunEntityCollectionNewTestrunEntity : testrunEntityCollectionNew) {
                if (!testrunEntityCollectionOld.contains(testrunEntityCollectionNewTestrunEntity)) {
                    TestcaseEntity oldTestcaseIdOfTestrunEntityCollectionNewTestrunEntity = testrunEntityCollectionNewTestrunEntity.getTestcaseId();
                    testrunEntityCollectionNewTestrunEntity.setTestcaseId(testcaseEntity);
                    testrunEntityCollectionNewTestrunEntity = em.merge(testrunEntityCollectionNewTestrunEntity);
                    if (oldTestcaseIdOfTestrunEntityCollectionNewTestrunEntity != null && !oldTestcaseIdOfTestrunEntityCollectionNewTestrunEntity.equals(testcaseEntity)) {
                        oldTestcaseIdOfTestrunEntityCollectionNewTestrunEntity.getTestrunEntityCollection().remove(testrunEntityCollectionNewTestrunEntity);
                        oldTestcaseIdOfTestrunEntityCollectionNewTestrunEntity = em.merge(oldTestcaseIdOfTestrunEntityCollectionNewTestrunEntity);
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
                Integer id = testcaseEntity.getTestcaseId();
                if (findTestcaseEntity(id) == null) {
                    throw new NonexistentEntityException("The testcaseEntity with id " + id + " no longer exists.");
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
            TestcaseEntity testcaseEntity;
            try {
                testcaseEntity = em.getReference(TestcaseEntity.class, id);
                testcaseEntity.getTestcaseId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The testcaseEntity with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<TestrunEntity> testrunEntityCollectionOrphanCheck = testcaseEntity.getTestrunEntityCollection();
            for (TestrunEntity testrunEntityCollectionOrphanCheckTestrunEntity : testrunEntityCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TestcaseEntity (" + testcaseEntity + ") cannot be destroyed since the TestrunEntity " + testrunEntityCollectionOrphanCheckTestrunEntity + " in its testrunEntityCollection field has a non-nullable testcaseId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            ScriptEntity scriptId = testcaseEntity.getScriptId();
            if (scriptId != null) {
                scriptId.getTestcaseCollection().remove(testcaseEntity);
                scriptId = em.merge(scriptId);
            }
            RequestEntity requestId = testcaseEntity.getRequestId();
            if (requestId != null) {
                requestId.getTestcaseEntityCollection().remove(testcaseEntity);
                requestId = em.merge(requestId);
            }
            em.remove(testcaseEntity);
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

    public List<TestcaseEntity> findTestcaseEntityEntities() {
        return findTestcaseEntityEntities(true, -1, -1);
    }

    public List<TestcaseEntity> findTestcaseEntityEntities(int maxResults, int firstResult) {
        return findTestcaseEntityEntities(false, maxResults, firstResult);
    }

    private List<TestcaseEntity> findTestcaseEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TestcaseEntity.class));
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

    public TestcaseEntity findTestcaseEntity(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TestcaseEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getTestcaseEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TestcaseEntity> rt = cq.from(TestcaseEntity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
