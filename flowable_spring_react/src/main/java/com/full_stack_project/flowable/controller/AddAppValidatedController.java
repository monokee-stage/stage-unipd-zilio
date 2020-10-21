package com.full_stack_project.flowable.controller;

import com.full_stack_project.flowable.modal.AddAppToUser;
import com.full_stack_project.flowable.modal.AddAppValidated;
import com.full_stack_project.flowable.repository.AddAppValidatedRepository;
import com.full_stack_project.flowable.repository.AppRepository;
import liquibase.pro.packaged.A;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.Task;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import process_engine.src.main.java.ParseFileJson;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddAppValidatedController {
    @Autowired
    private AddAppValidatedRepository addAppValidatedRepository;

    @PostMapping("/addAppValidated/{username}&{app_name}&{validator}")
    public void insertAppValidated(@PathVariable String username, @PathVariable String app_name, @PathVariable String validator) throws Exception {
        addAppValidatedRepository.insertWithQueryToAppValidated(new AddAppValidated(username, app_name, validator));
        AppController appController = new AppController();
        appController.ProcessRequestUser(username);
    }
}
