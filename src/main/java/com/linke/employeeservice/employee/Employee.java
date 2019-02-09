package com.linke.employeeservice.employee;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.Objects;

@Entity
public class Employee {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String charge;

    @NotNull
    private Long salary;

    private File photo;

    public Employee() {}

    public Employee(String firstName, String lastName, String charge, Long salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.charge = charge;
        this.salary = salary;
    }

    public Employee(Long id, String firstName, String lastName, String charge, Long salary, File photo) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.charge = charge;
        this.salary = salary;
        this.photo = photo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public File getPhoto() {
        return photo;
    }

    public void setPhoto(File photo) {
        this.photo = photo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) &&
                firstName.equals(employee.firstName) &&
                lastName.equals(employee.lastName) &&
                charge.equals(employee.charge) &&
                salary.equals(employee.salary) &&
                Objects.equals(photo, employee.photo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, charge, salary, photo);
    }

}
