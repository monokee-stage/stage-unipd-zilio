package com.full_stack_project.flowable.repository;

import com.full_stack_project.flowable.modal.GetLastUserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GetLastUserAppRepository extends JpaRepository<GetLastUserApp, String> {
    String FIND_LAST_USER_APP = "SELECT USER,value FROM ACT_ID_USER_APP ORDER BY id DESC LIMIT 1";

    @Query(value = FIND_LAST_USER_APP, nativeQuery = true)
    GetLastUserApp findLast();
}
