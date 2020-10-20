package com.full_stack_project.flowable.controller;

import com.full_stack_project.flowable.modal.AddAppToUser;
import com.full_stack_project.flowable.modal.App;
import com.full_stack_project.flowable.modal.AppNameOnly;
import com.full_stack_project.flowable.modal.AppNameUserOnly;
import com.full_stack_project.flowable.repository.*;

import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.*;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.bpmn.behavior.MailActivityBehavior;
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
public class AppController {
    private String value;
    @Autowired
    private AppRepository appRepository;
    @Autowired
    private UserAppRepository userAppRepository;
    @Autowired
    private InsertAppToUserRepository insertAppToUserRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private AddAppToUserRepository addAppToUserRepository;

    @PostMapping("/deleteUserApp/{user}&{app}")
    public void deleteApp(@PathVariable String user, @PathVariable String app){
        insertAppToUserRepository.deleteRow(new AddAppToUser(user, app));
    }

    @PostMapping("/addApp/{id}&{app}&{validation}")
    public void insertApp(@PathVariable String id, @PathVariable String app, @PathVariable int validation) {
        insertAppToUserRepository.insertWithQuery(new AddAppToUser(id, app, validation));
    }

    @GetMapping("/countRequest")
    public int countRequest(){
        return appRepository.countRequest();
    }

    @GetMapping("/getAllRequest")
    public List<AddAppToUser> getAllRequest(){
        return addAppToUserRepository.getAllRequest();
    }

    @GetMapping("/choiceValidationForm/{val}&{user}")
    public void validation(@PathVariable String[] val, @PathVariable String user) throws Exception {
        //System.out.println(val);
        ProcessValidation(val, user);
    }

    @GetMapping("/appsName")
    public List<AppNameOnly> getAllAppsName() {
        return appRepository.findAllAppsNames();
    }

    @GetMapping("/app/{name}")
    public Optional<App> getApp(@PathVariable String name) {
        return appRepository.findById(name);
    }

    @GetMapping("/getLastAppUserNoProcess/{user}")
    public AppNameUserOnly getLastAppUser(@PathVariable String user) throws Exception {
        value = Objects.requireNonNull(appRepository.findUserApps(user).stream().reduce((first, second) -> second).orElse(null)).getValue();
        return appRepository.findUserApps(user).stream().reduce((first, second) -> second).orElse(null);
    }

    @GetMapping("/getLastAppUser/{user}")
    public AppNameUserOnly getLastApp(@PathVariable String user) throws Exception {
        value = Objects.requireNonNull(appRepository.findUserApps(user).stream().reduce((first, second) -> second).orElse(null)).getValue();
        Process(value, user);
        return appRepository.findUserApps(user).stream().reduce((first, second) -> second).orElse(null);
    }

    /*@GetMapping("/getLastAppUserTest/{user}")
    public void getLastAppTest(@PathVariable String user) throws Exception {
        value = appRepository.getLastAppUserValue(user);
        System.out.println(value);
        Process(value, user);
    }*/

    @GetMapping("/getIfNeedValidation/{name}")
    public boolean needValidation(@PathVariable String name) {
        if(appRepository.findIfNeedValidation(name) == 1)
            return true;
        else
            return false;
    }

    public String getLastValue(){
        return value;
    }

    @GetMapping("/getAppUser/{id}")
    public List<AppNameUserOnly> getValue(@PathVariable String id) {
        return appRepository.findUserApps(id);
    }


    public void Process(String value, String id) throws Exception {
        System.out.println(value);

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

        //RuntimeService runtimeService = processEngine.getRuntimeService();
        //runtimeService.startProcessInstanceById(processDefinition.getKey());


        Scanner scanner = new Scanner(System.in);
       /* System.out.println("Username: ");
        String initiator = scanner.nextLine();*/

        Map<String, Object> variables = new HashMap<>();

        //runtimeService.startProcessInstanceByKey(processDefinition.getKey(), variables);

        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateOrAssigned(id).active().list();

        System.out.println("You have " + tasks.size() + " tasks:");

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(tasks.get(i).getId());

            if (tasks.get(i).getAssignee() == null) {
                taskService.setAssignee(tasks.get(i).getId(), id);
                tasks.get(i).setAssignee(id);
            }
            System.out.println((i + 1) + ") " + tasks.get(i).getName() + " assignee: " + tasks.get(i).getAssignee() +
                    " form key: " + tasks.get(i).getFormKey());

            if (!tasks.get(i).getFormKey().isEmpty()) {
                //System.out.println(taskService.getTaskFormModel(tasks.get(i).getId()));

                System.out.println("Choice: " + value);

                if (tasks.get(i).getFormKey().equals("chooseAppForm")) {
                    variables.put("chooseapp", value);
                } else {
                    variables.put("optionVariable", value);
                }
            }
            //taskRepository.getTaskByID(tasks.get(i).getId());
            taskService.complete(tasks.get(i).getId(), variables);
        }

