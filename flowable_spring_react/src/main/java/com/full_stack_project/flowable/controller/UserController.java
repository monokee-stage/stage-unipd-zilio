package com.full_stack_project.flowable.controller;

import com.full_stack_project.flowable.modal.User;
import com.full_stack_project.flowable.repository.UserRepository;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.*;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import process_engine.src.main.java.ParseFileJson;

import java.util.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/user/{id}")
    public Optional<User> getUser(@PathVariable String id) {
        return userRepository.findById(id);
    }

    @PostMapping("/userRequiredAccess/{id}")
    public String userRequireAccess(@PathVariable String id) throws Exception {
        ProcessRequestAccess(id);
        return id;
    }

   public void ProcessRequestAccess(String id) throws Exception {
        ParseFileJson parseFileJson = new ParseFileJson();
        Process process = parseFileJson.getProcess();
        BpmnModel bpmnModel = parseFileJson.getBpmnModel();

        // DB configuration parameters
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:mysql://mysql:3306/flowable?characterEncoding=UTF-8")
                .setJdbcUsername("root")
                .setJdbcPassword("root")
                .setJdbcDriver("com.mysql.cj.jdbc.Driver")
                .setMailServerHost("smtp.gmail.com")
                .setMailServerUsername("atlearningteam@gmail.com")
                .setMailServerPassword("plutobauco")
                .setMailServerPort(587)
                .setMailServerUseTLS(true)
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine processEngine = cfg.buildProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addBpmnModel("test.bpmn20.xml", bpmnModel)
                .deploy();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();

        RuntimeService runtimeService = processEngine.getRuntimeService();

        runtimeService.startProcessInstanceByKey(processDefinition.getKey());

        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskUnassigned().active().list();

        System.out.println("You have " + tasks.size() + " tasks:");

        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getAssignee() == null) {
                taskService.setAssignee(tasks.get(i).getId(), id);
                tasks.get(i).setAssignee(id);
            }
            System.out.println((i + 1) + ") " + tasks.get(i).getName() + " assignee: " + tasks.get(i).getAssignee() +
                    " form key: " + tasks.get(i).getFormKey());
            /*
            Scanner scanner = new Scanner(System.in);
            System.out.println("Which task would you like to complete?");
            int taskIndex = Integer.valueOf(scanner.nextLine());
            Task task = tasks.get(taskIndex - 1);
            taskService.complete(task.getId());*/
            taskService.complete(tasks.get(i).getId());
        }
        //set the following tasks
       taskService = processEngine.getTaskService();
       tasks = taskService.createTaskQuery().taskUnassigned().active().list();
       for (int i = 0; i < tasks.size(); i++) {
           if (tasks.get(i).getAssignee() == null) {
               taskService.setAssignee(tasks.get(i).getId(), id);
               tasks.get(i).setAssignee(id);
           }
       }
    }
}
