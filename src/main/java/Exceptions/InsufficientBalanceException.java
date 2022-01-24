package Exceptions;

public class InsufficientBalanceException extends Exception{
    public InsufficientBalanceException() {
        super("The Balance is insufficient");
    }
}
