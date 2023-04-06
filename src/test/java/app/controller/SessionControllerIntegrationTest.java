package app.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.dto.request.ReqPlanningPokerSessionDTO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class SessionControllerIntegrationTest extends IntegrationTest {


  @Test
  @SneakyThrows
  public void newSessionTest() {
    var req = new ReqPlanningPokerSessionDTO();
    req.setTitle("Title");
    req.setDeckType("type");

    mockMvc.perform(MockMvcRequestBuilders.post("/sessions").
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8").content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$._links.joinSession").exists())
        .andExpect(jsonPath("$._links.sessionSocket").exists())
        .andExpect(jsonPath("$._links.sessionUpdateTopic").exists())
        .andExpect(jsonPath("$._links.sessionUpdateTopic").exists())
        .andExpect(jsonPath("$._links.voteFinishedTopic").exists());
  }

  @Test
  @SneakyThrows
  public void destroySessionTest() {
    // delete session
    mockMvc.perform(MockMvcRequestBuilders.delete("/sessions/" + SESSION_ID).
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8"))
        .andExpect(status().isOk());

    // check all data already destroyed
    mockMvc.perform(MockMvcRequestBuilders.delete("/stories/" + SESSION_ID).
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8"))
        .andExpect(status().isNoContent());
    mockMvc.perform(MockMvcRequestBuilders.delete("/members/" + SESSION_ID).
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8"))
        .andExpect(status().isNoContent());
  }

}
