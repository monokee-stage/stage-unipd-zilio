package com.full_stack_project.flowable.repository;

import com.full_stack_project.flowable.modal.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class AdminRepository{
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insertAdmin(Admin admin) {
        entityManager.createNativeQuery("INSERT INTO ACT_ADMINS (username) VALUES (?)")
                .setParameter(1, admin.getUsername())
                .executeUpdate();
    }
}
