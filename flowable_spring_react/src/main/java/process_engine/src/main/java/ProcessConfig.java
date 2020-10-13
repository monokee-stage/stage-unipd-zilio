package process_engine.src.main.java;

import java.util.*;
import java.util.logging.Logger;

import org.flowable.bpmn.model.*;

import org.flowable.bpmn.model.Process;
import org.flowable.engine.*;
import org.flowable.engine.form.FormProperty;
import org.flowable.engine.form.StartFormData;
import org.flowable.engine.form.TaskFormData;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.bpmn.helper.ClassDelegate;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.impl.form.FormEngine;
import org.flowable.engine.impl.form.JuelFormEngine;
import org.flowable.engine.impl.form.TaskFormDataImpl;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.form.api.FormRepositoryService;
import org.flowable.task.api.Task;
import org.flowable.task.service.delegate.TaskListener;

public class ProcessConfig {

  public static void main(String[] args) throws Exception {
    // get the Bpmn model from the source file
    ParseFileJson parseFileJson = new ParseFileJson();
    Process process = parseFileJson.getProcess();
    BpmnModel bpmnModel = parseFileJson.getBpmnModel();

    // print the bpmn model and create the new XML file
    Converter converter = new Converter();
    converter.convertBpmnToXML(bpmnModel);

    System.out.print("\nBPMN model: ");
    System.out.println(bpmnModel.getTargetNamespace());

    Collection<FlowElement> elements = process.getFlowElements();

    for (FlowElement element : elements)
      System.out.println(element.getId());

    // DB configuration parameters
    ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
            .setJdbcUrl("jdbc:mysql://mysql:3306/flowable?characterEncoding=UTF-8")
            .setJdbcUsername("root")
            .setJdbcPassword("root")
            .setJdbcDriver("com.mysql.cj.jdbc.Driver")
            .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
    ProcessEngine processEngine = cfg.buildProcessEngine();

    RepositoryService repositoryService = processEngine.getRepositoryService();
    Deployment deployment = repositoryService.createDeployment()
            .addBpmnModel("test.bpmn20.xml", bpmnModel)
            .deploy();

    ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
            .deploymentId(deployment.getId())
            .singleResult();

    System.out.println("Found process definition : " + processDefinition.getName() + " " + processDefinition.getKey());

    RuntimeService runtimeService = processEngine.getRuntimeService();

    Scanner scanner= new Scanner(System.in);
    /*
    System.out.println("Identity yourself: ");
    String initiator = scanner.nextLine();

    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("INITIATOR", initiator);*/

    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey());

    TaskService taskService = processEngine.getTaskService();

    List<Task> tasks = taskService.createTaskQuery().list();

    FormEngine formEngine = new JuelFormEngine();
    FormService formService = processEngine.getFormService();

    StartFormData startFormData = formService.getStartFormData(processDefinition.getId());

    formEngine.renderStartForm(startFormData);

    for (int i = tasks.size()-1; i > tasks.size()-3; i--) {
      System.out.println((i + 1) + ") " + tasks.get(i).getId());
      //TaskFormData taskFormData = processEngine.getFormService().getTaskFormData(tasks.get(i).getId());
      TaskFormData taskFormData = formService.getTaskFormData(tasks.get(i).getId());
      formEngine.renderTaskForm(taskFormData);
      //taskService.complete(tasks.get(i).getId());
    }

    HistoryService historyService = processEngine.getHistoryService();
    List<HistoricActivityInstance> activities =
            historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(processInstance.getId())
                    .finished()
                    .orderByHistoricActivityInstanceEndTime().asc()
                    .list();

    for (HistoricActivityInstance activity : activities) {
      System.out.println(activity.getActivityId() + " took "
              + activity.getDurationInMillis() + " milliseconds");
    }
  }
}