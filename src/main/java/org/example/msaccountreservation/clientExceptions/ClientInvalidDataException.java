package org.example.msaccountreservation.clientExceptions;

public class ClientInvalidDataException extends RuntimeException{
    public ClientInvalidDataException(String message){
        super(message);
    }
}
