public class Course {
    private String ID;
    private int enrollments;
    private int timeSlot;
    private boolean set;

    Course(String ID, int enrollments) {
        this.ID = ID;
        this.enrollments = enrollments;
        this.timeSlot = -1;
        this.set = false;
    }

    public String getID() { return ID; }

    public void setID(String ID) { this.ID = ID; }

    public int getEnrollments() { return enrollments; }

    public void setEnrollments(int enrollments) { this.enrollments = enrollments; }

    public int getTimeSlot() { return timeSlot; }

    public void setTimeSlot(int timeSlot) {
        this.timeSlot = timeSlot;
        this.set = true;
    }

    public boolean isSet() { return set; }

    public int getDaysBetween(Course course) { return this.timeSlot - course.timeSlot; }

}
