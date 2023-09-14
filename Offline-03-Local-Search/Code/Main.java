import java.io.*;
import java.util.ArrayList;

public class Main {

    public static double avgPenalty(ArrayList<Student> students) {
        double sumPenalty = 0.0;
        for (Student student : students) sumPenalty += student.getExpPenalty();
        return sumPenalty * 1.0 / students.size();
    }

    public static double avgPenaltyLinear(ArrayList<Student> students) {
        double sumPenalty = 0.0;
        for (Student student : students) sumPenalty += student.getLinearPenalty();
        return sumPenalty * 1.0 / students.size();
    }

    public static void main(String[] args) {
        Graph graph = new Graph();
        ArrayList<Course> courses = new ArrayList<>();
        ArrayList<Student> students = new ArrayList<>();
        String fileName;
        fileName = "car-f-92";
        //fileName = "car-s-91";
        //fileName = "kfu-s-93";
        //fileName = "tre-s-92";
        //fileName = "yor-f-83";

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader("Toronto/"+fileName+".crs"));
            String line = br.readLine();
            while (line != null) {
                String[] lineInput = line.split(" ");
                courses.add(new Course(lineInput[0], Integer.parseInt(lineInput[1])));
                line = br.readLine();
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // add to graph
        for (Course course : courses) graph.addVertex(new Vertex(course));

        // conflictMatrix to hold the conflicts which will later be added as edges
        int[][] conflictMatrix = new int[courses.size()][courses.size()];
        for(int i=0; i<conflictMatrix.length; i++) {
            for(int j=0; j<conflictMatrix[0].length; j++) {
                conflictMatrix[i][j] = 0;
            }
        }

        // .stu file
        int student_count = 0;
        try {
            br = new BufferedReader(new FileReader("Toronto/"+fileName+".stu"));
            String line = br.readLine();
            while (line != null) {
                String[] lineInput = line.split(" ");
                int[] coursesTaken = new int[lineInput.length];
                for(int i=0; i<coursesTaken.length; i++) {
                    coursesTaken[i] = Integer.parseInt(lineInput[i]);
                }

                // adding to student
                students.add(new Student());
                for (int k : coursesTaken) {
                    students.get(student_count).addCourse(courses.get(k - 1));
                }
                student_count++;

                // setting conflicts
                for(int i=0; i<coursesTaken.length-1; i++) {
                    for(int j=i+1; j<coursesTaken.length; j++) {
                        if(conflictMatrix[coursesTaken[i]-1][coursesTaken[j]-1] == 0) {
                            conflictMatrix[coursesTaken[i]-1][coursesTaken[j]-1] = conflictMatrix[coursesTaken[j]-1][coursesTaken[i]-1] = 1;
                        }
                    }
                }

                line = br.readLine();
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // adding conflicts to graph as edges
        for(int i=0; i<conflictMatrix.length; i++) {
            for(int j=0; j<conflictMatrix[0].length; j++) {
                if(conflictMatrix[i][j] == 1) {
                    graph.getVertices().get(i).addNeighbour(graph.getVertices().get(j));
                }
            }
        }

        // applying heuristics
        Heuristics heuristics = new Heuristics(graph, students);
        int timeSlots = heuristics.randomSchedule();
        System.out.println(timeSlots);
        System.out.println("Penalty before applying Kempe-chain interchange: "+ avgPenalty(students));
        heuristics.applyKempeChain(5000);
        System.out.println("Penalty after applying Kempe-chain interchange: "+ avgPenalty(students));
        heuristics.applyPairSwap(30000);
        System.out.println("Penalty after applying pair swap: "+ avgPenalty(students));


    }
}
