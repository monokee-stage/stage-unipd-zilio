package com.full_stack_project.flowable.repository;

import com.full_stack_project.flowable.modal.AddAppToUser;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Repository
public class InsertAppToUserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insertWithQuery(AddAppToUser addAppToUser) {
        entityManager.createNativeQuery("INSERT INTO ACT_ID_USER_APP (user, value, validated) VALUES (?,?,?)")
                .setParameter(1, addAppToUser.getUser())
                .setParameter(2, addAppToUser.getValue())
                .setParameter(3, addAppToUser.getValidation())
                .executeUpdate();
    }

    @Transactional
    public void checkWithQuery(AddAppToUser addAppToUser){
        entityManager.createNativeQuery("SELECT value from ACT_ID_USER_APP  WHERE user = ?")
                .setParameter(1, addAppToUser.getUser())
                .executeUpdate();
    }

    @Transactional
    public void insertWithEntityManager(AddAppToUser addAppToUser) {
        this.entityManager.persist(addAppToUser);
    }

    @Transactional
    public void deleteRow(AddAppToUser addAppToUser){
        entityManager.createNativeQuery("DELETE FROM ACT_ID_USER_APP WHERE user = ? AND value = ? AND validated = ?")
                .setParameter(1, addAppToUser.getUser())
                .setParameter(2, addAppToUser.getValue())
                .setParameter(3, addAppToUser.getValidation())
                .executeUpdate();
    }
}
