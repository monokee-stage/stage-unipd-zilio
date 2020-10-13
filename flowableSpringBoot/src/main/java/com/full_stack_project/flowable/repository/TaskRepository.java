package com.full_stack_project.flowable.repository;

import com.full_stack_project.flowable.modal.App;
import com.full_stack_project.flowable.modal.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    String FIND_TASK_NAME = "SELECT ID_,NAME_,ASSIGNEE_ FROM ACT_RU_TASK WHERE ASSIGNEE_ = :assignee";

    @Query(value = FIND_TASK_NAME, nativeQuery = true)
    Task findTaskNameByASSIGNEE_(@Param("assignee") String assignee);
}
