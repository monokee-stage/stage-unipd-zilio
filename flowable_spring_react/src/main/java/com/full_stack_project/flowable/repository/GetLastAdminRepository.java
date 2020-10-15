package com.full_stack_project.flowable.repository;

import com.full_stack_project.flowable.modal.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GetLastAdminRepository extends JpaRepository<Admin, String> {
    String FIND_LAST_ADMIN = "SELECT username FROM ACT_ADMINS ORDER BY id DESC LIMIT 1";

    @Query(value = FIND_LAST_ADMIN, nativeQuery = true)
    Admin findLastAdmin();
}
