import java.util.ArrayList;

class Vertex {
    private Course course;
    private String color;
    private ArrayList<Vertex> adjList;

    Vertex(Course course) {
        this.course = course;
        this.color = "white";
        this.adjList = new ArrayList<>();
    }

    public int getAdjListSize() {return adjList.size();}

    public int getEnrollments() {return course.getEnrollments();}

    public void addNeighbour(Vertex vertex) { this.adjList.add(vertex); }

    public ArrayList<Vertex> getAdjList() { return adjList;}


    public Course getCourse() { return course; }

    public void setCourse(Course course) { this.course = course; }

    public String getColor() { return color; }

    public void setColor(String color) { this.color = color; }
}