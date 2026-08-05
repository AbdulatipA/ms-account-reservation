package org.example.msaccountreservation.clientExceptions;

import com.example.model.ErrorCode;
import com.example.model.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientAlreadyExistsException.class)
    public ResponseEntity<ResponseCode> handleClientAlreadyExistsException(ClientAlreadyExistsException ex) {
        ResponseCode responseCode = new ResponseCode();
        responseCode.setErrorCode(ErrorCode.CONFLICT);
        responseCode.setErrorDescription(ex.getMessage());
        responseCode.setStatusCode(409);

        log.info("ClientAlreadyExistsException: {}", responseCode);
        return new ResponseEntity<>(responseCode, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ResponseCode> handleClientAlreadyExistsException(ClientNotFoundException ex) {
        ResponseCode responseCode = new ResponseCode();

        responseCode.setErrorCode(ErrorCode.NOT_FOUND);
        responseCode.setErrorDescription(ex.getMessage());
        responseCode.setStatusCode(404);
        log.info("ClientNotFoundException: {}", responseCode);
        return new ResponseEntity<>(responseCode, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ClientInvalidDataException.class)
    public ResponseEntity<ResponseCode> handleClientInvalidDataException(ClientInvalidDataException ex) {
        ResponseCode responseCode = new ResponseCode();

        responseCode.setErrorCode(ErrorCode.BAD_REQUEST);
        responseCode.setErrorDescription(ex.getMessage());
        responseCode.setStatusCode(400);
        log.info("ClientInvalidDataException: {}", responseCode);
        return new ResponseEntity<>(responseCode, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ClientGatewayTimeout.class)
    public ResponseEntity<ResponseCode> handleClientGatewayTimeoutException(ClientGatewayTimeout ex) {
        ResponseCode responseCode = new ResponseCode();

        responseCode.setErrorCode(ErrorCode.GATEWAY_TIMEOUT);
        responseCode.setErrorDescription(ex.getMessage());
        responseCode.setStatusCode(504);
        log.info("ClientGatewayTimeoutException: {}", responseCode);
        return new ResponseEntity<>(responseCode, HttpStatus.GATEWAY_TIMEOUT);
    }
}
