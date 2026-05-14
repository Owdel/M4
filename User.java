
package com.mycompany.milestone;


public class User {
 private final String username;
    private final String password;
 
    // Private constructor — only the Builder can create a User
    private User(Builder builder) {
        this.username = builder.username;
        this.password = builder.password;
    }
 
    // Only getters — no setters, fields can't be changed after creation
    public String getUsername() { return username; }
    public String getPassword() { return password; }
 
    // ---------------------------------------------------------------
    // BUILDER
    // ---------------------------------------------------------------
    public static class Builder {
 
        private String username;
        private String password;
 
        public Builder username(String username) {
            this.username = username;
            return this;
        }
 
        public Builder password(String password) {
            this.password = password;
            return this;
        }
 
        public User build() {
            if (username == null || username.isEmpty()) {
                throw new IllegalStateException("Username cannot be empty.");
            }
            if (password == null || password.isEmpty()) {
                throw new IllegalStateException("Password cannot be empty.");
            }
            return new User(this);




            
        }




    }




}
