import entities.Course;
import entities.Student;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class lazyInitialization {
    public static void main(String[] args) {

        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();

        session.beginTransaction();
        Student student = new Student();
        student.setName("John");
        student.setSurname("Doe");
        session.persist(student);

        Course course1 = new Course();
        course1.setCourseName("Math");
        course1.setStudent(student);
        session.persist(course1);

        Course course2 = new Course();
        course2.setCourseName("Chemistry");
        course2.setStudent(student);
        session.persist(course2);

        Course course3 = new Course();
        course3.setCourseName("Poetry");
        course3.setStudent(student);
        session.persist(course3);

        session.getTransaction().commit();
        session.close();


        session = sessionFactory.openSession();
        // no exception when we use join fetch
        String sqlQuery = "select s from Student s join fetch s.courses cs";


        // in case below we have exception in thread "main" org.hibernate.LazyInitializationException
        //String sqlQuery = "select s from Student s ";


        Query<Student> query = session.createQuery(sqlQuery, Student.class);
        List<Student> studentList = query.list();

        session.close();

        System.out.println("session is close...");
        System.out.println("Student name => " + studentList.get(0).getName());
        System.out.println("Student surname => " + studentList.get(0).getSurname());
        System.out.println("Student courses => " + studentList.get(0).getCourses());



    }



}