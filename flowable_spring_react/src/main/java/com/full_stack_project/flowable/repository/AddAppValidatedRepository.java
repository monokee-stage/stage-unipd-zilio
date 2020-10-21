package com.full_stack_project.flowable.repository;

import com.full_stack_project.flowable.modal.AddAppToUser;
import com.full_stack_project.flowable.modal.AddAppValidated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AddAppValidatedRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Modifying
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void insertWithQueryToAppValidated(AddAppValidated addAppValidated) {
        entityManager.createNativeQuery("INSERT INTO ACT_ID_USER_APP_VALIDATED (username, app_name, validator) VALUES (?,?,?)")
                .setParameter(1, addAppValidated.getUser())
                .setParameter(2, addAppValidated.getValue())
                .setParameter(3, addAppValidated.getValidator())
                .executeUpdate();
    }

    @Transactional
    public void checkWithQueryToAppValidated(AddAppValidated addAppValidated){
        entityManager.createNativeQuery("SELECT app_name from ACT_ID_USER_APP_VALIDATED  WHERE username = ?")
                .setParameter(1, addAppValidated.getUser())
                .executeUpdate();
    }

    @Transactional
    public void insertWithEntityManagerToAppValidated(AddAppValidated addAppValidated) {
        this.entityManager.persist(addAppValidated);
    }
}
