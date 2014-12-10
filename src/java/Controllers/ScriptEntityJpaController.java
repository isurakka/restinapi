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
import Entities.RequestEntity;
import java.util.ArrayList;
import java.util.List;
import Entities.ProjectEntity;
import Entities.ScriptEntity;
import Entities.TestcaseEntity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Administrator
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
        if (scriptEntity.getRequestEntityList() == null) {
            scriptEntity.setRequestEntityList(new ArrayList<RequestEntity>());
        }
        if (scriptEntity.getProjectEntityList() == null) {
            scriptEntity.setProjectEntityList(new ArrayList<ProjectEntity>());
        }
        if (scriptEntity.getTestcaseEntityList() == null) {
            scriptEntity.setTestcaseEntityList(new ArrayList<TestcaseEntity>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<RequestEntity> attachedRequestEntityList = new ArrayList<RequestEntity>();
            for (RequestEntity requestEntityListRequestEntityToAttach : scriptEntity.getRequestEntityList()) {
                requestEntityListRequestEntityToAttach = em.getReference(requestEntityListRequestEntityToAttach.getClass(), requestEntityListRequestEntityToAttach.getRequestId());
                attachedRequestEntityList.add(requestEntityListRequestEntityToAttach);
            }
            scriptEntity.setRequestEntityList(attachedRequestEntityList);
            List<ProjectEntity> attachedProjectEntityList = new ArrayList<ProjectEntity>();
            for (ProjectEntity projectEntityListProjectEntityToAttach : scriptEntity.getProjectEntityList()) {
                projectEntityListProjectEntityToAttach = em.getReference(projectEntityListProjectEntityToAttach.getClass(), projectEntityListProjectEntityToAttach.getName());
                attachedProjectEntityList.add(projectEntityListProjectEntityToAttach);
            }
            scriptEntity.setProjectEntityList(attachedProjectEntityList);
            List<TestcaseEntity> attachedTestcaseEntityList = new ArrayList<TestcaseEntity>();
            for (TestcaseEntity testcaseEntityListTestcaseEntityToAttach : scriptEntity.getTestcaseEntityList()) {
                testcaseEntityListTestcaseEntityToAttach = em.getReference(testcaseEntityListTestcaseEntityToAttach.getClass(), testcaseEntityListTestcaseEntityToAttach.getTestcaseId());
                attachedTestcaseEntityList.add(testcaseEntityListTestcaseEntityToAttach);
            }
            scriptEntity.setTestcaseEntityList(attachedTestcaseEntityList);
            em.persist(scriptEntity);
            for (RequestEntity requestEntityListRequestEntity : scriptEntity.getRequestEntityList()) {
                ScriptEntity oldScriptIdOfRequestEntityListRequestEntity = requestEntityListRequestEntity.getScriptId();
                requestEntityListRequestEntity.setScriptId(scriptEntity);
                requestEntityListRequestEntity = em.merge(requestEntityListRequestEntity);
                if (oldScriptIdOfRequestEntityListRequestEntity != null) {
                    oldScriptIdOfRequestEntityListRequestEntity.getRequestEntityList().remove(requestEntityListRequestEntity);
                    oldScriptIdOfRequestEntityListRequestEntity = em.merge(oldScriptIdOfRequestEntityListRequestEntity);
                }
            }
            for (ProjectEntity projectEntityListProjectEntity : scriptEntity.getProjectEntityList()) {
                ScriptEntity oldScriptIdOfProjectEntityListProjectEntity = projectEntityListProjectEntity.getScriptId();
                projectEntityListProjectEntity.setScriptId(scriptEntity);
                projectEntityListProjectEntity = em.merge(projectEntityListProjectEntity);
                if (oldScriptIdOfProjectEntityListProjectEntity != null) {
                    oldScriptIdOfProjectEntityListProjectEntity.getProjectEntityList().remove(projectEntityListProjectEntity);
                    oldScriptIdOfProjectEntityListProjectEntity = em.merge(oldScriptIdOfProjectEntityListProjectEntity);
                }
            }
            for (TestcaseEntity testcaseEntityListTestcaseEntity : scriptEntity.getTestcaseEntityList()) {
                ScriptEntity oldScriptIdOfTestcaseEntityListTestcaseEntity = testcaseEntityListTestcaseEntity.getScriptId();
                testcaseEntityListTestcaseEntity.setScriptId(scriptEntity);
                testcaseEntityListTestcaseEntity = em.merge(testcaseEntityListTestcaseEntity);
                if (oldScriptIdOfTestcaseEntityListTestcaseEntity != null) {
                    oldScriptIdOfTestcaseEntityListTestcaseEntity.getTestcaseEntityList().remove(testcaseEntityListTestcaseEntity);
                    oldScriptIdOfTestcaseEntityListTestcaseEntity = em.merge(oldScriptIdOfTestcaseEntityListTestcaseEntity);
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
            List<RequestEntity> requestEntityListOld = persistentScriptEntity.getRequestEntityList();
            List<RequestEntity> requestEntityListNew = scriptEntity.getRequestEntityList();
            List<ProjectEntity> projectEntityListOld = persistentScriptEntity.getProjectEntityList();
            List<ProjectEntity> projectEntityListNew = scriptEntity.getProjectEntityList();
            List<TestcaseEntity> testcaseEntityListOld = persistentScriptEntity.getTestcaseEntityList();
            List<TestcaseEntity> testcaseEntityListNew = scriptEntity.getTestcaseEntityList();
            List<RequestEntity> attachedRequestEntityListNew = new ArrayList<RequestEntity>();
            for (RequestEntity requestEntityListNewRequestEntityToAttach : requestEntityListNew) {
                requestEntityListNewRequestEntityToAttach = em.getReference(requestEntityListNewRequestEntityToAttach.getClass(), requestEntityListNewRequestEntityToAttach.getRequestId());
                attachedRequestEntityListNew.add(requestEntityListNewRequestEntityToAttach);
            }
            requestEntityListNew = attachedRequestEntityListNew;
            scriptEntity.setRequestEntityList(requestEntityListNew);
            List<ProjectEntity> attachedProjectEntityListNew = new ArrayList<ProjectEntity>();
            for (ProjectEntity projectEntityListNewProjectEntityToAttach : projectEntityListNew) {
                projectEntityListNewProjectEntityToAttach = em.getReference(projectEntityListNewProjectEntityToAttach.getClass(), projectEntityListNewProjectEntityToAttach.getName());
                attachedProjectEntityListNew.add(projectEntityListNewProjectEntityToAttach);
            }
            projectEntityListNew = attachedProjectEntityListNew;
            scriptEntity.setProjectEntityList(projectEntityListNew);
            List<TestcaseEntity> attachedTestcaseEntityListNew = new ArrayList<TestcaseEntity>();
            for (TestcaseEntity testcaseEntityListNewTestcaseEntityToAttach : testcaseEntityListNew) {
                testcaseEntityListNewTestcaseEntityToAttach = em.getReference(testcaseEntityListNewTestcaseEntityToAttach.getClass(), testcaseEntityListNewTestcaseEntityToAttach.getTestcaseId());
                attachedTestcaseEntityListNew.add(testcaseEntityListNewTestcaseEntityToAttach);
            }
            testcaseEntityListNew = attachedTestcaseEntityListNew;
            scriptEntity.setTestcaseEntityList(testcaseEntityListNew);
            scriptEntity = em.merge(scriptEntity);
            for (RequestEntity requestEntityListOldRequestEntity : requestEntityListOld) {
                if (!requestEntityListNew.contains(requestEntityListOldRequestEntity)) {
                    requestEntityListOldRequestEntity.setScriptId(null);
                    requestEntityListOldRequestEntity = em.merge(requestEntityListOldRequestEntity);
                }
            }
            for (RequestEntity requestEntityListNewRequestEntity : requestEntityListNew) {
                if (!requestEntityListOld.contains(requestEntityListNewRequestEntity)) {
                    ScriptEntity oldScriptIdOfRequestEntityListNewRequestEntity = requestEntityListNewRequestEntity.getScriptId();
                    requestEntityListNewRequestEntity.setScriptId(scriptEntity);
                    requestEntityListNewRequestEntity = em.merge(requestEntityListNewRequestEntity);
                    if (oldScriptIdOfRequestEntityListNewRequestEntity != null && !oldScriptIdOfRequestEntityListNewRequestEntity.equals(scriptEntity)) {
                        oldScriptIdOfRequestEntityListNewRequestEntity.getRequestEntityList().remove(requestEntityListNewRequestEntity);
                        oldScriptIdOfRequestEntityListNewRequestEntity = em.merge(oldScriptIdOfRequestEntityListNewRequestEntity);
                    }
                }
            }
            for (ProjectEntity projectEntityListOldProjectEntity : projectEntityListOld) {
                if (!projectEntityListNew.contains(projectEntityListOldProjectEntity)) {
                    projectEntityListOldProjectEntity.setScriptId(null);
                    projectEntityListOldProjectEntity = em.merge(projectEntityListOldProjectEntity);
                }
            }
            for (ProjectEntity projectEntityListNewProjectEntity : projectEntityListNew) {
                if (!projectEntityListOld.contains(projectEntityListNewProjectEntity)) {
                    ScriptEntity oldScriptIdOfProjectEntityListNewProjectEntity = projectEntityListNewProjectEntity.getScriptId();
                    projectEntityListNewProjectEntity.setScriptId(scriptEntity);
                    projectEntityListNewProjectEntity = em.merge(projectEntityListNewProjectEntity);
                    if (oldScriptIdOfProjectEntityListNewProjectEntity != null && !oldScriptIdOfProjectEntityListNewProjectEntity.equals(scriptEntity)) {
                        oldScriptIdOfProjectEntityListNewProjectEntity.getProjectEntityList().remove(projectEntityListNewProjectEntity);
                        oldScriptIdOfProjectEntityListNewProjectEntity = em.merge(oldScriptIdOfProjectEntityListNewProjectEntity);
                    }
                }
            }
            for (TestcaseEntity testcaseEntityListOldTestcaseEntity : testcaseEntityListOld) {
                if (!testcaseEntityListNew.contains(testcaseEntityListOldTestcaseEntity)) {
                    testcaseEntityListOldTestcaseEntity.setScriptId(null);
                    testcaseEntityListOldTestcaseEntity = em.merge(testcaseEntityListOldTestcaseEntity);
                }
            }
            for (TestcaseEntity testcaseEntityListNewTestcaseEntity : testcaseEntityListNew) {
                if (!testcaseEntityListOld.contains(testcaseEntityListNewTestcaseEntity)) {
                    ScriptEntity oldScriptIdOfTestcaseEntityListNewTestcaseEntity = testcaseEntityListNewTestcaseEntity.getScriptId();
                    testcaseEntityListNewTestcaseEntity.setScriptId(scriptEntity);
                    testcaseEntityListNewTestcaseEntity = em.merge(testcaseEntityListNewTestcaseEntity);
                    if (oldScriptIdOfTestcaseEntityListNewTestcaseEntity != null && !oldScriptIdOfTestcaseEntityListNewTestcaseEntity.equals(scriptEntity)) {
                        oldScriptIdOfTestcaseEntityListNewTestcaseEntity.getTestcaseEntityList().remove(testcaseEntityListNewTestcaseEntity);
                        oldScriptIdOfTestcaseEntityListNewTestcaseEntity = em.merge(oldScriptIdOfTestcaseEntityListNewTestcaseEntity);
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
            List<RequestEntity> requestEntityList = scriptEntity.getRequestEntityList();
            for (RequestEntity requestEntityListRequestEntity : requestEntityList) {
                requestEntityListRequestEntity.setScriptId(null);
                requestEntityListRequestEntity = em.merge(requestEntityListRequestEntity);
            }
            List<ProjectEntity> projectEntityList = scriptEntity.getProjectEntityList();
            for (ProjectEntity projectEntityListProjectEntity : projectEntityList) {
                projectEntityListProjectEntity.setScriptId(null);
                projectEntityListProjectEntity = em.merge(projectEntityListProjectEntity);
            }
            List<TestcaseEntity> testcaseEntityList = scriptEntity.getTestcaseEntityList();
            for (TestcaseEntity testcaseEntityListTestcaseEntity : testcaseEntityList) {
                testcaseEntityListTestcaseEntity.setScriptId(null);
                testcaseEntityListTestcaseEntity = em.merge(testcaseEntityListTestcaseEntity);
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
