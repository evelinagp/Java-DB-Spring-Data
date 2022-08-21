import entities.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

//TODO
// WRITE YOUR USERNAME AND PASSWORD IN  FIRE "persistence.xml".
// RUN THE PROGRAM AND WRITE IN THE CONSOLE THE NUMBER OF THE EXERCISE AFTER THAT THE INPUT - YOU WILL RECEIVE THE RESULT.
// FOR EXERCISES №: 7 AND 12 USE FRESH DATABASE.
// FOR EX9 depends of your timezone settings the output hour MIGHT be +3 hours ahead.

public class Engine implements Runnable {
    private final EntityManager entityManager;
    private final BufferedReader reader;

    public Engine(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }


    @Override
    public void run() {
        System.out.println("Select ex number:");
        try {
            int exNum = Integer.parseInt(reader.readLine());
            switch (exNum) {
                case 2:
                    exTwo();
                    break;
                case 3:
                    exThree();
                    break;
                case 4:
                    exFour();
                    break;
                case 5:
                    exFive();
                    break;
                case 6:
                    exSix();
                    break;
                case 7:
                    exSeven();
                    break;
                case 8:
                    exEight();
                    break;
                case 9:
                    exNine();
                    break;
                case 10:
                    exTen();
                    break;
                case 11:
                    exEleven();
                    break;
                case 12:
                    exTwelve();
                    break;
                case 13:
                    exThirteen();
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

    }

    private void exThirteen() throws IOException {
        System.out.println("Enter town name:");
        String townName = reader.readLine();

        Query addressQuery = entityManager
                .createQuery("SELECT a FROM Address AS a WHERE a.town.name = :townName");
        addressQuery.setParameter("townName", townName);

        List<Address> addresses = addressQuery.getResultList();

        int affectedRows = 0;
        entityManager.getTransaction().begin();
        Town town = entityManager.createQuery("SELECT t FROM Town t WHERE t.name = :t_name", Town.class)
                .setParameter("t_name", townName)
                .getSingleResult();
        for (Address address : addresses) {
            for (Employee employee : address.getEmployees()) {
                employee.setAddress(null);
            }
            affectedRows++;
            entityManager.flush();
            entityManager.remove(address);
        }

        entityManager.remove(town);

        entityManager.getTransaction().commit();

        String address = affectedRows == 1 ? "address" : "addresses";
        System.out.printf("%d %s in %s deleted", affectedRows, address, ((townName.substring(0, 1)).toUpperCase()) + (townName.substring(1)).toLowerCase());

    }

    private void exEleven() throws IOException {
        System.out.println("Enter employee first name starting pattern:");
        String pattern = (reader.readLine());

        List<Employee> employees = entityManager.createQuery("SELECT e FROM Employee e " +
                        "WHERE e.firstName LIKE CONCAT(:startingWith, '%')", Employee.class)
                .setParameter("startingWith", pattern)
                .getResultList();

        if (employees.isEmpty()) {
            System.out.printf("There are no employees with names starting with: %s.", pattern);
        }

        employees
                .stream()
                .forEach(employee ->
                        System.out.printf("%s %s - %s - ($%s)%n",
                                employee.getFirstName(),
                                employee.getLastName(),
                                employee.getJobTitle(),
                                employee.getSalary()));
    }


    @SuppressWarnings("unchecked")
    private void exTwelve() {

        List<Department> departments = entityManager.createQuery("SELECT d FROM Department AS d " +
                        "JOIN Employee AS e ON e.department.id = d.id " +
                        "GROUP BY d.id " +
                        "HAVING MAX(e.salary) NOT BETWEEN 30000 AND 70000")
                .getResultList();

        departments.forEach(d -> {
            d.getEmployees().stream()
                    .sorted(Comparator.comparing(Employee::getSalary).reversed())
                    .limit(1)
                    .forEach(e ->
                            System.out.printf("%s  %.2f\n",
                                    d.getName(),
                                    e.getSalary()));
        });
    }

    private void exTen() {
        entityManager.getTransaction().begin();
        entityManager.createQuery("UPDATE Employee e " +
                        "SET e.salary = 1.12 * e.salary " +
                        "WHERE e.department.id IN :ids")
                .setParameter("ids", Set.of(1, 2, 4, 11))
                .executeUpdate();

        entityManager.getTransaction().commit();
        List<Employee> employees = entityManager.createQuery("SELECT e FROM Employee e "
                        + "WHERE e.department.id IN (1, 2, 4, 11)", Employee.class)
                .getResultList();

        employees.forEach(e -> {
            System.out.printf("%s %s ($%.2f)%n", e.getFirstName(),
                    e.getLastName(), e.getSalary());
        });
    }

    private void exNine() {

        List<Project> projects = entityManager.createQuery("SELECT p FROM Project p order by p.startDate desc", Project.class)
                .setMaxResults(10).getResultList();

    String output = projects.stream()
            .sorted(Comparator.comparing(Project::getName))
            .map(project -> String.format("Project name: %s" +
                            "\r\n\tProject Description: %s" +
                            "\r\n\tProject Start Date: %s" +
                            "\r\n\tProject End Date: %s",
                    project.getName(),
                    project.getDescription(),
                    Timestamp.valueOf(project.getStartDate()),
                    project.getEndDate()
            )).collect(Collectors.joining("\r\n"));
        System.out.println(output);

//        if (project.getStartDate() != null) {
//            System.out.printf("\tProject Start Date:%s%n", project.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        } else {
//            System.out.println("\tProject Start Date:null");
//        }

}

    private void exEight() throws IOException {
        System.out.println("Enter employee id:");
        int employeeId = Integer.parseInt(reader.readLine());
        Employee employee = entityManager.find(Employee.class, employeeId);
        Set<String> sorted = new TreeSet<>();
        if (employee != null) {
            employee.getProjects().forEach(p -> {
                sorted.add(p.getName());
            });
            System.out.printf("%s %s - %s%n", employee.getFirstName(), employee.getLastName(),
                    employee.getJobTitle());
            sorted.forEach(p -> {
                System.out.printf("\t%s%n", p);
            });
        } else {
            System.out.println("There is not such employee's id in the database");
        }
    }

    private void exSeven() {
        List<Address> addresses = entityManager.createQuery("SELECT a FROM Address a " +
                "order by a.employees.size DESC", Address.class).setMaxResults(10).getResultList();

        addresses.forEach(a -> {
            System.out.printf("%s, %s - %d employees%n", a.getText(), a.getTown() == null
                    ? "Unknown" : a.getTown().getName(), a.getEmployees().size());
        });
    }

    private void exSix() throws IOException {
        System.out.println("Enter employee last name:");
        String lastName = reader.readLine();

        Employee employee = entityManager.createQuery("SELECT e FROM Employee e " +
                        "WHERE e.lastName = :l_name ", Employee.class)
                .setParameter("l_name", lastName)
                .getSingleResult();

        Address address = createAddress("Vitoshka 15");

        entityManager.getTransaction().begin();
        employee.setAddress(address);
        entityManager.getTransaction().commit();

    }

    private Address createAddress(String addressText) {
        Address address = new Address();
        address.setText(addressText);

        entityManager.getTransaction().begin();
        entityManager.persist(address);
        entityManager.getTransaction().commit();

        return address;
    }

    private void exFive() {
        entityManager.createQuery("SELECT e FROM Employee e " +
                        "WHERE e.department.name = :d_name " +
                        "ORDER BY e.salary, e.id", Employee.class)
                .setParameter("d_name", "Research and Development")
                .getResultStream()
                .forEach(employee -> {
                    System.out.printf("%s %s from %s - $%.2f%n", employee.getFirstName()
                            , employee.getLastName(), employee.getDepartment().getName(), employee.getSalary());
                });
    }


    private void exFour() {
        entityManager.createQuery("SELECT e FROM Employee e " +
                        "WHERE e.salary > :min_salary", Employee.class)
                .setParameter("min_salary", BigDecimal.valueOf(50000L))
                .getResultStream()
                .map(Employee::getFirstName)
                .forEach(System.out::println);
    }

    private void exThree() throws IOException {
        System.out.println("Enter employee full name:");
        String[] fullName = reader.readLine().split("\\s+");
        String firstName = fullName[0];
        String lastName = fullName[1];

        Long singleResult = entityManager.createQuery("SELECT COUNT (e) FROM Employee e" +
                        " WHERE e.firstName = :f_name AND e.lastName = :l_name", Long.class)
                .setParameter("f_name", firstName)
                .setParameter("l_name", lastName)
                .getSingleResult();

        System.out.println(singleResult == 0
                ? "No" : "Yes");
    }

    private void exTwo() {
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("UPDATE Town t " +
                "SET t.name = lower(t.name) " +
                "WHERE length(t.name) <= 5 ");

        System.out.println(query.executeUpdate() + " towns were affected");
        entityManager.getTransaction().commit();

    }
}
