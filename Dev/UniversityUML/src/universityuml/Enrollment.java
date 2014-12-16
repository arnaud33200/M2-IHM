/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universityuml;

/**
 *
 * @author ladoucar
 */
public class Enrollment {

    private int mark;
    private Student student;
    private Course  course;
    
    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }
    
    
}
