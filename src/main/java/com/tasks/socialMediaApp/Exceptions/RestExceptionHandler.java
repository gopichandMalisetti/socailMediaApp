package com.tasks.socialMediaApp.Exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException exception,
                                                                   HttpHeaders headers, HttpStatusCode status, WebRequest request) {

             CustomErrorResponse errorResponse = new CustomErrorResponse();
             errorResponse.setStatus(9);
             errorResponse.setError("Not Found");
             errorResponse.setMessage(exception.getMessage());

             return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<String> handleInternalServerException(InternalServerException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handlePostNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(AccessException.class)
    public ResponseEntity<String> handleCommentAccessException(AccessException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ActionDoneBeforeException.class)
    public ResponseEntity<String> handleOperationAlreadyCompletedException(ActionDoneBeforeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}
