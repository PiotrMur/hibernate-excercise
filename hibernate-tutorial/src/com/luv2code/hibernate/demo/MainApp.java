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
                        ">5 - Close application\n");

                Session session = factory.getCurrentSession();

                try (session) {
                    session.beginTransaction();
                    Scanner scanner = new Scanner(System.in);
                    int decision = Integer.parseInt(scanner.nextLine());

                    switch (decision) {
                        case 1 -> {
                            List<Employee> employees = getEmployeesFromTextFile("C:/Users/piotr.murawski/OneDrive - intive/Desktop/Employees.txt");
                            saveEmployeesToDB(session, employees);
                        }
                        case 2 -> addEmployee(scanner, session);
                        case 3 -> updateEmployee(scanner, session);
                        case 4 -> deleteEmployee(scanner, session);
                        case 5 -> deleteAllEmployees(session, scanner);
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

    private static void deleteAllEmployees(Session session, Scanner scanner) {
        System.out.println("Are you sure you want to delete ALL Employees? (Y/N)");
        String decision = (scanner.nextLine().trim().toLowerCase());
        if(decision.equals("y")){
            session.createQuery("delete from Employee").executeUpdate();
            System.out.println("Deleting was successful");
        } else{
            System.out.println("Deleting procedure has been aborted");
        }


    }

    private static void addEmployee(Scanner scanner, Session session) {
        String iteration = "y";
        while(iteration.equals("y")) {
            displayEmployees(session);
            System.out.println("Add new Employee.");

            String firstName = "";
            while(firstName.equals("")){
                System.out.println("Insert employee's name: ");
                firstName = (scanner.nextLine()).trim();
            }
            String lastName = "";
            while(lastName.equals("")) {
                System.out.println("Insert employee's last name");
                lastName = (scanner.nextLine()).trim();
            }
            String company = "";
            while(company.equals("")) {
                System.out.println("Insert employee's company");
                company = (scanner.nextLine()).trim();
            }
            session.save(createEmployee(firstName, lastName, company));
            displayEmployees(session);
            System.out.println("Would you like to add another employee (Y/N)?");
            iteration = (scanner.nextLine()).toLowerCase().trim();
        }
    }

    private static void deleteEmployee(Scanner scanner, Session session) {
        String iteration = "y";
        while(iteration.equals("y")) {
            displayEmployees(session);
            System.out.println("Delete Employee\nInsert employee's id: ");
            int empId = Integer.parseInt(scanner.nextLine());
            Employee employee = session.get(Employee.class, empId);
            session.delete(employee);

            displayEmployees(session);
            System.out.println("Would you like to delete another employee (Y/N)?");
            iteration = (scanner.nextLine()).toLowerCase();
        }
    }

    private static void updateEmployee(Scanner scanner, Session session) {
        String iteration = "y";
        while(iteration.equals("y")) {
            displayEmployees(session);
            System.out.println("Update Employee's data\nInsert employee's id: ");
            int empId = Integer.parseInt(scanner.nextLine());
            Employee employee = session.get(Employee.class, empId);
            System.out.println("Update information of your choice. If an update is not necessary, press enter and leave field empty");
            System.out.println("First name:");
            String firstName = (scanner.nextLine()).trim();
            System.out.println("Last name:");
            String lastName = (scanner.nextLine()).trim();
            System.out.println("Company:");
            String company = (scanner.nextLine()).trim();

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
            iteration = (scanner.nextLine()).toLowerCase();
        }
    }

    private static void saveEmployeesToDB(Session session, List<Employee> employees) {
        for(Employee emp : employees){
            session.save(emp);
        }
    }

    private static List<Employee> getEmployeesFromTextFile(String textFilePath) throws FileNotFoundException {
        File myFile = new File(textFilePath);
        Scanner scan = new Scanner(myFile);
        List<Employee> employees = new ArrayList<>();

        while(scan.hasNextLine()){
            String line = scan.nextLine();
            String firstName = (line.split(" "))[0];
            String lastName = (line.split(" "))[1];
            String company = (line.split(" "))[2];
            employees.add(createEmployee(firstName, lastName, company));
        }
        scan.close();
        return employees;
    }

    private static Employee createEmployee(String firstName, String secondName, String company){
        return new Employee(firstName, secondName, company);

    }

    private static void displayEmployees(Session session){
        var employees = session.createQuery("from Employee").getResultList();
        for(Object emp : employees) {
            System.out.println(emp);
        }
    }
}


