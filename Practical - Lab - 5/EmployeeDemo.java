abstract class Employee {
    String name;
    int age;
    abstract double monthlySalary();
    Employee(String n, int a) {
        name = n;
        age = a;
    }
}

class FullTime extends Employee{
    double fixed;
    FullTime(String name , int age , double fixed) {
        super(name,age);
        this.fixed = fixed;
    }
    double monthlySalary() {
        return fixed;
    }
}

class PartTime extends Employee{
    int hours;
    double rate;
    PartTime(String name, int age, int hours, double rate) {
        super(name,age);
        this.hours = hours;
        this.rate = rate;
    }
    double monthlySalary() {
        return hours * rate;
    }
}

class Intern extends Employee{
    double stipend;
    Intern(String name , int age,double stipend) {
        super(name,age);
        this.stipend = stipend;
    }
    double monthlySalary() {
        return stipend;
    }
}

public class EmployeeDemo {
    public static void main(String[] args) {
        Employee []employees = {new FullTime("Arshit", 17, 100000),
                new PartTime("Pathu", 19, 5, 5000),
                new Intern("Jonty", 17, 20000)};
        double total=0;
        for (Employee emp : employees) {
            double salary = emp.monthlySalary();
            System.out.println(emp.name + ":" + salary);
            total += salary;
            if(emp instanceof Intern)
                System.out.println("Note: This Employee is Intern");
        }
        System.out.println("Total Salary:- " + total);
    }

}
