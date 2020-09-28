import java.util.*;

import org.flowable.bpmn.model.*;

import org.flowable.bpmn.model.Process;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

public class ProcessConfig {

  public static void main(String[] args) throws Exception {
    // get the Bpmn model from the source file
    ParseFileJson parseFileJson = new ParseFileJson();
    Process process = parseFileJson.getProcess();
    BpmnModel bpmnModel = parseFileJson.getBpmnModel();

    // here just to print the bpmn model
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
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey());

    TaskService taskService = processEngine.getTaskService();

    List<Task> tasks = taskService.createTaskQuery().list();

    int i=tasks.size();

    taskService.complete(tasks.get(i-1).getId());

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