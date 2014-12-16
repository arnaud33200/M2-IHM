/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universityuml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
/**
 *
 * @author ladoucar
 */
public class University {

    // /!\ les cours dans enrollement sont dans course
    private HashMap<Course, Object> courses;
    private HashMap<Enrollment, Object> registrations;
    private HashMap<Student, Object> students;

    public University(HashMap<Course, Object> courses, HashMap<Student, Object> students) {
        this.courses = courses;
        this.students = students;
    }
    
    public Enrollment getEnrollment( Student s, Course c) throws MissingEnrollmentException {
        Set<Enrollment> enrolls = registrations.keySet();
        for (Enrollment e : enrolls) {
            if (e.getStudent() == s && e.getCourse() == c) {
                return e;
            }               
        }
        throw new MissingEnrollmentException();
    }

    public int enroll( Student s, Course c) throws MissingStudentException, MissingEnrollmentException {
        // check if parameters is null
        if (students.containsKey(s) && courses.containsKey(c)) {
            return (getEnrollment(s, c)).getMark();
        }
        throw new MissingStudentException();
    }
    
    public ArrayList<Course> coursesForStudent(Student s) throws MissingEnrollmentException {
        ArrayList<Course> lc = new ArrayList<>();
        Set<Enrollment> enrolls = registrations.keySet();
        for (Enrollment e : enrolls) {
            if (e.getStudent() == s) {
                lc.add(e.getCourse());
            }               
        }
        if (lc.isEmpty()) {
            throw new MissingEnrollmentException();
        }
        return lc;
    }
    
    public ArrayList<Student> studentForCourse(Course c) throws MissingEnrollmentException {
        ArrayList<Student> ls = new ArrayList<>();
        Set<Enrollment> enrolls = registrations.keySet();
        for (Enrollment e : enrolls) {
            if (e.getCourse() == c) {
                ls.add(e.getStudent());
            }               
        }
        if (ls.isEmpty()) {
            throw new MissingEnrollmentException();
        }
        return ls;
    }
    
    public HashMap<Course, Object> emptyCourses() {
        return null;
    }
    
    public void withdraw( Student s, Course c) {
        
    }
    
    public void setMark(Student s, Course c) {
        
    }
}
