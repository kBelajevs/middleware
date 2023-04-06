package app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public final ResponseEntity<Object> handleNotFoundException(NotFoundException ex){
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(NoContentException.class)
  public final ResponseEntity<Object> handleNoContentException(NotFoundException ex){
    return ResponseEntity.noContent().build();
  }

  @ExceptionHandler(ActionForbiddenException.class)
  public final ResponseEntity<Object> handleActionForbiddenException(ActionForbiddenException ex){
    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
  }


}
