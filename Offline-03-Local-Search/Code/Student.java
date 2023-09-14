import java.util.ArrayList;
import java.util.Comparator;

public class Student {
    private final ArrayList<Course> courses;

    Student() { this.courses = new ArrayList<>();}

    public void addCourse(Course course) { this.courses.add(course); }


    public double getExpPenalty() {
        double penalty = 0.0;
        courses.sort(Comparator.comparingInt(Course::getTimeSlot));
        for(int i=0; i<courses.size()-1; i++) {
            for(int j=i+1; j<courses.size(); j++) {
                int days = courses.get(j).getDaysBetween(courses.get(i));
                if(days <= 5) penalty += Math.pow(2, 5-days);
            }
        }
        return penalty;
    }

    public double getLinearPenalty() {
        double penalty = 0.0;
        courses.sort(Comparator.comparingInt(Course::getTimeSlot));
        for(int i=0; i<courses.size()-1; i++) {
            for(int j=i+1; j<courses.size(); j++) {
                int days = courses.get(j).getDaysBetween(courses.get(i));
                if(days <= 5) penalty += 2 * (5-days);
            }
        }
        return penalty;
    }
}
