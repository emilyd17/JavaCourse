// By: Emily Dayanghirang
// Date: May 7, 2021
// Maintains wage information for employees at a company

import java.util.Scanner;
import java.text.NumberFormat;

public abstract class Employee {
    String empName;
    double hrlyPay;
    NumberFormat moneyFormat = NumberFormat.getCurrencyInstance();
    static int empCount;
    {
        ++empCount;
    }

    Employee(String empName, double hrlyPay) {
        this.empName = empName;
        this.hrlyPay = hrlyPay;
    }

    // Getter and setter methods for instance variables
    void setEmpName(String empName) {
        this.empName = empName;
    }
    String getEmpName() {
        return empName;
    }

    void setHrlyPay(double hrlyPay) {
        this.hrlyPay = hrlyPay;
    }
    double getHrlyPay() {
        return hrlyPay;
    }

    // Getter method for static variable
    static int getEmpCount() {
        return empCount;
    }

    // Increases hourly wage by given percentage
    void incrWage(double perInc) {
        this.hrlyPay += hrlyPay * (perInc/100);
    }

    // Computes weekly pay
    abstract double computePay(double hoursWorked);
}

class HourlyEmployee extends Employee {
    HourlyEmployee(String empName, double hrlyPay) {
        super(empName, hrlyPay);
    }

    double computePay(double hoursWorked) {
        // Compute both regular time as well as overtime if needed
        double regPay, otPay, weeklyPay = 0, regHours, otHours;
        regHours = 40;

        if (hoursWorked <= 40) {
            weeklyPay = hoursWorked * getHrlyPay();
        }
        else if (hoursWorked > 40) {
            otHours = hoursWorked - regHours;
            regPay = regHours * getHrlyPay();
            otPay = otHours * (getHrlyPay() * 1.5);

            weeklyPay = regPay + otPay;
        }
        return weeklyPay;
    }

    @Override
    public String toString() {
        return getEmpName() + "\t\t" + (moneyFormat.format(getHrlyPay())) + "/hour";
    }
}

class SalariedEmployee extends Employee {
    double annualSalary;

    SalariedEmployee(String empName, double annualSalary) {
        // The annualSalary will be used to assign a value to the hrlyPay
        // annualSalary/52/40 would give the hrlyPay
        // 52 weeks in a year/ 40 hours worked in a week
        super(empName, (annualSalary / 52) / 40);
        this.annualSalary = annualSalary;
    }

    void setAnnualSalary(double annualSalary) {
        this.annualSalary = annualSalary;
    }

    double getAnnualSalary() {
        return annualSalary;
    }

    double computePay(double hoursWorked) {
        // Multiply hrlypay x hoursWorked
        // hoursWorked is 40 for salaried employees
        // It is only being passed in so that we can get experience with the overriding
        // feature in Java. For salaried employees it is already assumed that it is 40 hours
        // a week so what is being passed in is essentially being ignored
        double weeklyPay = 0;
        weeklyPay = getHrlyPay() * 40;
        return weeklyPay;
    }
}

class EmployeeDriver {
    // Declare array
    static Scanner scnr = new Scanner(System.in);
    static Employee employees[] = new Employee[1];

    public static void main(String[] args) {
        String userChoice;
        userChoice = employeeMenu();
        do {
            select(userChoice);
            userChoice = employeeMenu();
        } while(!userChoice.equals("Q"));
    }

    private static String employeeMenu() {
        String userChoice = "";
        System.out.println("\nN: New employee\n" +
                           "C: Compute paychecks\n" +
                           "R: Raise wages\n" +
                           "L: List all employees\n" +
                           "Q: Quit\n");
        System.out.print("Enter command: ");
        userChoice = scnr.next();
        userChoice = userChoice.toUpperCase();
        scnr.nextLine();

        return userChoice;
    }

