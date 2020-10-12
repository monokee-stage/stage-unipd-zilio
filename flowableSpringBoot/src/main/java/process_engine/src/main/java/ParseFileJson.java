package process_engine.src.main.java;

import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import process_engine.src.main.java.Converter;

import java.io.FileReader;
import java.util.*;

public class ParseFileJson extends ProcessConfig {

    public static String file = "newJSONTest.json";

    public static Process process;
    public static SubProcess subProcess;
    public static Signal signal;
    public static StartEvent startEvent;
    public static EndEvent endEvent;
    public static SequenceFlow sequenceFlow;
    public static UserTask userTask;
    public static ServiceTask serviceTask;
    public static ScriptTask scriptTask;
    public static ExclusiveGateway exclusiveGateway;
    public static BpmnModel bpmnModel = new BpmnModel();

    public ParseFileJson() throws Exception{
        readAndSetFlowElements(getFileDefinitions());
        setBpmnModel();
    }

    public static void main(String[] args) throws Exception{
        ParseFileJson parseFileJson = new ParseFileJson();
        Converter converter = new Converter();
        converter.convertBpmnToXML(bpmnModel);
    }

    public void printBpmnModel(){
        System.out.println("\nBpmnModel: " + bpmnModel.getTargetNamespace());
        // get the elementID of the bpmnModel
        Collection<FlowElement> elements = process.getFlowElements();
        for (FlowElement element : elements) {
            System.out.println(element.getId() + " " + element.getName());
            //String id = element.getId();
            //element.setId(id + "_1");
        }
    }

    public void readAndSetFlowElements(JSONObject start) throws Exception{
        if(start.containsKey("signal"))
            setSignalAttributes(start);

        JSONObject p = getFileProcess();
        setProcessAttributes(p);
        setFlowElements(p);
    }

    public void setFlowElements(JSONObject start) throws Exception{
        if(start.containsKey("startEvent")) {
            if (start.get("startEvent").getClass().toString().equals("class org.json.simple.JSONObject")) {
                JSONObject se = getStartEvent(start);
                setStartEventAttributes(se);
            } else {
                org.json.simple.JSONArray se = getStartEventAsArray(start);
                setStartEventAttributes(se);
            }
        }

        if(start.containsKey("userTask")) {
            if (start.get("userTask").getClass().toString().equals("class org.json.simple.JSONObject")) {
                JSONObject ut = getFileUserTaskAsObject(start);
                setUserTaskAttributes(ut);
            } else {
                org.json.simple.JSONArray ut = getFileUserTaskAsArray(start);
                setUserTaskAttributes(ut);
            }
        }

        if(start.containsKey("serviceTask")) {
            if (start.get("serviceTask").getClass().toString().equals("class org.json.simple.JSONObject")) {
                JSONObject st = getFileServiceTaskAsObject(start);
                setServiceTaskAttributes(st);
            } else {
                org.json.simple.JSONArray st = getFileServiceTaskAsArray(start);
                setServiceTaskAttributes(st);
            }
        }

        if(start.containsKey("scriptTask")) {
            if (start.get("scriptTask").getClass().toString().equals("class org.json.simple.JSONObject")) {
                JSONObject st = getFileScriptTask(start);
                setScriptTaskAttributes(st);
            } else {
                org.json.simple.JSONArray st = getFileScriptTaskAsArray(start);
                setScriptTaskAttributes(st);
            }
        }

        if(start.containsKey("sequenceFlow")) {
            org.json.simple.JSONArray sf = getFileSequenceFlowAsArray(start);
            setSequenceFlowAttributes(sf);
        }

        if(start.containsKey("exclusiveGateway")) {
            if (start.get("exclusiveGateway").getClass().toString().equals("class org.json.simple.JSONObject")) {
                JSONObject eg = getFileExclusiveGateway(start);
                setExclusiveGatewayAttributes(eg);
            } else {
                org.json.simple.JSONArray eg = getFileExclusiveGatewayAsArray(start);
                setExclusiveGatewayAttributes(eg);
            }
        }

        if(start.containsKey("endEvent")) {
            if (start.get("endEvent").getClass().toString().equals("class org.json.simple.JSONObject")) {
                JSONObject ee = getEndEvent(start);
                setEndEventAttributes(ee);
            } else {
                org.json.simple.JSONArray ee = getEndEventAsArray(start);
                setEndEventAttributes(ee);
            }
        }

        if(start.containsKey("subProcess")){
            if (start.get("subProcess").getClass().toString().equals("class org.json.simple.JSONObject")) {
                JSONObject eg = getSubProcess(start);
                setSubProcessAttributes(eg);
            }
        }
    }

