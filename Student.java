/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.milestone;

/**
 *
 * @author Owdel
 */
public class Student {
   private final String username;
    private final String firstName;
    private final String lastName;
    private final String number;
    private final String email;
    private final String className;
    private final String gradeLvl;
 
    private Student(Builder builder) {
        this.username  = builder.username;
        this.firstName = builder.firstName;
        this.lastName  = builder.lastName;
        this.number    = builder.number;
        this.email     = builder.email;
        this.className = builder.className;
        this.gradeLvl  = builder.gradeLvl;
    }
 
    public String getUsername()   { return username; }
    public String getFirstName()  { return firstName; }
    public String getLastName()   { return lastName; }
    public String getNumber()     { return number; }
    public String getEmail()      { return email; }
    public String getClassName()  { return className; }
    public String getGradeLvl()   { return gradeLvl; }
 
    // ---------------------------------------------------------------
    // BUILDER
    // ---------------------------------------------------------------
    public static class Builder {
 
        private String username;
        private String firstName;
        private String lastName;
        private String number;
        private String email;
        private String className;
        private String gradeLvl;
 
        public Builder username(String username) {
            this.username = username;
            return this;
        }
 
        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }
 
        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
 
        public Builder number(String number) {
            this.number = number;
            return this;
        }
 
        public Builder email(String email) {
            this.email = email;
            return this;
        }
 
        public Builder className(String className) {
            this.className = className;
            return this;
        }
 
        public Builder gradeLvl(String gradeLvl) {
            this.gradeLvl = gradeLvl;
            return this;
        }
 
        public Student build() {
            if (username  == null || username.isEmpty())   throw new IllegalStateException("Username cannot be empty.");
            if (firstName == null || firstName.isEmpty())  throw new IllegalStateException("First name cannot be empty.");
            if (lastName  == null || lastName.isEmpty())   throw new IllegalStateException("Last name cannot be empty.");
            if (number    == null || number.isEmpty())     throw new IllegalStateException("Number cannot be empty.");
            if (email     == null || email.isEmpty())      throw new IllegalStateException("Email cannot be empty.");
            if (className == null || className.isEmpty())  throw new IllegalStateException("Class cannot be empty.");
            if (gradeLvl  == null || gradeLvl.isEmpty())   throw new IllegalStateException("Grade level cannot be empty.");
            return new Student(this);






        }




    }




}






