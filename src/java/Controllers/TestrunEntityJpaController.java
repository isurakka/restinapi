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
import Entities.TestcaseEntity;
import Entities.TestrunEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Matti
 */
public class TestrunEntityJpaController implements Serializable {

    public TestrunEntityJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TestrunEntity testrunEntity) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TestcaseEntity testcaseId = testrunEntity.getTestcaseId();
            if (testcaseId != null) {
                testcaseId = em.getReference(testcaseId.getClass(), testcaseId.getTestcaseId());
                testrunEntity.setTestcaseId(testcaseId);
            }
            em.persist(testrunEntity);
            if (testcaseId != null) {
                testcaseId.getTestrunEntityCollection().add(testrunEntity);
                testcaseId = em.merge(testcaseId);
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

    public void edit(TestrunEntity testrunEntity) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TestrunEntity persistentTestrunEntity = em.find(TestrunEntity.class, testrunEntity.getTestrunId());
            TestcaseEntity testcaseIdOld = persistentTestrunEntity.getTestcaseId();
            TestcaseEntity testcaseIdNew = testrunEntity.getTestcaseId();
            if (testcaseIdNew != null) {
                testcaseIdNew = em.getReference(testcaseIdNew.getClass(), testcaseIdNew.getTestcaseId());
                testrunEntity.setTestcaseId(testcaseIdNew);
            }
            testrunEntity = em.merge(testrunEntity);
            if (testcaseIdOld != null && !testcaseIdOld.equals(testcaseIdNew)) {
                testcaseIdOld.getTestrunEntityCollection().remove(testrunEntity);
                testcaseIdOld = em.merge(testcaseIdOld);
            }
            if (testcaseIdNew != null && !testcaseIdNew.equals(testcaseIdOld)) {
                testcaseIdNew.getTestrunEntityCollection().add(testrunEntity);
                testcaseIdNew = em.merge(testcaseIdNew);
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
                Integer id = testrunEntity.getTestrunId();
                if (findTestrunEntity(id) == null) {
                    throw new NonexistentEntityException("The testrunEntity with id " + id + " no longer exists.");
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
            TestrunEntity testrunEntity;
            try {
                testrunEntity = em.getReference(TestrunEntity.class, id);
                testrunEntity.getTestrunId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The testrunEntity with id " + id + " no longer exists.", enfe);
            }
            TestcaseEntity testcaseId = testrunEntity.getTestcaseId();
            if (testcaseId != null) {
                testcaseId.getTestrunEntityCollection().remove(testrunEntity);
                testcaseId = em.merge(testcaseId);
            }
            em.remove(testrunEntity);
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

    public List<TestrunEntity> findTestrunEntityEntities() {
        return findTestrunEntityEntities(true, -1, -1);
    }

    public List<TestrunEntity> findTestrunEntityEntities(int maxResults, int firstResult) {
        return findTestrunEntityEntities(false, maxResults, firstResult);
    }

    private List<TestrunEntity> findTestrunEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TestrunEntity.class));
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

    public TestrunEntity findTestrunEntity(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TestrunEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getTestrunEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TestrunEntity> rt = cq.from(TestrunEntity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