    public void setFlowElementsSubProcess(JSONObject start) throws Exception {
        if (start.containsKey("startEvent")) {
            if (start.get("startEvent").getClass().toString().equals("class org.json.simple.JSONObject")) {
                JSONObject se = getStartEvent(start);
                setStartEventAttributes(se);
            } else {
                org.json.simple.JSONArray se = getStartEventAsArray(start);
                setStartEventAttributes(se);
            }
        }

        if (start.containsKey("userTask")) {
            if (start.get("userTask").getClass().toString().equals("class org.json.simple.JSONObject")) {
                JSONObject ut = getFileUserTaskAsObject(start);
                setUserTaskAttributes(ut);
            } else {
                org.json.simple.JSONArray ut = getFileUserTaskAsArray(start);
                setUserTaskAttributes(ut);
            }
        }

        if (start.containsKey("serviceTask")) {
            if (start.get("serviceTask").getClass().toString().equals("class org.json.simple.JSONObject")) {
                JSONObject st = getFileServiceTaskAsObject(start);
                setServiceTaskAttributes(st);
            } else {
                org.json.simple.JSONArray st = getFileServiceTaskAsArray(start);
                setServiceTaskAttributes(st);
            }
        }

        if (start.containsKey("scriptTask")) {
            if (start.get("scriptTask").getClass().toString().equals("class org.json.simple.JSONObject")) {
                JSONObject st = getFileScriptTask(start);
                setScriptTaskAttributes(st);
            } else {
                org.json.simple.JSONArray st = getFileScriptTaskAsArray(start);
                setScriptTaskAttributes(st);
            }
        }

        if (start.containsKey("sequenceFlow")) {
            org.json.simple.JSONArray sf = getFileSequenceFlowAsArray(start);
            setSequenceFlowAttributes(sf);
        }

        if (start.containsKey("exclusiveGateway")) {
            if (start.get("exclusiveGateway").getClass().toString().equals("class org.json.simple.JSONObject")) {
                JSONObject eg = getFileExclusiveGateway(start);
                setExclusiveGatewayAttributes(eg);
            } else {
                org.json.simple.JSONArray eg = getFileExclusiveGatewayAsArray(start);
                setExclusiveGatewayAttributes(eg);
            }
        }

        if (start.containsKey("endEvent")) {
            if (start.get("endEvent").getClass().toString().equals("class org.json.simple.JSONObject")) {
                JSONObject ee = getEndEvent(start);
                setEndEventAttributes(ee);
            } else {
                org.json.simple.JSONArray ee = getEndEventAsArray(start);
                setEndEventAttributes(ee);
            }
        }
    }

    // GETTER
    public Process getProcess(){
        return process;
    }

    public BpmnModel getBpmnModel(){
        return bpmnModel;
    }

    public JSONObject getFileDefinitions() throws Exception{
        Object obj = new JSONParser().parse(new FileReader(file));

        // typecasting obj to JSONObject
        JSONObject jo = (JSONObject) obj;

        // getting definitions
        JSONObject def = (JSONObject) jo.get("definitions");

        if(def.containsKey("targetNamespace"))
            bpmnModel.setTargetNamespace(def.get("targetNamespace").toString());
        return def;
    }

    public JSONObject getFileProcess() throws Exception{
        JSONObject process = (JSONObject) getFileDefinitions().get("process");
        return process;
    }

    public List<FlowableListener> getProcessExecutionListener(JSONObject ee){
        List<FlowableListener> el = new ArrayList<>();
        if(ee.containsKey("flowable:executionListener") && ee.get("flowable:executionListener").getClass().toString().equals("class org.json.simple.JSONObject")) {
            org.json.simple.JSONObject executionListener = (org.json.simple.JSONObject) ee.get("flowable:executionListener");
            FlowableListener fl = new FlowableListener();
            fl.setEvent(executionListener.get("event").toString());
            fl.setImplementation(executionListener.get("class").toString());
            fl.setImplementationType("class");
            el.add(fl);
        }else if (ee.containsKey("flowable:executionListener") && ee.get("flowable:executionListener").getClass().toString().equals("class org.json.simple.JSONArray")) {
            org.json.simple.JSONArray executionListener = (org.json.simple.JSONArray) ee.get("flowable:executionListener");
            for(int i=0; i<executionListener.size(); i++){
                FlowableListener fl = new FlowableListener();
                JSONObject jsonObject = (JSONObject) executionListener.get(i);
                fl.setEvent(jsonObject.get("event").toString());
                fl.setImplementation(jsonObject.get("class").toString());
                fl.setImplementationType("class");
                el.add(fl);
            }
        }
        //p.setExecutionListeners(el);
        return el;
    }

