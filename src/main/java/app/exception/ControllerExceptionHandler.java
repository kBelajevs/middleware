package app.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public final ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
    log.error(ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(ex.getMessage()));
  }

  @ExceptionHandler(NoContentException.class)
  public final ResponseEntity<Object> handleNoContentException(NoContentException ex) {
    log.error(ex.getMessage(), ex);
    return ResponseEntity.noContent().build();
  }

  @ExceptionHandler(ActionForbiddenException.class)
  public final ResponseEntity<Object> handleActionForbiddenException(ActionForbiddenException ex) {
    log.error(ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionResponse(ex.getMessage()));
  }


}
