/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author Owdel
 */
    
public class FinanceException extends RuntimeException {
 
    private final String context;
 
    public FinanceException(String message) {
        super(message);
        this.context = "General";
    }
 
    public FinanceException(String message, String context) {
        super(message);
        this.context = context;
    }
 
    public String getContext() {
        return context;
    }
 
    @Override
    public String toString() {
        return "[FinanceException - " + context + "] " + getMessage();
    }
}

 
