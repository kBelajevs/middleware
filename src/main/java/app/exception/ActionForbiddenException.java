package app.exception;

public class ActionForbiddenException extends RuntimeException {

  public ActionForbiddenException(String message) {
    super(message);
  }
}
