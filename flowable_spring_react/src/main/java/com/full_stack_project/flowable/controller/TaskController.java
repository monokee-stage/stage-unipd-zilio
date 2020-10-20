package com.full_stack_project.flowable.controller;

import com.full_stack_project.flowable.modal.Task;
import com.full_stack_project.flowable.modal.TaskNameOnly;
import com.full_stack_project.flowable.modal.User;
import com.full_stack_project.flowable.repository.TaskRepository;
import com.full_stack_project.flowable.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {
    private List<String> admins;
    private List<String> users;
    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/task/{assignee}")
    List<Task> getName(@PathVariable String assignee){
        return taskRepository.findTaskNameByASSIGNEE_(assignee);
    }

    @GetMapping("/addToList/{admin}&{user}")
    public void add(@PathVariable String admin, @PathVariable String user) {
        admins.add(admin);
        users.add(user);
    }

    @GetMapping("/request/{userAdmin}&{simpleUser}")
    public void runRequest(@PathVariable String userAdmin, @PathVariable String[] simpleUser) throws Exception {
        //List<Task> t = getName(userAdmin);
        //appController.ProcessRequest(t.get(0).getASSIGNEE_(), simpleUser[0]);
        AppController appController = new AppController();
        appController.ProcessRequest(userAdmin, simpleUser);
    }

    @GetMapping("/request/onlyUser/{user}")
    public void runRequestUser(@PathVariable String user) throws Exception{
        AppController appController = new AppController();
        appController.ProcessRequestUser(user);
    }

    @GetMapping("/task/getTaskById")
    public Task getTaskById(String id){
        return taskRepository.getTaskByID(id);
    }

    @GetMapping("/task/getTaskByGivenId/{id}")
    public Task getTaskByGivenId(@PathVariable String id){
        return taskRepository.getTaskByID(id);
    }
}
