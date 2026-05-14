/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.milestone;

/**
 *
 * @author Owdel
 */
public class Kitchen {
 
    private final int    kitchenId;
    private final int    section;
    private final String availability;
    private final String scheduleStart;
    private final String scheduleEnd;
    private final double price;
    private final String username;
 
    private Kitchen(Builder builder) {
        this.kitchenId     = builder.kitchenId;
        this.section       = builder.section;
        this.availability  = builder.availability;
        this.scheduleStart = builder.scheduleStart;
        this.scheduleEnd   = builder.scheduleEnd;
        this.price         = builder.price;
        this.username      = builder.username;
    }
 
    public int    getKitchenId()     { return kitchenId; }
    public int    getSection()       { return section; }
    public String getAvailability()  { return availability; }
    public String getScheduleStart() { return scheduleStart; }
    public String getScheduleEnd()   { return scheduleEnd; }
    public double getPrice()         { return price; }
    public String getUsername()      { return username; }
 
    public static class Builder {
 
        private int    kitchenId;
        private int    section;
        private String availability = "Occupied";
        private String scheduleStart;
        private String scheduleEnd;
        private double price;
        private String username;
 
        public Builder kitchenId(int kitchenId) {
            this.kitchenId = kitchenId;
            return this;
        }
 
        public Builder section(int section) {
            this.section = section;
            return this;
        }
 
        public Builder availability(String availability) {
            this.availability = availability;
            return this;
        }
 
        public Builder scheduleStart(String scheduleStart) {
            this.scheduleStart = scheduleStart;
            return this;
        }
 
        public Builder scheduleEnd(String scheduleEnd) {
            this.scheduleEnd = scheduleEnd;
            return this;
        }
 
        public Builder price(double price) {
            this.price = price;
            return this;
        }
 
        public Builder username(String username) {
            this.username = username;
            return this;
        }
 
        public Kitchen build() {
            if (kitchenId < 1 || kitchenId > 4)
                throw new IllegalStateException("Kitchen ID must be between 1 and 4.");
            if (section < 1 || section > 8)
                throw new IllegalStateException("Section must be between 1 and 8.");
            if (scheduleStart == null || scheduleStart.isEmpty())
                throw new IllegalStateException("Schedule start cannot be empty.");
            if (scheduleEnd == null || scheduleEnd.isEmpty())
                throw new IllegalStateException("Schedule end cannot be empty.");
            if (price <= 0)
                throw new FinanceException("Price must be greater than zero.", "Kitchen.Builder");
            if (username == null || username.isEmpty())
                throw new IllegalStateException("Username cannot be empty.");
            return new Kitchen(this);



            
        }




    }


    
}
 





