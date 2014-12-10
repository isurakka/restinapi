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
import Entities.ProjectEntity;
import Entities.UserEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Administrator
 */
public class UserEntityJpaController implements Serializable {

    public UserEntityJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UserEntity userEntity) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (userEntity.getProjectEntityList() == null) {
            userEntity.setProjectEntityList(new ArrayList<ProjectEntity>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<ProjectEntity> attachedProjectEntityList = new ArrayList<ProjectEntity>();
            for (ProjectEntity projectEntityListProjectEntityToAttach : userEntity.getProjectEntityList()) {
                projectEntityListProjectEntityToAttach = em.getReference(projectEntityListProjectEntityToAttach.getClass(), projectEntityListProjectEntityToAttach.getName());
                attachedProjectEntityList.add(projectEntityListProjectEntityToAttach);
            }
            userEntity.setProjectEntityList(attachedProjectEntityList);
            em.persist(userEntity);
            for (ProjectEntity projectEntityListProjectEntity : userEntity.getProjectEntityList()) {
                UserEntity oldUserNameOfProjectEntityListProjectEntity = projectEntityListProjectEntity.getUserName();
                projectEntityListProjectEntity.setUserName(userEntity);
                projectEntityListProjectEntity = em.merge(projectEntityListProjectEntity);
                if (oldUserNameOfProjectEntityListProjectEntity != null) {
                    oldUserNameOfProjectEntityListProjectEntity.getProjectEntityList().remove(projectEntityListProjectEntity);
                    oldUserNameOfProjectEntityListProjectEntity = em.merge(oldUserNameOfProjectEntityListProjectEntity);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findUserEntity(userEntity.getName()) != null) {
                throw new PreexistingEntityException("UserEntity " + userEntity + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UserEntity userEntity) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            UserEntity persistentUserEntity = em.find(UserEntity.class, userEntity.getName());
            List<ProjectEntity> projectEntityListOld = persistentUserEntity.getProjectEntityList();
            List<ProjectEntity> projectEntityListNew = userEntity.getProjectEntityList();
            List<String> illegalOrphanMessages = null;
            for (ProjectEntity projectEntityListOldProjectEntity : projectEntityListOld) {
                if (!projectEntityListNew.contains(projectEntityListOldProjectEntity)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProjectEntity " + projectEntityListOldProjectEntity + " since its userName field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<ProjectEntity> attachedProjectEntityListNew = new ArrayList<ProjectEntity>();
            for (ProjectEntity projectEntityListNewProjectEntityToAttach : projectEntityListNew) {
                projectEntityListNewProjectEntityToAttach = em.getReference(projectEntityListNewProjectEntityToAttach.getClass(), projectEntityListNewProjectEntityToAttach.getName());
                attachedProjectEntityListNew.add(projectEntityListNewProjectEntityToAttach);
            }
            projectEntityListNew = attachedProjectEntityListNew;
            userEntity.setProjectEntityList(projectEntityListNew);
            userEntity = em.merge(userEntity);
            for (ProjectEntity projectEntityListNewProjectEntity : projectEntityListNew) {
                if (!projectEntityListOld.contains(projectEntityListNewProjectEntity)) {
                    UserEntity oldUserNameOfProjectEntityListNewProjectEntity = projectEntityListNewProjectEntity.getUserName();
                    projectEntityListNewProjectEntity.setUserName(userEntity);
                    projectEntityListNewProjectEntity = em.merge(projectEntityListNewProjectEntity);
                    if (oldUserNameOfProjectEntityListNewProjectEntity != null && !oldUserNameOfProjectEntityListNewProjectEntity.equals(userEntity)) {
                        oldUserNameOfProjectEntityListNewProjectEntity.getProjectEntityList().remove(projectEntityListNewProjectEntity);
                        oldUserNameOfProjectEntityListNewProjectEntity = em.merge(oldUserNameOfProjectEntityListNewProjectEntity);
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
                String id = userEntity.getName();
                if (findUserEntity(id) == null) {
                    throw new NonexistentEntityException("The userEntity with id " + id + " no longer exists.");
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
            UserEntity userEntity;
            try {
                userEntity = em.getReference(UserEntity.class, id);
                userEntity.getName();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The userEntity with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ProjectEntity> projectEntityListOrphanCheck = userEntity.getProjectEntityList();
            for (ProjectEntity projectEntityListOrphanCheckProjectEntity : projectEntityListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This UserEntity (" + userEntity + ") cannot be destroyed since the ProjectEntity " + projectEntityListOrphanCheckProjectEntity + " in its projectEntityList field has a non-nullable userName field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(userEntity);
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

    public List<UserEntity> findUserEntityEntities() {
        return findUserEntityEntities(true, -1, -1);
    }

    public List<UserEntity> findUserEntityEntities(int maxResults, int firstResult) {
        return findUserEntityEntities(false, maxResults, firstResult);
    }

    private List<UserEntity> findUserEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UserEntity.class));
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

    public UserEntity findUserEntity(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UserEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UserEntity> rt = cq.from(UserEntity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