    public JSONObject getSignal() throws Exception{
        JSONObject signal = (JSONObject) getFileDefinitions().get("signal");
        return signal;
    }

    public JSONObject getSubProcess(JSONObject start) throws Exception{
        JSONObject subProcess = (JSONObject) start.get("subProcess");
        return subProcess;
    }

    public JSONObject getStartEvent(JSONObject start) throws Exception{
        JSONObject startEvent = (JSONObject) start.get("startEvent");
        return startEvent;
    }

    public JSONArray getStartEventAsArray(JSONObject start) throws Exception{
        JSONArray startEvent = (JSONArray) start.get("startEvent");
        return startEvent;
    }

    public JSONObject getEndEvent(JSONObject start) throws Exception{
        JSONObject endEvent = (JSONObject) start.get("endEvent");
        return endEvent;
    }

    public org.json.simple.JSONArray getEndEventAsArray(JSONObject start) throws Exception{
        org.json.simple.JSONArray endEvent = (org.json.simple.JSONArray) start.get("endEvent");
        return endEvent;
    }

    public JSONObject getFileUserTaskAsObject(JSONObject start) throws Exception{
        JSONObject userTask = (JSONObject) start.get("userTask");
        return userTask;
    }

    public org.json.simple.JSONArray getFileUserTaskAsArray(JSONObject start) throws Exception{
        org.json.simple.JSONArray userTask = (org.json.simple.JSONArray) start.get("userTask");
        return userTask;
    }

    public List<FormProperty> getUserTaskFormProperty(JSONObject ee) {
        List<FormProperty> list = new ArrayList<>();
        if (ee.containsKey("flowable:formProperty") && ee.get("flowable:formProperty").getClass().toString().equals("class org.json.simple.JSONArray")) {
            org.json.simple.JSONArray formProperty = (org.json.simple.JSONArray) ee.get("flowable:formProperty");
            for (int i = 0; i < formProperty.size(); i++) {
                FormProperty fp = new FormProperty();
                JSONObject property = (JSONObject) formProperty.get(i);
                fp.setName(property.get("name").toString());
                fp.setId(property.get("id").toString());
                fp.setType(property.get("type").toString());
                if(property.containsKey("required"))
                    fp.setRequired(property.get("required").toString().equals("true"));
                if(property.containsKey("variable"))
                    fp.setVariable(property.get("variable").toString());
                if(property.containsKey("writable"))
                    fp.setWriteable(property.get("writable").toString().equals("true"));
                list.add(fp);
            }
            //userTask.setFormProperties(list);
        } else if(ee.containsKey("flowable:formProperty") && ee.get("flowable:formProperty").getClass().toString().equals("class org.json.simple.JSONObject")){
            org.json.simple.JSONObject formProperty = (org.json.simple.JSONObject) ee.get("flowable:formProperty");
            FormProperty fp = new FormProperty();
            fp.setName(formProperty.get("name").toString());
            fp.setId(formProperty.get("id").toString());
            fp.setType(formProperty.get("type").toString());
            if(formProperty.containsKey("required"))
                fp.setRequired(formProperty.get("required").toString().equals("true"));
            if(formProperty.containsKey("variable"))
                fp.setVariable(formProperty.get("variable").toString());
            if(formProperty.containsKey("writable"))
                fp.setWriteable(formProperty.get("writable").toString().equals("true"));
            list.add(fp);
        }
        return list;
    }

    public JSONObject getFileServiceTaskAsObject(JSONObject start) throws Exception{
        JSONObject serviceTask = (JSONObject) start.get("serviceTask");
        return serviceTask;
    }

    public JSONArray getFileServiceTaskAsArray(JSONObject start) throws Exception{
        JSONArray serviceTask = (JSONArray) start.get("serviceTask");
        return serviceTask;
    }

    public JSONObject getFileScriptTask(JSONObject start) throws Exception{
        JSONObject scriptTask = (JSONObject) start.get("scriptTask");
        return scriptTask;
    }

