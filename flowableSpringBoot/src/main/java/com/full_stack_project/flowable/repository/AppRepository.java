package com.full_stack_project.flowable.repository;

import com.full_stack_project.flowable.modal.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppRepository extends JpaRepository<App, String> {
    String FIND_NAME = "SELECT NAME FROM APP_LIST";
    String FIND_IF_NEED_VALIDATION = "SELECT NEED_ADMIN from APP_LIST where NAME = :name";
    String FIND_APPS_NAME = "SELECT value FROM ACT_ID_USER_APP WHERE user = :user";

    @Query(value = FIND_NAME, nativeQuery = true)
    List<AppNameOnly> findAllAppsNames();

    @Query(value = FIND_IF_NEED_VALIDATION, nativeQuery = true)
    int findIfNeedValidation(@Param("name") String name);

    @Query(value = FIND_APPS_NAME, nativeQuery = true)
    List<AppNameUserOnly> findUserApps(@Param("user") String user);
}
