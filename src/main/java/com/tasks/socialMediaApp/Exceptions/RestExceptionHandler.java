package com.tasks.socialMediaApp.Exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler{

//    @ExceptionHandler(NoHandlerFoundException.class)
//    public ResponseEntity<?> handleNotFound(NoHandlerFoundException exception, HttpServletRequest request){
//         CustomErrorResponse errorResponse = new CustomErrorResponse();
//         errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
//         errorResponse.setError("Not Found");
//         errorResponse.setMessage(exception.getMessage());
//         errorResponse.setPath(request.getRequestURI());
//
//         return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
//    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

                 CustomErrorResponse errorResponse = new CustomErrorResponse();
         errorResponse.setStatus(9);
         errorResponse.setError("Not Found");
         errorResponse.setMessage(exception.getMessage());
//         errorResponse.setPath(request.getRequestURI());

         return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
