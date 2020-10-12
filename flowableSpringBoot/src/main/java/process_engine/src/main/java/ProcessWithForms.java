package process_engine.src.main.java;

import com.full_stack_project.flowable.controller.AppController;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FormProperty;
import org.flowable.bpmn.model.Process;
import org.flowable.common.engine.impl.EngineConfigurator;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.*;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.impl.cmd.CompleteTaskWithFormCmd;
import org.flowable.engine.impl.form.FormEngine;
import org.flowable.engine.impl.form.JuelFormEngine;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.form.api.FormDefinition;
import org.flowable.form.api.FormEngineConfigurationApi;
import org.flowable.form.api.FormManagementService;
import org.flowable.form.api.FormRepositoryService;
import org.flowable.task.api.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ProcessWithForms {
    public static void main(String[] args) throws Exception {
        // get the Bpmn model from the source file
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

        Scanner scanner= new Scanner(System.in);
        System.out.println("Username: ");
        String initiator = scanner.nextLine();

        Map<String, Object> variables = new HashMap<>();

        runtimeService.startProcessInstanceByKey(processDefinition.getKey(), variables);

        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().active().taskUnassigned().list();

        System.out.println("You have " + tasks.size() + " tasks:");

        AppController appController = new AppController();
        System.out.println(appController.getLastValue());

        for (int i=0; i<tasks.size(); i++) {
            if(tasks.get(i).getAssignee() == null) {
                taskService.setAssignee(tasks.get(i).getId(), initiator);
                tasks.get(i).setAssignee(initiator);
            }
            System.out.println((i+1) + ") " + tasks.get(i).getName() + " assignee: " + tasks.get(i).getAssignee() +
                    " form key: "+ tasks.get(i).getFormKey());
            String choice = "";
            if(!tasks.get(i).getFormKey().isEmpty()){
                //System.out.println(taskService.getTaskFormModel(tasks.get(i).getId()));
                System.out.println("Choice: ");
                choice = scanner.nextLine();
                if(tasks.get(i).getFormKey().equals("chooseAppForm")) {
                    variables.put("chooseapp", choice);
                } else {
                    variables.put("optionVariable", choice);
                }
            }
            System.out.println("Which task would you like to complete?");
            int taskIndex = Integer.valueOf(scanner.nextLine());
            Task task = tasks.get(taskIndex - 1);
            Map<String, Object> processVariables = taskService.getVariables(task.getId());
            System.out.println(processVariables.get("initiator") + " wants to complete this task and his choice is " + choice);

            taskService.complete(task.getId(), variables);
        }
    }
}
