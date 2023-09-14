import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Graph {
    private final ArrayList<Vertex> vertices;

    Graph() {
        this.vertices = new ArrayList<>();
    }

    public void addVertex(Vertex vertex) { vertices.add(vertex);}

    public ArrayList<Vertex> getVertices() { return vertices; }

    public int schedule() {
        int timeSlots = 0;
        for (Vertex vertex : vertices) {
            ArrayList<Vertex> conflictingVertices = vertex.getAdjList();
            int[] slot = new int[conflictingVertices.size()];
            for (int j = 0; j < conflictingVertices.size(); j++)
                slot[j] = conflictingVertices.get(j).getCourse().getTimeSlot();
            Arrays.sort(slot);

            int assignedTimeSlot = 0;
            for (int i : slot) {
                if (i == -1) continue;
                if (assignedTimeSlot == i) // conflict -> increase assignedTimeSlot to next slot
                    assignedTimeSlot++;
                if (assignedTimeSlot < i) // free slot -> set here
                    vertex.getCourse().setTimeSlot(assignedTimeSlot);
            }

            //if(vertices.get(i).getCourse().getTimeSlot() == -1) {
            if (!vertex.getCourse().isSet()) {
                if (assignedTimeSlot == timeSlots) {
                    vertex.getCourse().setTimeSlot(timeSlots);
                    timeSlots++;
                } else vertex.getCourse().setTimeSlot(assignedTimeSlot);
            }

        }
        return timeSlots;
    }

    public void addAssignedTimeSlots(HashSet<Integer> assigned, Vertex v) {
        for(int i=0; i<v.getAdjList().size(); i++) {
            if(v.getAdjList().get(i).getCourse().isSet()) assigned.add(v.getAdjList().get(i).getCourse().getTimeSlot());
        }
    }



    public void setKempeChain(Vertex initial, Vertex next, ArrayList<Vertex> conflictingVertices) {
        initial.setColor("gray");
        for(int j=0; j<conflictingVertices.size(); j++) {
            if((conflictingVertices.get(j).getColor().equals("white")) &&(conflictingVertices.get(j).getCourse().getTimeSlot() == next.getCourse().getTimeSlot()))
                setKempeChain(conflictingVertices.get(j), initial, conflictingVertices);
        }
        initial.setColor("black");
    }

    public boolean isPairSwapPossible(Vertex v1, Vertex v2) {
        if(v1.getCourse().getTimeSlot() == v2.getCourse().getTimeSlot()) return false;
        for(int i=0; i<v1.getAdjList().size(); i++) {
            if(v1.getAdjList().get(i).getCourse().getTimeSlot() == v2.getCourse().getTimeSlot()) return false;
        }
        for(int i=0; i<v2.getAdjList().size(); i++) {
            if(v2.getAdjList().get(i).getCourse().getTimeSlot() == v1.getCourse().getTimeSlot()) return false;
        }
        return true;
    }




}

























