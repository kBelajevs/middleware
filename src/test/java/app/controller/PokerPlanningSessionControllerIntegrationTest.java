package app.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class PokerPlanningSessionControllerIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  @SneakyThrows
  public void testGetSession(){
    mockMvc.perform(MockMvcRequestBuilders.get("/sessions").
        contentType(MediaType.APPLICATION_JSON).
        characterEncoding("UTF-8")).andExpect(status().isOk()).andDo(print());
  }

}
