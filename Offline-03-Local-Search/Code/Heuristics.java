import java.util.*;

public class Heuristics {
    Graph graph;
    ArrayList<Student> students;

    public Heuristics(Graph graph, ArrayList<Student> students) {
        this.graph = graph;
        this.students = students;
    }

    public int scheduleByLargestDegree() {
        graph.getVertices().sort((vertex1, vertex2) -> vertex2.getAdjListSize() - vertex1.getAdjListSize());
        return graph.schedule();
    }

    public int scheduleBySaturationDegree() {
        int timeSlots = 0;
        graph.getVertices().sort((vertex1, vertex2) -> vertex2.getAdjListSize() - vertex1.getAdjListSize());
        graph.getVertices().get(0).getCourse().setTimeSlot(0);
        timeSlots++;
        HashSet<Integer> assigned, tempAssigned;
        for(int i=1; i<graph.getVertices().size(); i++) {
            assigned = null;
            int maxSaturationDegree = -1;
            int maxSaturationDegreeIdx = -1;
            for(int j=0; j<graph.getVertices().size(); j++) {
                if(!graph.getVertices().get(j).getCourse().isSet()) {
                    tempAssigned = new HashSet<>();
                    graph.addAssignedTimeSlots(tempAssigned, graph.getVertices().get(j));
                    if((tempAssigned.size() > maxSaturationDegree) || (tempAssigned.size() == maxSaturationDegree && graph.getVertices().get(j).getAdjListSize() > graph.getVertices().get(maxSaturationDegreeIdx).getAdjListSize())) {
                        maxSaturationDegree = tempAssigned.size();
                        maxSaturationDegreeIdx = j;
                        assigned = tempAssigned;
                    }
                }
            }
            int assignedSlot = 0;
            while(!graph.getVertices().get(maxSaturationDegreeIdx).getCourse().isSet()) {
                if(assigned.contains(assignedSlot)) assignedSlot++;
                else {
                    graph.getVertices().get(maxSaturationDegreeIdx).getCourse().setTimeSlot(assignedSlot);
                    if(assignedSlot == timeSlots) timeSlots++;
                }
            }
        }
        return timeSlots;
    }

    public int scheduleByLargestEnrollment() {
        graph.getVertices().sort((vertex1, vertex2) -> vertex2.getEnrollments() - vertex1.getEnrollments());
        return graph.schedule();
    }

    public int randomSchedule() {
        Collections.shuffle(graph.getVertices());
        return graph.schedule();
    }


    public void applyKempeChain(int trials) {
        Random rand = new Random();
        ArrayList<Vertex> vertices = graph.getVertices();
        for(int count=0; count<trials; count++) {
            int val1 = rand.nextInt(vertices.size());
            Vertex initial = vertices.get(val1);
            ArrayList<Vertex> conflictingVertices = initial.getAdjList();
            if(conflictingVertices.size() != 0) {
                Vertex next = conflictingVertices.get(rand.nextInt(conflictingVertices.size()));
                // get a Kempe chain
                graph.setKempeChain(initial, next, conflictingVertices);
                double penaltyBefore = Main.avgPenalty(students);
                int iTimeSlot = initial.getCourse().getTimeSlot();
                int nTimeSlot = next.getCourse().getTimeSlot();

                // Kempe chain interchange
                for(int i=0; i<vertices.size(); i++) {
                    if(vertices.get(i).getColor().equals("black")) {
                        if(vertices.get(i).getCourse().getTimeSlot() == iTimeSlot) vertices.get(i).getCourse().setTimeSlot(nTimeSlot);
                        else vertices.get(i).getCourse().setTimeSlot(iTimeSlot);
                    }
                }
                double penaltyAfter = Main.avgPenalty(students);
                if(penaltyBefore <= penaltyAfter) {
                    // undoing interchange
                    for(int i=0; i<vertices.size(); i++) {
                        if(vertices.get(i).getColor().equals("black")) {
                            if(vertices.get(i).getCourse().getTimeSlot() == iTimeSlot) vertices.get(i).getCourse().setTimeSlot(nTimeSlot);
                            else vertices.get(i).getCourse().setTimeSlot(iTimeSlot);
                        }
                    }
                }
                for(int i=0; i<vertices.size(); i++) {
                    if(vertices.get(i).getColor().equals("black")) vertices.get(i).setColor("white");
                }
            }

        }
    }

    public void applyPairSwap(int trials) {
        Random rand = new Random();
        for(int count = 0; count < trials; count++) {
            Vertex v1 = graph.getVertices().get(rand.nextInt(graph.getVertices().size()));
            Vertex v2 = graph.getVertices().get(rand.nextInt(graph.getVertices().size()));
            if(graph.isPairSwapPossible(v1, v2)) {
                // current penalty
                double penaltyBefore = Main.avgPenalty(students);
                // swapping pair time slots
                int v1TimeSlot = v1.getCourse().getTimeSlot();
                int v2TimeSlot = v2.getCourse().getTimeSlot();
                v1.getCourse().setTimeSlot(v2TimeSlot);
                v2.getCourse().setTimeSlot(v1TimeSlot);
                // penalty after swap
                double penaltyAfter = Main.avgPenalty(students);
                if(penaltyBefore <= penaltyAfter) {
                    // no benefit -> undo swap
                    v1.getCourse().setTimeSlot(v1TimeSlot);
                    v2.getCourse().setTimeSlot(v2TimeSlot);
                }
            }
        }
    }
}
