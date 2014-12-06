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
import java.util.ArrayList;
import java.util.Collection;
import Entities.RequestEntity;
import Entities.ScriptEntity;
import Entities.TestcaseEntity;
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
        if (scriptEntity.getRequestCollection() == null) {
            scriptEntity.setRequestCollection(new ArrayList<RequestEntity>());
        }
        if (scriptEntity.getTestcaseCollection() == null) {
            scriptEntity.setTestcaseCollection(new ArrayList<TestcaseEntity>());
        }
        if (scriptEntity.getRequestEntityCollection() == null) {
            scriptEntity.setRequestEntityCollection(new ArrayList<RequestEntity>());
        }
        if (scriptEntity.getTestcaseEntityCollection() == null) {
            scriptEntity.setTestcaseEntityCollection(new ArrayList<TestcaseEntity>());
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
            Collection<RequestEntity> attachedRequestCollection = new ArrayList<RequestEntity>();
            for (RequestEntity requestCollectionRequestEntityToAttach : scriptEntity.getRequestCollection()) {
                requestCollectionRequestEntityToAttach = em.getReference(requestCollectionRequestEntityToAttach.getClass(), requestCollectionRequestEntityToAttach.getProjectId());
                attachedRequestCollection.add(requestCollectionRequestEntityToAttach);
            }
            scriptEntity.setRequestCollection(attachedRequestCollection);
            Collection<TestcaseEntity> attachedTestcaseCollection = new ArrayList<TestcaseEntity>();
            for (TestcaseEntity testcaseCollectionTestcaseEntityToAttach : scriptEntity.getTestcaseCollection()) {
                testcaseCollectionTestcaseEntityToAttach = em.getReference(testcaseCollectionTestcaseEntityToAttach.getClass(), testcaseCollectionTestcaseEntityToAttach.getTestcaseId());
                attachedTestcaseCollection.add(testcaseCollectionTestcaseEntityToAttach);
            }
            scriptEntity.setTestcaseCollection(attachedTestcaseCollection);
            Collection<RequestEntity> attachedRequestEntityCollection = new ArrayList<RequestEntity>();
            for (RequestEntity requestEntityCollectionRequestEntityToAttach : scriptEntity.getRequestEntityCollection()) {
                requestEntityCollectionRequestEntityToAttach = em.getReference(requestEntityCollectionRequestEntityToAttach.getClass(), requestEntityCollectionRequestEntityToAttach.getProjectId());
                attachedRequestEntityCollection.add(requestEntityCollectionRequestEntityToAttach);
            }
            scriptEntity.setRequestEntityCollection(attachedRequestEntityCollection);
            Collection<TestcaseEntity> attachedTestcaseEntityCollection = new ArrayList<TestcaseEntity>();
            for (TestcaseEntity testcaseEntityCollectionTestcaseEntityToAttach : scriptEntity.getTestcaseEntityCollection()) {
                testcaseEntityCollectionTestcaseEntityToAttach = em.getReference(testcaseEntityCollectionTestcaseEntityToAttach.getClass(), testcaseEntityCollectionTestcaseEntityToAttach.getTestcaseId());
                attachedTestcaseEntityCollection.add(testcaseEntityCollectionTestcaseEntityToAttach);
            }
            scriptEntity.setTestcaseEntityCollection(attachedTestcaseEntityCollection);
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
            for (RequestEntity requestCollectionRequestEntity : scriptEntity.getRequestCollection()) {
                ScriptEntity oldScriptIdOfRequestCollectionRequestEntity = requestCollectionRequestEntity.getScriptId();
                requestCollectionRequestEntity.setScriptId(scriptEntity);
                requestCollectionRequestEntity = em.merge(requestCollectionRequestEntity);
                if (oldScriptIdOfRequestCollectionRequestEntity != null) {
                    oldScriptIdOfRequestCollectionRequestEntity.getRequestCollection().remove(requestCollectionRequestEntity);
                    oldScriptIdOfRequestCollectionRequestEntity = em.merge(oldScriptIdOfRequestCollectionRequestEntity);
                }
            }
            for (TestcaseEntity testcaseCollectionTestcaseEntity : scriptEntity.getTestcaseCollection()) {
                ScriptEntity oldScriptIdOfTestcaseCollectionTestcaseEntity = testcaseCollectionTestcaseEntity.getScriptId();
                testcaseCollectionTestcaseEntity.setScriptId(scriptEntity);
                testcaseCollectionTestcaseEntity = em.merge(testcaseCollectionTestcaseEntity);
                if (oldScriptIdOfTestcaseCollectionTestcaseEntity != null) {
                    oldScriptIdOfTestcaseCollectionTestcaseEntity.getTestcaseCollection().remove(testcaseCollectionTestcaseEntity);
                    oldScriptIdOfTestcaseCollectionTestcaseEntity = em.merge(oldScriptIdOfTestcaseCollectionTestcaseEntity);
                }
            }
            for (RequestEntity requestEntityCollectionRequestEntity : scriptEntity.getRequestEntityCollection()) {
                ScriptEntity oldScriptIdOfRequestEntityCollectionRequestEntity = requestEntityCollectionRequestEntity.getScriptId();
                requestEntityCollectionRequestEntity.setScriptId(scriptEntity);
                requestEntityCollectionRequestEntity = em.merge(requestEntityCollectionRequestEntity);
                if (oldScriptIdOfRequestEntityCollectionRequestEntity != null) {
                    oldScriptIdOfRequestEntityCollectionRequestEntity.getRequestEntityCollection().remove(requestEntityCollectionRequestEntity);
                    oldScriptIdOfRequestEntityCollectionRequestEntity = em.merge(oldScriptIdOfRequestEntityCollectionRequestEntity);
                }
            }
            for (TestcaseEntity testcaseEntityCollectionTestcaseEntity : scriptEntity.getTestcaseEntityCollection()) {
                ScriptEntity oldScriptIdOfTestcaseEntityCollectionTestcaseEntity = testcaseEntityCollectionTestcaseEntity.getScriptId();
                testcaseEntityCollectionTestcaseEntity.setScriptId(scriptEntity);
                testcaseEntityCollectionTestcaseEntity = em.merge(testcaseEntityCollectionTestcaseEntity);
                if (oldScriptIdOfTestcaseEntityCollectionTestcaseEntity != null) {
                    oldScriptIdOfTestcaseEntityCollectionTestcaseEntity.getTestcaseEntityCollection().remove(testcaseEntityCollectionTestcaseEntity);
                    oldScriptIdOfTestcaseEntityCollectionTestcaseEntity = em.merge(oldScriptIdOfTestcaseEntityCollectionTestcaseEntity);
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
            Collection<RequestEntity> requestCollectionOld = persistentScriptEntity.getRequestCollection();
            Collection<RequestEntity> requestCollectionNew = scriptEntity.getRequestCollection();
            Collection<TestcaseEntity> testcaseCollectionOld = persistentScriptEntity.getTestcaseCollection();
            Collection<TestcaseEntity> testcaseCollectionNew = scriptEntity.getTestcaseCollection();
            Collection<RequestEntity> requestEntityCollectionOld = persistentScriptEntity.getRequestEntityCollection();
            Collection<RequestEntity> requestEntityCollectionNew = scriptEntity.getRequestEntityCollection();
            Collection<TestcaseEntity> testcaseEntityCollectionOld = persistentScriptEntity.getTestcaseEntityCollection();
            Collection<TestcaseEntity> testcaseEntityCollectionNew = scriptEntity.getTestcaseEntityCollection();
            Collection<ProjectEntity> attachedProjectEntityCollectionNew = new ArrayList<ProjectEntity>();
            for (ProjectEntity projectEntityCollectionNewProjectEntityToAttach : projectEntityCollectionNew) {
                projectEntityCollectionNewProjectEntityToAttach = em.getReference(projectEntityCollectionNewProjectEntityToAttach.getClass(), projectEntityCollectionNewProjectEntityToAttach.getName());
                attachedProjectEntityCollectionNew.add(projectEntityCollectionNewProjectEntityToAttach);
            }
            projectEntityCollectionNew = attachedProjectEntityCollectionNew;
            scriptEntity.setProjectEntityCollection(projectEntityCollectionNew);
            Collection<RequestEntity> attachedRequestCollectionNew = new ArrayList<RequestEntity>();
            for (RequestEntity requestCollectionNewRequestEntityToAttach : requestCollectionNew) {
                requestCollectionNewRequestEntityToAttach = em.getReference(requestCollectionNewRequestEntityToAttach.getClass(), requestCollectionNewRequestEntityToAttach.getProjectId());
                attachedRequestCollectionNew.add(requestCollectionNewRequestEntityToAttach);
            }
            requestCollectionNew = attachedRequestCollectionNew;
            scriptEntity.setRequestCollection(requestCollectionNew);
            Collection<TestcaseEntity> attachedTestcaseCollectionNew = new ArrayList<TestcaseEntity>();
            for (TestcaseEntity testcaseCollectionNewTestcaseEntityToAttach : testcaseCollectionNew) {
                testcaseCollectionNewTestcaseEntityToAttach = em.getReference(testcaseCollectionNewTestcaseEntityToAttach.getClass(), testcaseCollectionNewTestcaseEntityToAttach.getTestcaseId());
                attachedTestcaseCollectionNew.add(testcaseCollectionNewTestcaseEntityToAttach);
            }
            testcaseCollectionNew = attachedTestcaseCollectionNew;
            scriptEntity.setTestcaseCollection(testcaseCollectionNew);
            Collection<RequestEntity> attachedRequestEntityCollectionNew = new ArrayList<RequestEntity>();
            for (RequestEntity requestEntityCollectionNewRequestEntityToAttach : requestEntityCollectionNew) {
                requestEntityCollectionNewRequestEntityToAttach = em.getReference(requestEntityCollectionNewRequestEntityToAttach.getClass(), requestEntityCollectionNewRequestEntityToAttach.getProjectId());
                attachedRequestEntityCollectionNew.add(requestEntityCollectionNewRequestEntityToAttach);
            }
            requestEntityCollectionNew = attachedRequestEntityCollectionNew;
            scriptEntity.setRequestEntityCollection(requestEntityCollectionNew);
            Collection<TestcaseEntity> attachedTestcaseEntityCollectionNew = new ArrayList<TestcaseEntity>();
            for (TestcaseEntity testcaseEntityCollectionNewTestcaseEntityToAttach : testcaseEntityCollectionNew) {
                testcaseEntityCollectionNewTestcaseEntityToAttach = em.getReference(testcaseEntityCollectionNewTestcaseEntityToAttach.getClass(), testcaseEntityCollectionNewTestcaseEntityToAttach.getTestcaseId());
                attachedTestcaseEntityCollectionNew.add(testcaseEntityCollectionNewTestcaseEntityToAttach);
            }
            testcaseEntityCollectionNew = attachedTestcaseEntityCollectionNew;
            scriptEntity.setTestcaseEntityCollection(testcaseEntityCollectionNew);
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
            for (RequestEntity requestCollectionOldRequestEntity : requestCollectionOld) {
                if (!requestCollectionNew.contains(requestCollectionOldRequestEntity)) {
                    requestCollectionOldRequestEntity.setScriptId(null);
                    requestCollectionOldRequestEntity = em.merge(requestCollectionOldRequestEntity);
                }
            }
            for (RequestEntity requestCollectionNewRequestEntity : requestCollectionNew) {
                if (!requestCollectionOld.contains(requestCollectionNewRequestEntity)) {
                    ScriptEntity oldScriptIdOfRequestCollectionNewRequestEntity = requestCollectionNewRequestEntity.getScriptId();
                    requestCollectionNewRequestEntity.setScriptId(scriptEntity);
                    requestCollectionNewRequestEntity = em.merge(requestCollectionNewRequestEntity);
                    if (oldScriptIdOfRequestCollectionNewRequestEntity != null && !oldScriptIdOfRequestCollectionNewRequestEntity.equals(scriptEntity)) {
                        oldScriptIdOfRequestCollectionNewRequestEntity.getRequestCollection().remove(requestCollectionNewRequestEntity);
                        oldScriptIdOfRequestCollectionNewRequestEntity = em.merge(oldScriptIdOfRequestCollectionNewRequestEntity);
                    }
                }
            }
            for (TestcaseEntity testcaseCollectionOldTestcaseEntity : testcaseCollectionOld) {
                if (!testcaseCollectionNew.contains(testcaseCollectionOldTestcaseEntity)) {
                    testcaseCollectionOldTestcaseEntity.setScriptId(null);
                    testcaseCollectionOldTestcaseEntity = em.merge(testcaseCollectionOldTestcaseEntity);
                }
            }
            for (TestcaseEntity testcaseCollectionNewTestcaseEntity : testcaseCollectionNew) {
                if (!testcaseCollectionOld.contains(testcaseCollectionNewTestcaseEntity)) {
                    ScriptEntity oldScriptIdOfTestcaseCollectionNewTestcaseEntity = testcaseCollectionNewTestcaseEntity.getScriptId();
                    testcaseCollectionNewTestcaseEntity.setScriptId(scriptEntity);
                    testcaseCollectionNewTestcaseEntity = em.merge(testcaseCollectionNewTestcaseEntity);
                    if (oldScriptIdOfTestcaseCollectionNewTestcaseEntity != null && !oldScriptIdOfTestcaseCollectionNewTestcaseEntity.equals(scriptEntity)) {
                        oldScriptIdOfTestcaseCollectionNewTestcaseEntity.getTestcaseCollection().remove(testcaseCollectionNewTestcaseEntity);
                        oldScriptIdOfTestcaseCollectionNewTestcaseEntity = em.merge(oldScriptIdOfTestcaseCollectionNewTestcaseEntity);
                    }
                }
            }
            for (RequestEntity requestEntityCollectionOldRequestEntity : requestEntityCollectionOld) {
                if (!requestEntityCollectionNew.contains(requestEntityCollectionOldRequestEntity)) {
                    requestEntityCollectionOldRequestEntity.setScriptId(null);
                    requestEntityCollectionOldRequestEntity = em.merge(requestEntityCollectionOldRequestEntity);
                }
            }
            for (RequestEntity requestEntityCollectionNewRequestEntity : requestEntityCollectionNew) {
                if (!requestEntityCollectionOld.contains(requestEntityCollectionNewRequestEntity)) {
                    ScriptEntity oldScriptIdOfRequestEntityCollectionNewRequestEntity = requestEntityCollectionNewRequestEntity.getScriptId();
                    requestEntityCollectionNewRequestEntity.setScriptId(scriptEntity);
                    requestEntityCollectionNewRequestEntity = em.merge(requestEntityCollectionNewRequestEntity);
                    if (oldScriptIdOfRequestEntityCollectionNewRequestEntity != null && !oldScriptIdOfRequestEntityCollectionNewRequestEntity.equals(scriptEntity)) {
                        oldScriptIdOfRequestEntityCollectionNewRequestEntity.getRequestEntityCollection().remove(requestEntityCollectionNewRequestEntity);
                        oldScriptIdOfRequestEntityCollectionNewRequestEntity = em.merge(oldScriptIdOfRequestEntityCollectionNewRequestEntity);
                    }
                }
            }
            for (TestcaseEntity testcaseEntityCollectionOldTestcaseEntity : testcaseEntityCollectionOld) {
                if (!testcaseEntityCollectionNew.contains(testcaseEntityCollectionOldTestcaseEntity)) {
                    testcaseEntityCollectionOldTestcaseEntity.setScriptId(null);
                    testcaseEntityCollectionOldTestcaseEntity = em.merge(testcaseEntityCollectionOldTestcaseEntity);
                }
            }
            for (TestcaseEntity testcaseEntityCollectionNewTestcaseEntity : testcaseEntityCollectionNew) {
                if (!testcaseEntityCollectionOld.contains(testcaseEntityCollectionNewTestcaseEntity)) {
                    ScriptEntity oldScriptIdOfTestcaseEntityCollectionNewTestcaseEntity = testcaseEntityCollectionNewTestcaseEntity.getScriptId();
                    testcaseEntityCollectionNewTestcaseEntity.setScriptId(scriptEntity);
                    testcaseEntityCollectionNewTestcaseEntity = em.merge(testcaseEntityCollectionNewTestcaseEntity);
                    if (oldScriptIdOfTestcaseEntityCollectionNewTestcaseEntity != null && !oldScriptIdOfTestcaseEntityCollectionNewTestcaseEntity.equals(scriptEntity)) {
                        oldScriptIdOfTestcaseEntityCollectionNewTestcaseEntity.getTestcaseEntityCollection().remove(testcaseEntityCollectionNewTestcaseEntity);
                        oldScriptIdOfTestcaseEntityCollectionNewTestcaseEntity = em.merge(oldScriptIdOfTestcaseEntityCollectionNewTestcaseEntity);
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
            Collection<RequestEntity> requestCollection = scriptEntity.getRequestCollection();
            for (RequestEntity requestCollectionRequestEntity : requestCollection) {
                requestCollectionRequestEntity.setScriptId(null);
                requestCollectionRequestEntity = em.merge(requestCollectionRequestEntity);
            }
            Collection<TestcaseEntity> testcaseCollection = scriptEntity.getTestcaseCollection();
            for (TestcaseEntity testcaseCollectionTestcaseEntity : testcaseCollection) {
                testcaseCollectionTestcaseEntity.setScriptId(null);
                testcaseCollectionTestcaseEntity = em.merge(testcaseCollectionTestcaseEntity);
            }
            Collection<RequestEntity> requestEntityCollection = scriptEntity.getRequestEntityCollection();
            for (RequestEntity requestEntityCollectionRequestEntity : requestEntityCollection) {
                requestEntityCollectionRequestEntity.setScriptId(null);
                requestEntityCollectionRequestEntity = em.merge(requestEntityCollectionRequestEntity);
            }
            Collection<TestcaseEntity> testcaseEntityCollection = scriptEntity.getTestcaseEntityCollection();
            for (TestcaseEntity testcaseEntityCollectionTestcaseEntity : testcaseEntityCollection) {
                testcaseEntityCollectionTestcaseEntity.setScriptId(null);
                testcaseEntityCollectionTestcaseEntity = em.merge(testcaseEntityCollectionTestcaseEntity);
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
