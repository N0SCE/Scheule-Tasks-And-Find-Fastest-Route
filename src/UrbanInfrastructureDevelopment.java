import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class UrbanInfrastructureDevelopment implements Serializable {
    static final long serialVersionUID = 88L;

    /**
     * Given a list of Project objects, prints the schedule of each of them.
     * Uses getEarliestSchedule() and printSchedule() methods of the current project to print its schedule.
     * @param projectList a list of Project objects
     */
    public void printSchedule(List<Project> projectList) {
        // TODO: YOUR CODE HERE
        for(Project project : projectList){
            int[] schedule = project.getEarliestSchedule();
            project.printSchedule(schedule);
        }
    }

    /**
     * TODO: Parse the input XML file and return a list of Project objects
     *
     * @param filename the input XML file
     * @return a list of Project objects
     */
    public List<Project> readXML(String filename) {
        List<Project> projectList = new ArrayList<>();
        // TODO: YOUR CODE HERE

        try {

            File file = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(filename);
            document.getDocumentElement().normalize();

            NodeList projects = document.getElementsByTagName("Project");
            for(int i = 0; i < projects.getLength(); i++){
                Project project;
                Node projectNode = projects.item(i);
                Element projectElement = (Element) projectNode;

                String projectName = projectElement.getElementsByTagName("Name").item(0).getTextContent();
                NodeList tastLists = projectElement.getElementsByTagName("Task");

                List<Task> tasks = new ArrayList<>();
                for(int j = 0; j < tastLists.getLength(); j++){
                    Task task;
                    Element taskElement = (Element) tastLists.item(j);
                    int taskID = Integer.parseInt(taskElement.getElementsByTagName("TaskID").item(0).getTextContent());
                    String description = taskElement.getElementsByTagName("Description").item(0).getTextContent();
                    int duration = Integer.parseInt(taskElement.getElementsByTagName("Duration").item(0).getTextContent());
                    List<Integer> dependicies = new ArrayList<>();
                    //make dependicies
                    if(taskElement.getElementsByTagName("Dependencies").getLength() != 0){
                        for(int k = 0; k < taskElement.getElementsByTagName("DependsOnTaskID").getLength(); k++){
                            int dependency = Integer.parseInt(taskElement.getElementsByTagName("DependsOnTaskID").item(k).getTextContent());
                            dependicies.add(dependency);
                        }
                    }

                    task = new Task(taskID, description, duration, dependicies);
                    tasks.add(task);
                }
                project = new Project(projectName, tasks);
                projectList.add(project);
            }



        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }


        return projectList;
    }


}