    public JSONArray getFileScriptTaskAsArray(JSONObject start) throws Exception{
        JSONArray scriptTask = (JSONArray) start.get("scriptTask");
        return scriptTask;
    }

    public org.json.simple.JSONArray getFileSequenceFlowAsArray(JSONObject start) throws Exception{
        org.json.simple.JSONArray sequenceFlow = (org.json.simple.JSONArray) start.get("sequenceFlow");
        return sequenceFlow;
    }

    public JSONObject getFileExclusiveGateway(JSONObject start) throws Exception{
        JSONObject eg = (JSONObject) start.get("exclusiveGateway");
        return eg;
    }

    public JSONArray getFileExclusiveGatewayAsArray(JSONObject start) throws Exception{
        org.json.simple.JSONArray exclusiveGateway = (org.json.simple.JSONArray) start.get("exclusiveGateway");
        return exclusiveGateway;
    }

    // SETTER
    public void setBpmnModel(){
        // add the process to the bpmnModel
        //bpmnModel = new BpmnModel();
        bpmnModel.addSignal(signal);
        bpmnModel.addProcess(process);
        bpmnModel.getMainProcess();
        bpmnModel.setTargetNamespace("JSONParser-bpmnModel");
    }

    public void setSignalAttributes(JSONObject s){
        JSONObject sg = (JSONObject) s.get("signal");
        signal = new Signal();
        signal.setId(sg.get("id").toString());
        signal.setName(sg.get("name").toString());
        signal.setScope(sg.get("flowable:scope").toString());
    }

    public void setProcessAttributes(JSONObject p){
        process = new Process();
        process.setId(p.get("id").toString());
        process.setName(p.get("name").toString());
        if(p.get("isExecutable").toString().equals("true"))
            process.isExecutable();
        if(p.containsKey("documentation"))
            process.setDocumentation(p.get("documentation").toString());
        if(p.containsKey("extensionElements")) {
            JSONObject extElem = (JSONObject) p.get("extensionElements");
            List<FlowableListener> fl = getProcessExecutionListener(extElem);
            process.setExecutionListeners(fl);
        }
    }

    public void setSubProcessAttributes(JSONObject sp) throws Exception{
        subProcess = new SubProcess();
        subProcess.setId(sp.get("id").toString());
        subProcess.setName(sp.get("name").toString());
        if(sp.containsKey("multiInstanceLoopCharacteristics")){
            JSONObject multi = (JSONObject) sp.get("multiInstanceLoopCharacteristics");
            subProcess.setLoopCharacteristics(setMultiInstanceLoopCharacteristics(multi));
        }
        process.addFlowElement(subProcess);
        setFlowElementsSubProcess(sp);
    }

    public void setStartEventAttributes(JSONObject se){
        startEvent = new StartEvent();
        startEvent.setId(se.get("id").toString());
        if(se.containsKey("name"))
            startEvent.setName(se.get("name").toString());
        if(se.containsKey("flowable:formFieldValidation"))
            startEvent.setValidateFormFields(se.get("flowable:formFieldValidation").toString());
        if(se.containsKey("isInterrupting")) {
            startEvent.setInterrupting(se.get("isInterrupting").toString().equals("true"));
            if(startEvent.isInterrupting() == true)
                startEvent.isInterrupting();
        }
        if(se.containsKey("signalEventDefinition")){
            JSONObject sref = (JSONObject) se.get("signalEventDefinition");
            SignalEventDefinition signalEventDefinition = new SignalEventDefinition();
            signalEventDefinition.setSignalRef(sref.get("signalRef").toString());
            startEvent.addEventDefinition(signalEventDefinition);
        }
        if(process != null && subProcess == null)
            process.addFlowElement(startEvent);
        else if(process != null && subProcess != null)
            subProcess.addFlowElement(startEvent);
    }

    public void setStartEventAttributes(JSONArray se){
        for(int i=0; i<se.size(); i++){
            JSONObject s = (JSONObject) se.get(i);
            setStartEventAttributes(s);
        }
    }

    public void setEndEventAttributes(JSONObject ee){
        endEvent = new EndEvent();
        endEvent.setId(ee.get("id").toString());
        if(ee.containsKey("name"))
            endEvent.setName(ee.get("name").toString());
        if(process != null && subProcess == null)
            process.addFlowElement(endEvent);
        else if(process != null && subProcess != null)
            subProcess.addFlowElement(endEvent);
    }

