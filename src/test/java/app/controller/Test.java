package app.controller;

import java.util.Optional;

public class Test {

  @org.junit.jupiter.api.Test
  public void test(){

    Optional<String> optionalValue = Optional.of("test");

    optionalValue.orElseThrow(() -> new RuntimeException("Value is present!"));

  }

}
