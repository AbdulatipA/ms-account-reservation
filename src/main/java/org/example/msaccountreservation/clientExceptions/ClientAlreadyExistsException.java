package org.example.msaccountreservation.clientExceptions;

public class ClientAlreadyExistsException extends RuntimeException{
    public ClientAlreadyExistsException(String message) {
        super(message);
    }
}
