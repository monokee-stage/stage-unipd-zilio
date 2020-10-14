package com.full_stack_project.flowable.controller;

import com.full_stack_project.flowable.modal.Task;
import com.full_stack_project.flowable.modal.User;
import com.full_stack_project.flowable.repository.TaskRepository;
import com.full_stack_project.flowable.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/task/{assignee}")
    Task getName(@PathVariable String assignee){
        return taskRepository.findTaskNameByASSIGNEE_(assignee);
    }

    @GetMapping("/request/{userAdmin}&{simpleUser}")
    public void runRequest(@PathVariable String userAdmin, @PathVariable String simpleUser) throws Exception {
        Task t = getName(userAdmin);
        AppController appController = new AppController();
        appController.ProcessRequest(t.getASSIGNEE_(), simpleUser);
    }
}
