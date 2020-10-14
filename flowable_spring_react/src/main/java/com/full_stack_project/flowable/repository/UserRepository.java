package com.full_stack_project.flowable.repository;

import com.full_stack_project.flowable.modal.Task;
import com.full_stack_project.flowable.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    String FIND_USER = "SELECT ID_,PWD_ FROM ACT_ID_USER WHERE ID_ = :id AND PWD_ = :pwd";

    @Query(value = FIND_USER, nativeQuery = true)
    User findUserByIdPwd(@Param("id") String id, @Param("pwd") String pwd);
}