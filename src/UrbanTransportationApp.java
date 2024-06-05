import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.*;

class UrbanTransportationApp implements Serializable {
    static final long serialVersionUID = 99L;

    DecimalFormat decimalFormat = new DecimalFormat("0.00");
    
    public HyperloopTrainNetwork readHyperloopTrainNetwork(String filename) {
        HyperloopTrainNetwork hyperloopTrainNetwork = new HyperloopTrainNetwork();
        hyperloopTrainNetwork.readInput(filename);
        return hyperloopTrainNetwork;
    }

    /**
     * Function calculate the fastest route from the user's desired starting point to 
     * the desired destination point, taking into consideration the hyperloop train
     * network. 
     * @return List of RouteDirection instances
     */
    public List<RouteDirection> getFastestRouteDirections(HyperloopTrainNetwork network) {
        List<RouteDirection> routeDirections = new ArrayList<>();
        
        // TODO: Your code goes here

        List<Double> distances = new ArrayList<>();
        List<Station> previous = new ArrayList<>();
        //PriorityQueue<StationDistancePair> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(pair -> pair.distance));
        distances.add(0.0);
        previous.add(null);

        Station startPoint = network.startPoint;
        Station endPoint = network.destinationPoint;
        Station current = startPoint;

        Station nextStation = null;
        double duration = 0;
        List<Station> stations = new ArrayList<>();
        for(TrainLine trainLine : network.lines){
            stations.addAll(trainLine.trainLineStations);
            for (Station station : trainLine.trainLineStations) {
                distances.add(Double.MAX_VALUE);
                previous.add(null);
            }
        }
        stations.add(endPoint);
        distances.add(Double.MAX_VALUE);
        previous.add(null);


        while (!endPoint.isVisited){
            double min = Double.MAX_VALUE;

            int indexFirstDistance = 0;
            for(Station station : stations){ // check where you visit before
                indexFirstDistance++;
                    if(station.isVisited){
                        continue;
                    }


                    if(Objects.equals(current.description.split(" ")[0], station.description.split(" ")[0])){ // check it
                        duration = Math.sqrt(Math.pow((station.coordinates.y - current.coordinates.y), 2) + Math.pow((station.coordinates.x - current.coordinates.x),2)) / (network.averageTrainSpeed);
                    }else{
                        duration = Math.sqrt(Math.pow((station.coordinates.y - current.coordinates.y), 2) + Math.pow((station.coordinates.x - current.coordinates.x),2)) / network.averageWalkingSpeed;
                    }

                    if(distances.get(indexFirstDistance) == Double.MAX_VALUE){
                        distances.set(indexFirstDistance, duration);
                        previous.set(indexFirstDistance, current);
                    }

                    if(duration < min){
                        min = duration;
                        nextStation = station;
                    }

            }
            nextStation.isVisited = true;

            int index = 0;
            int firstIndex = stations.indexOf(nextStation) + 1;
            for(Station station : stations){
                index++;
                if(station.description == nextStation.description){
                    continue;
                }

                double lenght = 0;
                if(Objects.equals(nextStation.description.split(" ")[0], station.description.split(" ")[0])){ // check it
                    lenght = Math.sqrt(Math.pow((station.coordinates.y - nextStation.coordinates.y), 2) + Math.pow((station.coordinates.x - nextStation.coordinates.x),2)) / (network.averageTrainSpeed) ;
                }else{
                    lenght = Math.sqrt(Math.pow((station.coordinates.y - nextStation.coordinates.y), 2) + Math.pow((station.coordinates.x - nextStation.coordinates.x),2)) / network.averageWalkingSpeed;
                }
                if(distances.get(firstIndex) + lenght < distances.get(index)){
                    distances.set(index,distances.get(firstIndex) + lenght) ;
                    previous.set(index, nextStation);
                }
            }

            //
            //routeDirections.add(new RouteDirection(current.description, nextStation.description, duration, current.description.contains("Station")));
            current = nextStation;
            current.isVisited = true;
        }

        Station station = endPoint;
        Station previousStation = previous.get(previous.size()-1);
        Stack<RouteDirection> temp = new Stack<>();
        while (previousStation.description != startPoint.description){
            double time = distances.get(stations.indexOf(station)+1) - distances.get(stations.indexOf(previousStation)+1);
            temp.push(new RouteDirection(previousStation.description, station.description, time, Objects.equals(station.description.split(" ")[0], previousStation.description.split(" ")[0])));
            //routeDirections.add(new RouteDirection(previousStation.description, station.description, time, Objects.equals(station.description.split(" ")[0], previousStation.description.split(" ")[0])));
            station = previousStation;
            previousStation = previous.get(stations.indexOf(station)+1);
        }

        temp.push(new RouteDirection(startPoint.description, station.description, distances.get(stations.indexOf(station)+1), Objects.equals(startPoint.description.split(" ")[0], station.description.split(" ")[0])));
        while (!temp.isEmpty()){
            routeDirections.add(temp.pop());
        }
        return routeDirections;
    }

    /**
     * Function to print the route directions to STDOUT
     */
    public void printRouteDirections(List<RouteDirection> directions) {
        
        // TODO: Your code goes here


        int index = 0;
        String output = "Directions\n----------\n";
        double totalDuration = 0;
        for(RouteDirection routeDirection : directions){
            index++;
            output += String.valueOf(index) + ". " ;

            if(routeDirection.trainRide){
                output += "Get on the train from \"";
            }else {
                output += "Walk from ";
            }

            output += routeDirection.startStationName + "\" to \"" + routeDirection.endStationName + "\" ";
            output += decimalFormat.format(routeDirection.duration) + " minutes.";
            output += "\n";
            totalDuration += routeDirection.duration;
        }

        System.out.println("The fastest route takes " + Math.round(totalDuration) + " minute(s).");
        System.out.println(output);
    }
}