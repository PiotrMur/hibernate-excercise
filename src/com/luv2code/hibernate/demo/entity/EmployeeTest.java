package com.luv2code.hibernate.demo.entity;

import javax.persistence.*;

@Entity
@Table(name = "employee")
public class EmployeeTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "company")
    private String company;

    public EmployeeTest() {
    }

    public EmployeeTest(Builder builder){
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.company = builder.company;
    }

    public int getId(){
        return id;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCompany() {
        return company;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public static class Builder {

        private String firstName;
        private String lastName;
        private String company;

        public Builder(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public Builder company(String company){
            this.company = company;
            return this;
        }

        public EmployeeTest build() {
            return new EmployeeTest(this);
        }
    }
}
