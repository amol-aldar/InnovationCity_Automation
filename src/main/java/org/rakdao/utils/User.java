package org.rakdao.utils;


public class User {
    private String firstName;
    private String lastName;
    private String company;
    private String mobile;
    private String email;

    public User(String firstName, String lastName, String company, String mobile, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
        this.mobile = mobile;
        this.email = email;
    }

    // Getters
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getCompany() { return company; }
    public String getMobile() { return mobile; }
    public String getEmail() { return email; }
}

