package org.example.msaccountreservation.clientExceptions;

import com.example.model.ErrorCode;
import com.example.model.ResponseCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientAlreadyExistsException.class)
    public ResponseEntity<ResponseCode> handleClientAlreadyExistsException(ClientAlreadyExistsException ex) {
        ResponseCode responseCode = new ResponseCode();

        responseCode.setErrorCode(ErrorCode.CONFLICT);
        responseCode.setErrorDescription(ex.getMessage());
        responseCode.setStatusCode(409);
        return new ResponseEntity<>(responseCode, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ResponseCode> handleClientAlreadyExistsException(ClientNotFoundException ex) {
        ResponseCode responseCode = new ResponseCode();

        responseCode.setErrorCode(ErrorCode.NOT_FOUND);
        responseCode.setErrorDescription(ex.getMessage());
        responseCode.setStatusCode(404);
        return new ResponseEntity<>(responseCode, HttpStatus.NOT_FOUND);
    }

//    @ExceptionHandler(ClientInvalidDataException.class)
//    public ResponseEntity<ResponseCode> handleClientInvalidDataException(ClientInvalidDataException ex) {
//
//        ResponseCode responseCode = new ResponseCode();
//
//        responseCode.setErrorCode(ErrorCode.BAD_REQUEST);
//        responseCode.setErrorDescription(ex.getMessage());
//        responseCode.setStatusCode(400);
//        return new ResponseEntity<>(responseCode, HttpStatus.BAD_REQUEST);
//    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseCode> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Собираем понятное описание, какое именно поле нарушило правила
        StringBuilder errorDescription = new StringBuilder("Невалидные данные");
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errorDescription.append(error.getField()).append(" (").append(error.getDefaultMessage()).append("); ")
        );

        ResponseCode responseCode = new ResponseCode();
        responseCode.setErrorCode(ErrorCode.BAD_REQUEST);
        responseCode.setErrorDescription(errorDescription.toString());
        responseCode.setStatusCode(400);

        return new ResponseEntity<>(responseCode, HttpStatus.BAD_REQUEST);
    }
}