    private static void select(String user) {
        switch (user) {
            case "N":
                newEmployee();
                break;
            case "C":
                computePaycheck();
                break;
            case "R":
                raiseWages();
                break;
            case "L":
                listEmployees();
                break;
            case "Q":
                break;
            default:
                System.out.println("\nInvalid command. Try again.");
                break;
        }
    }

    private static void newEmployee () {
        // Grab input from user
        // Create employee object
        // Expand array as needed
        String name;
        char hourlyOrSalaried;
        double hourlyWage, annualSalary;


        if (HourlyEmployee.empCount == employees.length) {
            employees = resize();
        }

        System.out.print("Enter name of new employee: ");
        name = scnr.nextLine();

        System.out.print("Hourly (h) or salaried (s): ");
        hourlyOrSalaried = scnr.next().charAt(0);
        scnr.nextLine();

        if (hourlyOrSalaried == 'h' || hourlyOrSalaried == 'H') {
            System.out.print("Enter hourly wage: ");
            hourlyWage = scnr.nextDouble();
            scnr.nextLine();

            HourlyEmployee hourlyEmp = new HourlyEmployee(name, hourlyWage);
            employees[HourlyEmployee.empCount - 1] = hourlyEmp;
        }
        else if (hourlyOrSalaried == 's' || hourlyOrSalaried == 'S') {
            System.out.print("Enter annual salary: ");
            annualSalary = scnr.nextDouble();
            scnr.nextLine();

            SalariedEmployee salariedEmp = new SalariedEmployee(name, annualSalary);
            employees[SalariedEmployee.empCount - 1] = salariedEmp;
        }

    }

    private static void computePaycheck () {
        // Display weekly pay for all employees
        // For hourly employees first grab hours from users
        // for salaried employees assume weekly hours is 40.
        // call computePay method
        double hours;
        for (int i = 0; i < HourlyEmployee.empCount; ++i) {
            System.out.print("Enter number of hours worked per week by "
                    + employees[i].getEmpName() + ": ");
            hours = scnr.nextDouble();
            scnr.nextLine();

            System.out.print("Pay: ");
            System.out.println(employees[i].moneyFormat.format(employees[i].computePay(hours)));
            System.out.println();
        }
    }

    private static void raiseWages () {
        // Grab percentage and raise wages for all employees
        // This means that the hourly pay for
        // each employee will be raised by the inputted percentage
        double perInc;
        System.out.print("Enter percentage increase: ");
        perInc = scnr.nextDouble();
        scnr.nextLine();

        System.out.println("\nName\t\t\t\tNew Wages" +
                "\n-------------\t\t-------------");
        for (int i = 0; i < HourlyEmployee.empCount; ++i) {
            employees[i].incrWage(perInc);
            if (employees[i] instanceof HourlyEmployee) {
                System.out.println(employees[i]);
            }
            else if (employees[i] instanceof SalariedEmployee) {
                System.out.print(employees[i].getEmpName() + "\t\t");
                System.out.print(employees[i].moneyFormat.format(40 * 52 * employees[i].getHrlyPay()));
                System.out.println("/year");
            }
        }
    }

    private static void listEmployees () {
        //display information for all employees
        System.out.println("\nName\t\t\t\tHourly Wages" +
                "\n-------------\t\t-------------");
        for (int i = 0; i < HourlyEmployee.empCount; ++i) {
            if (employees[i] instanceof HourlyEmployee) {
                System.out.println(employees[i]);
            }
            else if (employees[i] instanceof SalariedEmployee) {
                System.out.print(employees[i].getEmpName() + "\t\t");
                System.out.print(employees[i].moneyFormat.format(employees[i].getHrlyPay()));
                System.out.println("/hour");
            }
        }
    }

    private static Employee[] resize() {
        // Resize array
        // Double the size
        Employee[] temp = new Employee[2 * employees.length];

        System.arraycopy(employees, 0, temp, 0, employees.length);

        employees = temp;

        return employees;
    }
}