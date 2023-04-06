package app.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.dto.request.ReqPlanningPokerSessionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"/reset-db.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class SessionControllerIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  private SimpMessagingTemplate messagingTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @SneakyThrows
  public void newSessionTest(){
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
  public void destroySessionTest(){
    // delete session
    mockMvc.perform(MockMvcRequestBuilders.delete("/sessions/1").
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8"))
        .andExpect(status().isOk());

    // check all data already destroyed
    mockMvc.perform(MockMvcRequestBuilders.delete("/stories/1").
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8"))
        .andExpect(status().isNoContent());
    mockMvc.perform(MockMvcRequestBuilders.delete("/members/1").
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8"))
        .andExpect(status().isNoContent());
  }

}
