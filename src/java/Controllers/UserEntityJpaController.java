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
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Matti
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
        if (userEntity.getProjectEntityCollection() == null) {
            userEntity.setProjectEntityCollection(new ArrayList<ProjectEntity>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<ProjectEntity> attachedProjectEntityCollection = new ArrayList<ProjectEntity>();
            for (ProjectEntity projectEntityCollectionProjectEntityToAttach : userEntity.getProjectEntityCollection()) {
                projectEntityCollectionProjectEntityToAttach = em.getReference(projectEntityCollectionProjectEntityToAttach.getClass(), projectEntityCollectionProjectEntityToAttach.getName());
                attachedProjectEntityCollection.add(projectEntityCollectionProjectEntityToAttach);
            }
            userEntity.setProjectEntityCollection(attachedProjectEntityCollection);
            em.persist(userEntity);
            for (ProjectEntity projectEntityCollectionProjectEntity : userEntity.getProjectEntityCollection()) {
                UserEntity oldUserNameOfProjectEntityCollectionProjectEntity = projectEntityCollectionProjectEntity.getUserName();
                projectEntityCollectionProjectEntity.setUserName(userEntity);
                projectEntityCollectionProjectEntity = em.merge(projectEntityCollectionProjectEntity);
                if (oldUserNameOfProjectEntityCollectionProjectEntity != null) {
                    oldUserNameOfProjectEntityCollectionProjectEntity.getProjectEntityCollection().remove(projectEntityCollectionProjectEntity);
                    oldUserNameOfProjectEntityCollectionProjectEntity = em.merge(oldUserNameOfProjectEntityCollectionProjectEntity);
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
            Collection<ProjectEntity> projectEntityCollectionOld = persistentUserEntity.getProjectEntityCollection();
            Collection<ProjectEntity> projectEntityCollectionNew = userEntity.getProjectEntityCollection();
            List<String> illegalOrphanMessages = null;
            for (ProjectEntity projectEntityCollectionOldProjectEntity : projectEntityCollectionOld) {
                if (!projectEntityCollectionNew.contains(projectEntityCollectionOldProjectEntity)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProjectEntity " + projectEntityCollectionOldProjectEntity + " since its userName field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<ProjectEntity> attachedProjectEntityCollectionNew = new ArrayList<ProjectEntity>();
            for (ProjectEntity projectEntityCollectionNewProjectEntityToAttach : projectEntityCollectionNew) {
                projectEntityCollectionNewProjectEntityToAttach = em.getReference(projectEntityCollectionNewProjectEntityToAttach.getClass(), projectEntityCollectionNewProjectEntityToAttach.getName());
                attachedProjectEntityCollectionNew.add(projectEntityCollectionNewProjectEntityToAttach);
            }
            projectEntityCollectionNew = attachedProjectEntityCollectionNew;
            userEntity.setProjectEntityCollection(projectEntityCollectionNew);
            userEntity = em.merge(userEntity);
            for (ProjectEntity projectEntityCollectionNewProjectEntity : projectEntityCollectionNew) {
                if (!projectEntityCollectionOld.contains(projectEntityCollectionNewProjectEntity)) {
                    UserEntity oldUserNameOfProjectEntityCollectionNewProjectEntity = projectEntityCollectionNewProjectEntity.getUserName();
                    projectEntityCollectionNewProjectEntity.setUserName(userEntity);
                    projectEntityCollectionNewProjectEntity = em.merge(projectEntityCollectionNewProjectEntity);
                    if (oldUserNameOfProjectEntityCollectionNewProjectEntity != null && !oldUserNameOfProjectEntityCollectionNewProjectEntity.equals(userEntity)) {
                        oldUserNameOfProjectEntityCollectionNewProjectEntity.getProjectEntityCollection().remove(projectEntityCollectionNewProjectEntity);
                        oldUserNameOfProjectEntityCollectionNewProjectEntity = em.merge(oldUserNameOfProjectEntityCollectionNewProjectEntity);
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
            Collection<ProjectEntity> projectEntityCollectionOrphanCheck = userEntity.getProjectEntityCollection();
            for (ProjectEntity projectEntityCollectionOrphanCheckProjectEntity : projectEntityCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This UserEntity (" + userEntity + ") cannot be destroyed since the ProjectEntity " + projectEntityCollectionOrphanCheckProjectEntity + " in its projectEntityCollection field has a non-nullable userName field.");
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
