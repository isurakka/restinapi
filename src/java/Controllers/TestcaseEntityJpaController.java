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
import Entities.RequestEntity;
import Entities.ScriptEntity;
import Entities.TestcaseEntity;
import Entities.TestrunEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Administrator
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
        if (testcaseEntity.getTestrunEntityList() == null) {
            testcaseEntity.setTestrunEntityList(new ArrayList<TestrunEntity>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            RequestEntity requestId = testcaseEntity.getRequestId();
            if (requestId != null) {
                requestId = em.getReference(requestId.getClass(), requestId.getRequestId());
                testcaseEntity.setRequestId(requestId);
            }
            ScriptEntity scriptId = testcaseEntity.getScriptId();
            if (scriptId != null) {
                scriptId = em.getReference(scriptId.getClass(), scriptId.getScriptId());
                testcaseEntity.setScriptId(scriptId);
            }
            List<TestrunEntity> attachedTestrunEntityList = new ArrayList<TestrunEntity>();
            for (TestrunEntity testrunEntityListTestrunEntityToAttach : testcaseEntity.getTestrunEntityList()) {
                testrunEntityListTestrunEntityToAttach = em.getReference(testrunEntityListTestrunEntityToAttach.getClass(), testrunEntityListTestrunEntityToAttach.getTestrunId());
                attachedTestrunEntityList.add(testrunEntityListTestrunEntityToAttach);
            }
            testcaseEntity.setTestrunEntityList(attachedTestrunEntityList);
            em.persist(testcaseEntity);
            if (requestId != null) {
                requestId.getTestcaseEntityList().add(testcaseEntity);
                requestId = em.merge(requestId);
            }
            if (scriptId != null) {
                scriptId.getTestcaseEntityList().add(testcaseEntity);
                scriptId = em.merge(scriptId);
            }
            for (TestrunEntity testrunEntityListTestrunEntity : testcaseEntity.getTestrunEntityList()) {
                TestcaseEntity oldTestcaseIdOfTestrunEntityListTestrunEntity = testrunEntityListTestrunEntity.getTestcaseId();
                testrunEntityListTestrunEntity.setTestcaseId(testcaseEntity);
                testrunEntityListTestrunEntity = em.merge(testrunEntityListTestrunEntity);
                if (oldTestcaseIdOfTestrunEntityListTestrunEntity != null) {
                    oldTestcaseIdOfTestrunEntityListTestrunEntity.getTestrunEntityList().remove(testrunEntityListTestrunEntity);
                    oldTestcaseIdOfTestrunEntityListTestrunEntity = em.merge(oldTestcaseIdOfTestrunEntityListTestrunEntity);
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
            RequestEntity requestIdOld = persistentTestcaseEntity.getRequestId();
            RequestEntity requestIdNew = testcaseEntity.getRequestId();
            ScriptEntity scriptIdOld = persistentTestcaseEntity.getScriptId();
            ScriptEntity scriptIdNew = testcaseEntity.getScriptId();
            List<TestrunEntity> testrunEntityListOld = persistentTestcaseEntity.getTestrunEntityList();
            List<TestrunEntity> testrunEntityListNew = testcaseEntity.getTestrunEntityList();
            List<String> illegalOrphanMessages = null;
            for (TestrunEntity testrunEntityListOldTestrunEntity : testrunEntityListOld) {
                if (!testrunEntityListNew.contains(testrunEntityListOldTestrunEntity)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TestrunEntity " + testrunEntityListOldTestrunEntity + " since its testcaseId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (requestIdNew != null) {
                requestIdNew = em.getReference(requestIdNew.getClass(), requestIdNew.getRequestId());
                testcaseEntity.setRequestId(requestIdNew);
            }
            if (scriptIdNew != null) {
                scriptIdNew = em.getReference(scriptIdNew.getClass(), scriptIdNew.getScriptId());
                testcaseEntity.setScriptId(scriptIdNew);
            }
            List<TestrunEntity> attachedTestrunEntityListNew = new ArrayList<TestrunEntity>();
            for (TestrunEntity testrunEntityListNewTestrunEntityToAttach : testrunEntityListNew) {
                testrunEntityListNewTestrunEntityToAttach = em.getReference(testrunEntityListNewTestrunEntityToAttach.getClass(), testrunEntityListNewTestrunEntityToAttach.getTestrunId());
                attachedTestrunEntityListNew.add(testrunEntityListNewTestrunEntityToAttach);
            }
            testrunEntityListNew = attachedTestrunEntityListNew;
            testcaseEntity.setTestrunEntityList(testrunEntityListNew);
            testcaseEntity = em.merge(testcaseEntity);
            if (requestIdOld != null && !requestIdOld.equals(requestIdNew)) {
                requestIdOld.getTestcaseEntityList().remove(testcaseEntity);
                requestIdOld = em.merge(requestIdOld);
            }
            if (requestIdNew != null && !requestIdNew.equals(requestIdOld)) {
                requestIdNew.getTestcaseEntityList().add(testcaseEntity);
                requestIdNew = em.merge(requestIdNew);
            }
            if (scriptIdOld != null && !scriptIdOld.equals(scriptIdNew)) {
                scriptIdOld.getTestcaseEntityList().remove(testcaseEntity);
                scriptIdOld = em.merge(scriptIdOld);
            }
            if (scriptIdNew != null && !scriptIdNew.equals(scriptIdOld)) {
                scriptIdNew.getTestcaseEntityList().add(testcaseEntity);
                scriptIdNew = em.merge(scriptIdNew);
            }
            for (TestrunEntity testrunEntityListNewTestrunEntity : testrunEntityListNew) {
                if (!testrunEntityListOld.contains(testrunEntityListNewTestrunEntity)) {
                    TestcaseEntity oldTestcaseIdOfTestrunEntityListNewTestrunEntity = testrunEntityListNewTestrunEntity.getTestcaseId();
                    testrunEntityListNewTestrunEntity.setTestcaseId(testcaseEntity);
                    testrunEntityListNewTestrunEntity = em.merge(testrunEntityListNewTestrunEntity);
                    if (oldTestcaseIdOfTestrunEntityListNewTestrunEntity != null && !oldTestcaseIdOfTestrunEntityListNewTestrunEntity.equals(testcaseEntity)) {
                        oldTestcaseIdOfTestrunEntityListNewTestrunEntity.getTestrunEntityList().remove(testrunEntityListNewTestrunEntity);
                        oldTestcaseIdOfTestrunEntityListNewTestrunEntity = em.merge(oldTestcaseIdOfTestrunEntityListNewTestrunEntity);
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
            List<TestrunEntity> testrunEntityListOrphanCheck = testcaseEntity.getTestrunEntityList();
            for (TestrunEntity testrunEntityListOrphanCheckTestrunEntity : testrunEntityListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TestcaseEntity (" + testcaseEntity + ") cannot be destroyed since the TestrunEntity " + testrunEntityListOrphanCheckTestrunEntity + " in its testrunEntityList field has a non-nullable testcaseId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            RequestEntity requestId = testcaseEntity.getRequestId();
            if (requestId != null) {
                requestId.getTestcaseEntityList().remove(testcaseEntity);
                requestId = em.merge(requestId);
            }
            ScriptEntity scriptId = testcaseEntity.getScriptId();
            if (scriptId != null) {
                scriptId.getTestcaseEntityList().remove(testcaseEntity);
                scriptId = em.merge(scriptId);
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
