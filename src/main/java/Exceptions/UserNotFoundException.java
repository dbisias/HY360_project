package Exceptions;

public class UserNotFoundException extends Exception{
    public UserNotFoundException() {
        super("This Account Was Not Found In The Database");
    }

}
