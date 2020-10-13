package process_engine.src.main.java;

import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.json.JSONObject;
import org.json.XML;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Converter {

    public static void main(String[] args) throws Exception{
        Converter converter = new Converter();
        converter.newJSONFile("C:\\Users\\Davide\\Desktop\\requestAccess.bpmn20.xml");
    }

    // convert XML string to JSON string
    public String convertXMLToJSON(String fileName) throws Exception {
        JSONObject xmlJSONObj = XML.toJSONObject(new FileReader(fileName));
        return xmlJSONObj.toString(4);
    }

    // convert JSON file to XML string
    public String convertJSONtoXML(String fileName) throws Exception{
        String jsonStr = new String(Files.readAllBytes(Paths.get(fileName)));
        JSONObject json = new JSONObject(jsonStr);
        return XML.toString(json);
    }

    // convert Bpmn model to XML file
    public File convertBpmnToXML(BpmnModel bpmnModel) throws Exception{
        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
        byte[] xmlBytes = bpmnXMLConverter.convertToXML(bpmnModel);
        String reconverted = new String(xmlBytes, StandardCharsets.UTF_8);
        File newFile = new File("C:\\Users\\Davide\\Desktop\\newXMLTest.bpmn20.xml");
        FileWriter writer = new FileWriter(newFile, false);
        writer.write(reconverted);
        writer.close();
        return newFile;
    }

    // to create new XML file "newXMLTest.xml"
    public File newXMLFile(String fileName) throws Exception{
        String xml = convertJSONtoXML(fileName);
        File newFile = new File("C:\\Users\\Davide\\Desktop\\rpova.bpmn20.xml");
        FileWriter writer = new FileWriter(newFile, false);
        writer.write(xml);
        writer.close();
        return newFile;
    }

    // to create new JSON file "newJSONTest.json""
    public File newJSONFile(String fileName) throws Exception{
        String json = convertXMLToJSON(fileName);
        File newFile = new File("C:\\Users\\Davide\\Desktop\\newJSONTest.json");
        FileWriter writer = new FileWriter(newFile, false);
        writer.write(json);
        writer.close();
        return newFile;
    }
}
