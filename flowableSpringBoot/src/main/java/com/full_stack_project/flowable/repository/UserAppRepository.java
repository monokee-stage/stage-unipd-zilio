package com.full_stack_project.flowable.repository;

import com.full_stack_project.flowable.modal.AddAppToUser;
import com.full_stack_project.flowable.modal.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserAppRepository extends JpaRepository<AddAppToUser, String> {
    String FIND_LAST = "SELECT id, value FROM ACT_ID_USER_APP where id != 'admin' ORDER BY id ASC LIMIT 1;";

    @Query(value = FIND_LAST, nativeQuery = true)
    AddAppToUser findLast();
}