        taskService = processEngine.getTaskService();
        tasks = taskService.createTaskQuery().taskUnassigned().active().list();
        for (int i=0; i<tasks.size(); i++) {
            if (tasks.get(i).getAssignee() == null) {
                taskService.setAssignee(tasks.get(i).getId(), id);
                tasks.get(i).setAssignee(id);
            }
        }
/*
        Task t = taskService.createTaskQuery().active().singleResult();
        taskRepository.getTaskByID(t.getId());*/
    }

    public void ProcessValidation(String[] value, String id) throws Exception {
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

        //RuntimeService runtimeService = processEngine.getRuntimeService();
        //runtimeService.startProcessInstanceById(processDefinition.getKey());


        Scanner scanner = new Scanner(System.in);
       /* System.out.println("Username: ");
        String initiator = scanner.nextLine();*/

        Map<String, Object> variables = new HashMap<>();

        //runtimeService.startProcessInstanceByKey(processDefinition.getKey(), variables);

        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateOrAssigned(id).active().list();

        System.out.println("You have " + tasks.size() + " tasks:");

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(tasks.get(i).getId());

            if (tasks.get(i).getAssignee() == null) {
                taskService.setAssignee(tasks.get(i).getId(), id);
                tasks.get(i).setAssignee(id);
            }
            System.out.println((i + 1) + ") " + tasks.get(i).getName() + " assignee: " + tasks.get(i).getAssignee() +
                    " form key: " + tasks.get(i).getFormKey());

            if (!tasks.get(i).getFormKey().isEmpty()) {
                //System.out.println(taskService.getTaskFormModel(tasks.get(i).getId()));

                System.out.println("Choice: " + value[i]);

                variables.put("optionVariable", value[i]);
            }
            //taskRepository.getTaskByID(tasks.get(i).getId());
            taskService.complete(tasks.get(i).getId(), variables);
        }
        taskService = processEngine.getTaskService();
        tasks = taskService.createTaskQuery().taskUnassigned().active().list();
        for (int i=0; i<tasks.size(); i++) {
            if (tasks.get(i).getAssignee() == null) {
                taskService.setAssignee(tasks.get(i).getId(), id);
                tasks.get(i).setAssignee(id);
            }
        }
/*
        Task t = taskService.createTaskQuery().active().singleResult();
        taskRepository.getTaskByID(t.getId());*/
    }

    public void ProcessRequest(String id, String[] simpleUser) throws Exception {
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

        //RuntimeService runtimeService = processEngine.getRuntimeService();

        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateOrAssigned(id).active().list();

        //set the following task to the user
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getAssignee() == null) {
                taskService.setAssignee(tasks.get(i).getId(), id);
                tasks.get(i).setAssignee(id);
            }
            System.out.println((i + 1) + ") " + tasks.get(i).getName() + " assignee: " + tasks.get(i).getAssignee() +
                    " form key: " + tasks.get(i).getFormKey());

            taskService.complete(tasks.get(i).getId());

            taskService = processEngine.getTaskService();

            Task taskU = taskService.createTaskQuery().taskUnassigned().active().singleResult();

            if (taskU.getAssignee() == null) {
                taskService.setAssignee(taskU.getId(), simpleUser[i]);
                taskU.setAssignee(simpleUser[i]);
            }
        }
        /*
        //set the following task to the user
        taskService = processEngine.getTaskService();
        tasks = taskService.createTaskQuery().taskUnassigned().active().list();

        for (int i=0; i<tasks.size(); i++) {
            if (tasks.get(i).getAssignee() == null) {
                taskService.setAssignee(tasks.get(i).getId(), simpleUser);
                tasks.get(i).setAssignee(simpleUser);
            }
        }*/

    }


    public void ProcessRequestUser(String user) throws Exception {
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

        //RuntimeService runtimeService = processEngine.getRuntimeService();

        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateOrAssigned(user).active().list();

        System.out.println("You have " + tasks.size() + " tasks:");

        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getAssignee() == null) {
                taskService.setAssignee(tasks.get(i).getId(), user);
                tasks.get(i).setAssignee(user);
            }
            System.out.println((i + 1) + ") " + tasks.get(i).getName() + " assignee: " + tasks.get(i).getAssignee() +
                    " form key: " + tasks.get(i).getFormKey());

            taskService.complete(tasks.get(i).getId());
        }
    }
}
