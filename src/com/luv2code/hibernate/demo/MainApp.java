package com.luv2code.hibernate.demo;

import com.luv2code.hibernate.demo.entity.Employee;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import java.io.*;
import java.util.*;


public class MainApp {

    public static void main(String[] args) {

            boolean keepWorking = true;
            while(keepWorking) {

                SessionFactory factory = new Configuration()
                        .configure("hibernateEmployee.cfg.xml")
                        .addAnnotatedClass(Employee.class)
                        .buildSessionFactory();

                System.out.println("Insert a number assign to action you would like to perform: \n" +
                        "1 - Add Employees from text file\n" +
                        "2 - Add Employee\n" +
                        "3 - Update Employee's data\n" +
                        "4 - Delete Employee\n" +
                        "5 - Delete ALL Employees\n" +
                        "6 - Show Employees\n" +
                        ">6 - Close application\n");

                Session session = factory.getCurrentSession();

                try (session) {
                    session.beginTransaction();
                    ConsoleReader consoleReader = new ConsoleReader();
                    
                    int decision = consoleReader.readDecision();

                    switch (decision) {
                        case 1 -> saveEmployeesFromTextFile(session);
                        case 2 -> addEmployee(session);
                        case 3 -> updateEmployee(session);
                        case 4 -> deleteEmployee(session);
                        case 5 -> deleteAllEmployees(session);
                        case 6 -> showEmployees(session);
                        default -> {
                            System.out.println("Application has been terminated");
                            keepWorking = false;
                        }
                    }

                    session.getTransaction().commit();


                } catch (FileNotFoundException e) {
                    System.out.println("No text file was found");
                }
            }

        System.out.println("Application is closing...");
    }



    private static ConsoleReader consoleReader = new ConsoleReader();

    private static void deleteAllEmployees(Session session) {
        System.out.println("Are you sure you want to delete ALL Employees? (Y/N)");
        String decision = (consoleReader.fetchInput());
        if(decision.equals("y")){
            session.createQuery("delete from Employee").executeUpdate();
            System.out.println("Deleting was successful");
        } else{
            System.out.println("Deleting procedure has been aborted");
        }


    }

    private static void addEmployee(Session session) {
        String iteration = "y";
        while("y".equalsIgnoreCase(iteration)) {
            displayEmployees(session);
            System.out.println("Add new Employee.");

            String firstName = consoleReader.readProperty("Insert employee's name: ");
            String lastName = consoleReader.readProperty("Insert employee's last name: ");
            String company = consoleReader.readProperty("Insert employee's company: ");
            session.save(new Employee(firstName, lastName, company));
            displayEmployees(session);
            System.out.println("Would you like to add another employee (Y/N)?");
            iteration = consoleReader.fetchInput();
        }
    }



    private static void deleteEmployee(Session session) {
        String iteration = "y";
        while("y".equalsIgnoreCase(iteration)) {
            displayEmployees(session);
            System.out.println("Delete Employee\nInsert employee's id: ");
            int empId = Integer.parseInt(consoleReader.fetchInput());
            Employee employee = session.get(Employee.class, empId);
            session.delete(employee);

            displayEmployees(session);
            System.out.println("Would you like to delete another employee (Y/N)?");
            iteration = consoleReader.fetchInput();
        }
    }

    private static void updateEmployee(Session session) {
        String iteration = "y";
        while("y".equalsIgnoreCase(iteration)) {
            displayEmployees(session);
            System.out.println("Update Employee's data\nInsert employee's id: ");
            int empId = Integer.parseInt(consoleReader.fetchInput());
            Employee employee = session.get(Employee.class, empId);
            System.out.println("Update information of your choice. If an update is not necessary, press enter and leave field empty");
            System.out.println("First name:");
            String firstName = (consoleReader.fetchInput());
            System.out.println("Last name:");
            String lastName = (consoleReader.fetchInput());
            System.out.println("Company:");
            String company = (consoleReader.fetchInput());

            if (!"".equals(firstName)) {
                employee.setFirstName(firstName);
                System.out.println("First name updated");
            }
            if (!"".equals(lastName)) {
                employee.setLastName(lastName);
                System.out.println("Last name updated");
            }
            if (!"".equals(company)) {
                employee.setCompany(company);
                System.out.println("Company name updated");
            }
            displayEmployees(session);
            System.out.println("Would you like to update another employee (Y/N)?");
            iteration = consoleReader.fetchInput();
        }
    }

    private static void saveEmployeesToDB(Session session, List<Employee> employees) {
        for(Employee emp : employees){
            session.save(emp);
        }
    }
    private static void saveEmployeesFromTextFile(Session session) throws FileNotFoundException {
        System.out.println("Insert path to file you would like to use: ");
        String textFilePath = consoleReader.fetchInput();
        File myFile = new File(textFilePath);
        Scanner scan = new Scanner(myFile);
        List<Employee> employees = new ArrayList<>();

        while(scan.hasNextLine()){
            String line = scan.nextLine();
            String firstName = (line.split(" "))[0];
            String lastName = (line.split(" "))[1];
            String company = (line.split(" "))[2];
            employees.add(new Employee(firstName, lastName, company));
        }

        saveEmployeesToDB(session, employees);
    }

    private static void displayEmployees(Session session){
        var employees = session.createQuery("from Employee").getResultList();
        for(Object emp : employees) {
            System.out.println(emp);
        }
    }

    private static void showEmployees(Session session){
        String iteration = "n";
        while("n".equalsIgnoreCase(iteration)) {
            var employees = session.createQuery("from Employee").getResultList();
            for(Object emp : employees) {
                System.out.println(emp);
            }
            System.out.println("Would you like to go back to main menu (Y/N)?");
            iteration = consoleReader.fetchInput();
        }
    }
}