    public void setEndEventAttributes(org.json.simple.JSONArray ee){
        for(int i=0; i<ee.size(); i++) {
            JSONObject eeAttributes = (JSONObject) ee.get(i);
            setEndEventAttributes(eeAttributes);
        }
    }

    public void setUserTaskAttributes(JSONObject utO){
        userTask = new UserTask();
        userTask.setId(utO.get("id").toString());
        userTask.setName(utO.get("name").toString());
        if(utO.containsKey("flowable:assignee"))
            userTask.setAssignee(utO.get("flowable:assignee").toString());
        if(utO.containsKey("flowable:formKey"))
            userTask.setFormKey(utO.get("flowable:formKey").toString());
        if(utO.containsKey("extensionElements")){
            JSONObject ee = (JSONObject) utO.get("extensionElements");
            List<FormProperty> properties = getUserTaskFormProperty(ee);
            userTask.setFormProperties(properties);
            if(ee.containsKey("flowable:taskListener")) {
                JSONArray tasks = (JSONArray) ee.get("flowable:taskListener");
                userTask.setTaskListeners(setUserTaskTaskListener(tasks));
            }
        }
        if(utO.containsKey("flowable:formFieldValidation"))
            userTask.setValidateFormFields(utO.get("flowable:formFieldValidation").toString());
        if(utO.containsKey("flowable:async")) {
            userTask.setAsynchronous(utO.get("flowable:async").toString().equals("true"));
            if(utO.get("flowable:async").equals(true))
                userTask.isAsynchronous();
        }
        if(utO.containsKey("flowable:exclusive")) {
            userTask.setExclusive(utO.get("flowable:exclusive").toString().equals("true"));
            if(utO.get("flowable:exclusive").equals(true))
                userTask.isExclusive();
        }
        if(process != null && subProcess == null)
            process.addFlowElement(userTask);
        else if(process != null && subProcess != null)
            subProcess.addFlowElement(userTask);
    }

    public void setUserTaskAttributes(org.json.simple.JSONArray utA){
        for(int i=0; i<utA.size(); i++){
            JSONObject utAttributes = (JSONObject) utA.get(i);
            setUserTaskAttributes(utAttributes);
        }
    }

    public List<FlowableListener> setUserTaskTaskListener(JSONArray tl){
        List<FlowableListener> list = new ArrayList<>();
        for(int i=0; i<tl.size(); i++){
            JSONObject task = (JSONObject) tl.get(i);
            FlowableListener flowableListener = new FlowableListener();
            flowableListener.setEvent(task.get("event").toString());
            flowableListener.setImplementationType("class");
            flowableListener.setImplementation(task.get("class").toString());
            list.add(flowableListener);
        }
        return list;
    }


    public void setServiceTaskAttributes(JSONObject st){
        serviceTask = new ServiceTask();
        serviceTask.setId(st.get("id").toString());
        serviceTask.setName(st.get("name").toString());
        if (st.containsKey("flowable:class")) {
            serviceTask.setImplementationType("class");
            serviceTask.setImplementation(st.get("flowable:class").toString());
        }
        if (st.containsKey("flowable:type"))
            serviceTask.setType(st.get("flowable:type").toString());
        if (st.containsKey("flowable:async"))
            serviceTask.setAsynchronous(st.get("flowable:async").toString().equals("true"));
        setServiceTaskExtensionElements(serviceTask, st);
        if (st.containsKey("multiInstanceLoopCharacteristics")) {
            serviceTask.hasMultiInstanceLoopCharacteristics();
            JSONObject m = (JSONObject) st.get("multiInstanceLoopCharacteristics");
            serviceTask.setLoopCharacteristics(setMultiInstanceLoopCharacteristics(m));
        }
        if(process != null && subProcess == null)
            process.addFlowElement(serviceTask);
        else if(process != null && subProcess != null)
            subProcess.addFlowElement(serviceTask);
    }

    public void setServiceTaskAttributes(org.json.simple.JSONArray st){
        for(int i=0; i<st.size(); i++) {
            JSONObject stAttributes = (JSONObject) st.get(i);
            setServiceTaskAttributes(stAttributes);
        }
    }

