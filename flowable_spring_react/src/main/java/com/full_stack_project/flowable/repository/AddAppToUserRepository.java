package com.full_stack_project.flowable.repository;

import com.full_stack_project.flowable.modal.AddAppToUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddAppToUserRepository extends JpaRepository<AddAppToUser, String> {
    String GET_ALL_REQUEST = "SELECT * FROM ACT_ID_USER_APP";

    @Query(value = GET_ALL_REQUEST, nativeQuery = true)
    List<AddAppToUser> getAllRequest();
}
