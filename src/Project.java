import java.io.Serializable;
import java.util.*;

public class Project implements Serializable {
    static final long serialVersionUID = 33L;
    private final String name;
    private final List<Task> tasks;

    private boolean[] marked;
    private Stack<Integer> post;
    private int[] schedule;

    public Project(String name, List<Task> tasks) {
        this.name = name;
        this.tasks = tasks;
    }

    /**
     * @return the total duration of the project in days
     */
    public int getProjectDuration() {
        int projectDuration = 0;

        // TODO: YOUR CODE HERE
        //int lastIndex = schedule.length -1;
        //projectDuration = schedule[lastIndex] + tasks.get(lastIndex).getDuration();
        getEarliestSchedule();
        int max = Integer.MIN_VALUE;
        for(int i = 0; i < schedule.length; i++){
            if(schedule[i] + tasks.get(i).getDuration() > max){
                max = schedule[i] + tasks.get(i).getDuration();
            }
        }
        projectDuration = max;
        return projectDuration;
    }

    /**
     * Schedule all tasks within this project such that they will be completed as early as possible.
     *
     * @return An integer array consisting of the earliest start days for each task.
     */
    public int[] getEarliestSchedule() {

        // TODO: YOUR CODE HERE
        int tasksLenght = tasks.size();
        post = new Stack<Integer>();
        schedule = new int[tasksLenght];
        marked = new boolean[tasksLenght];
        for(int i = 0; i < tasksLenght; i++){
            if (!marked[i]){
                dfs(i);
            }
        }

        ArrayList<Integer> tasksDone = new ArrayList<>();
        int projectDuration = 0;
        for(int id : post){
            if(id == 0){
                schedule[id] = 0;
                continue;
            }
            Task task = tasks.get(id);


            int maxStart = 0;
            for(int dependency : task.getDependencies()){
                int start = schedule[tasks.get(dependency).getTaskID()] + tasks.get(dependency).getDuration();
                if(start >= maxStart){
                    maxStart = start;
                }
            }
            schedule[id] = maxStart;

        }

        return schedule;
    }

    private void dfs(int i){
        marked[i] = true;
        for(int j : tasks.get(i).getDependencies()){
            if(!marked[j]){
                dfs(j);
            }
        }
        post.push(i);
    }

    public static void printlnDash(int limit, char symbol) {
        for (int i = 0; i < limit; i++) System.out.print(symbol);
        System.out.println();
    }

    /**
     * Some free code here. YAAAY! 
     */
    public void printSchedule(int[] schedule) {
        int limit = 65;
        char symbol = '-';
        printlnDash(limit, symbol);
        System.out.println(String.format("Project name: %s", name));
        printlnDash(limit, symbol);

        // Print header
        System.out.println(String.format("%-10s%-45s%-7s%-5s","Task ID","Description","Start","End"));
        printlnDash(limit, symbol);
        for (int i = 0; i < schedule.length; i++) {
            Task t = tasks.get(i);
            System.out.println(String.format("%-10d%-45s%-7d%-5d", i, t.getDescription(), schedule[i], schedule[i]+t.getDuration()));
        }
        printlnDash(limit, symbol);
        System.out.println(String.format("Project will be completed in %d days.", tasks.get(schedule.length-1).getDuration() + schedule[schedule.length-1]));
        printlnDash(limit, symbol);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;

        int equal = 0;

        for (Task otherTask : ((Project) o).tasks) {
            if (tasks.stream().anyMatch(t -> t.equals(otherTask))) {
                equal++;
            }
        }

        return name.equals(project.name) && equal == tasks.size();
    }

}