    public MultiInstanceLoopCharacteristics setMultiInstanceLoopCharacteristics(JSONObject m){
        //st.hasMultiInstanceLoopCharacteristics();
        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = new MultiInstanceLoopCharacteristics();
        if(m.get("isSequential").toString().equals("true")) {
            multiInstanceLoopCharacteristics.setSequential(true);
            multiInstanceLoopCharacteristics.isSequential();
        }
        if(m.containsKey("flowable:collection"))
            multiInstanceLoopCharacteristics.setInputDataItem(m.get("flowable:collection").toString());
        multiInstanceLoopCharacteristics.setElementVariable(m.get("flowable:elementVariable").toString());
        if(m.containsKey("completionCondition"))
            multiInstanceLoopCharacteristics.setCompletionCondition(m.get("completionCondition").toString());
        //st.setLoopCharacteristics(multiInstanceLoopCharacteristics);
        return multiInstanceLoopCharacteristics;
    }

    public void setServiceTaskExtensionElements(ServiceTask st, JSONObject stO){
        if(stO.containsKey("extensionElements")){
            org.json.simple.JSONObject ee = (org.json.simple.JSONObject) stO.get("extensionElements");
            if(ee.containsKey("flowable:field")) {
                org.json.simple.JSONArray flowableField = (org.json.simple.JSONArray) ee.get("flowable:field");
                List<FieldExtension> fieldExtension = new ArrayList<>();
                for (int i = 0; i < flowableField.size(); i++) {
                    FieldExtension f = new FieldExtension();
                    JSONObject sfAttributes = (JSONObject) flowableField.get(i);
                    f.setFieldName(sfAttributes.get("name").toString());
                    if(sfAttributes.containsKey("flowable:string"))
                        f.setStringValue(sfAttributes.get("flowable:string").toString());
                    if(sfAttributes.containsKey("flowable:expression"))
                        f.setExpression(sfAttributes.get("flowable:expression").toString());
                    //f.setExpression(process.getJSONObject("serviceTask").getJSONObject("extensionElements").getJSONArray("flowable:field").getJSONObject("flowable:string").get(i).toString());
                    System.out.println(f.getFieldName() + ": " + f.getStringValue());
                    fieldExtension.add(f);
                }
                st.setFieldExtensions(fieldExtension);
            }
        }
    }

    public void setScriptTaskAttributes(JSONObject jo){
        scriptTask = new ScriptTask();
        scriptTask.setId(jo.get("id").toString());
        scriptTask.setName(jo.get("name").toString());
        scriptTask.setScriptFormat(jo.get("scriptFormat").toString());
        if(jo.containsKey("flowable:autoStoreVariables"))
            scriptTask.setAutoStoreVariables(jo.get("flowable:autoStoreVariables").toString().equals("true"));
        scriptTask.setScript(jo.get("script").toString());
        if(process != null && subProcess == null)
            process.addFlowElement(scriptTask);
        else if(process != null && subProcess != null)
            subProcess.addFlowElement(scriptTask);
    }

    public void setScriptTaskAttributes(JSONArray ja){
        for(int i=0; i<ja.size(); i++){
            JSONObject jo = (JSONObject) ja.get(i);
            setScriptTaskAttributes(jo);
        }
    }

    public void setSequenceFlowAttributes(org.json.simple.JSONArray sf){
        for(int i=0; i<sf.size(); i++){
            sequenceFlow = new SequenceFlow();
            JSONObject sfAttributes = (JSONObject) sf.get(i);
            sequenceFlow.setId(sfAttributes.get("id").toString());
            if(sfAttributes.containsKey("name"))
                sequenceFlow.setName(sfAttributes.get("name").toString());
            sequenceFlow.setTargetRef(sfAttributes.get("targetRef").toString());
            sequenceFlow.setSourceRef(sfAttributes.get("sourceRef").toString());
            if(sfAttributes.containsKey("conditionExpression")) {
                JSONObject ce = (JSONObject) sfAttributes.get("conditionExpression");
                sequenceFlow.setConditionExpression(ce.get("content").toString());
            }
            if(process != null && subProcess == null)
                process.addFlowElement(sequenceFlow);
            else if(process != null && subProcess != null)
                subProcess.addFlowElement(sequenceFlow);
        }
    }

    public void setExclusiveGatewayAttributes(JSONObject eg){
        exclusiveGateway = new ExclusiveGateway();
        exclusiveGateway.setId(eg.get("id").toString());
        if(process != null && subProcess == null)
            process.addFlowElement(exclusiveGateway);
        else if(process != null && subProcess != null)
            subProcess.addFlowElement(exclusiveGateway);
    }

    public void setExclusiveGatewayAttributes(JSONArray eg){
        for(int i=0; i<eg.size(); i++) {
            JSONObject egO = (JSONObject) eg.get(i);
            setExclusiveGatewayAttributes(egO);
        }
    }

}
