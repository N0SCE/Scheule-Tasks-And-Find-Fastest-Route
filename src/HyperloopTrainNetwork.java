import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HyperloopTrainNetwork implements Serializable {
    static final long serialVersionUID = 11L;
    public double averageTrainSpeed;
    public final double averageWalkingSpeed = 1000 / 6.0;;
    public int numTrainLines;
    public Station startPoint;
    public Station destinationPoint;
    public List<TrainLine> lines;

    /**
     * Method with a Regular Expression to extract integer numbers from the fileContent
     * @return the result as int
     */
    public int getIntVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*([0-9]+)");
        Matcher m = p.matcher(fileContent);

        m.find();
        return Integer.parseInt(m.group(1));
    }

    /**
     * Write the necessary Regular Expression to extract string constants from the fileContent
     * @return the result as String
     */
    public String getStringVar(String varName, String fileContent) {
        // TODO: Your code goes here
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*\"([^\"]*)\"");
        Matcher m = p.matcher(fileContent);
        m.find();
        return m.group(1);

    }

    /**
     * Write the necessary Regular Expression to extract floating point numbers from the fileContent
     * Your regular expression should support floating point numbers with an arbitrary number of
     * decimals or without any (e.g. 5, 5.2, 5.02, 5.0002, etc.).
     * @return the result as Double
     */
    public Double getDoubleVar(String varName, String fileContent) {
        // TODO: Your code goes here
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*(-?\\d*\\.?\\d+)");
        Matcher m = p.matcher(fileContent);
        m.find();
        return Double.parseDouble(m.group(1));
    }

    /**
     * Write the necessary Regular Expression to extract a Point object from the fileContent
     * points are given as an x and y coordinate pair surrounded by parentheses and separated by a comma
     * @return the result as a Point object
     */
    public Point getPointVar(String varName, String fileContent) {
        //Point p = new Point(0, 0);
        // TODO: Your code goes here
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)");
        Matcher m = p.matcher(fileContent);

        if(m.find()){

        }
        int x = Integer.parseInt(m.group(1));
        int y = Integer.parseInt(m.group(2));
        return new Point(x, y);


    }

    /**
     * Function to extract the train lines from the fileContent by reading train line names and their
     * respective stations.
     * @return List of TrainLine instances
     */
    public List<TrainLine> getTrainLines(String fileContent) {
        List<TrainLine> trainLines = new ArrayList<>();

        // TODO: Your code goes here
        Pattern trainLinePattern = Pattern.compile("train_line_name\\s*=\\s*\"([^\"]+)\".*?train_line_stations\\s*=\\s*((?:\\(\\s*\\d+\\s*,\\s*\\d+\\s*\\)\\s*)+)", Pattern.DOTALL);
        Matcher trainLineMatcher = trainLinePattern.matcher(fileContent);

        while (trainLineMatcher.find()) {
            String trainLineName = trainLineMatcher.group(1);
            String stationsStr = trainLineMatcher.group(2);

            Pattern stationPattern = Pattern.compile("\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)");
            Matcher stationMatcher = stationPattern.matcher(stationsStr);

            List<Station> stations = new ArrayList<>();
            int index = 0;
            while (stationMatcher.find()) {
                index++;
                int x = Integer.parseInt(stationMatcher.group(1));
                int y = Integer.parseInt(stationMatcher.group(2));
                Point point = new Point(x, y);
                Station station = new Station(point, trainLineName + " Line Station " + index);
                stations.add(station);
            }

            TrainLine trainLine = new TrainLine(trainLineName, stations);
            trainLines.add(trainLine);
        }


        return trainLines;
    }

    /**
     * Function to populate the given instance variables of this class by calling the functions above.
     */
    public void readInput(String filename) {

        // TODO: Your code goes here
        try{
            String fileContent = new String(Files.readAllBytes(Paths.get(filename)));

            averageTrainSpeed = (getDoubleVar("average_train_speed", fileContent) * 1000) / 60;

            numTrainLines = getIntVar("num_train_lines", fileContent);
            destinationPoint = new Station(getPointVar("destination_point", fileContent), "Final Destination");

            startPoint = new Station(getPointVar("starting_point", fileContent), "Starting Point");
            lines = getTrainLines(fileContent);

        }catch (IOException e) {
            System.out.println("IO error.");
        }

    }
}