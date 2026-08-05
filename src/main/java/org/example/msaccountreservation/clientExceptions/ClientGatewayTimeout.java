package org.example.msaccountreservation.clientExceptions;

public class ClientGatewayTimeout extends RuntimeException {
    public ClientGatewayTimeout(String message) {
        super(message);
    }
}
