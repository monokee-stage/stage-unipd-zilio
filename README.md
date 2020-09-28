# monokee-iga-workflow

Simple workflow engine.

1) It parses the JSON source file, that contains the process, and creates the BpmnModel associated.
2) The BpmnModel will be used to create the new XML file (bmpn20.xml) and set and execute the new process.

PS: change the DB parameters to your own set-up.
PPS: to verify the correctness of the new XML file, import it into the flowable-modeler.
