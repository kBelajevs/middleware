package app.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.dto.request.ReqUserStoryDTO;
import app.dto.response.ResPlanningPokerSessionDTO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class StoryControllerIntegrationTest extends IntegrationTest {

  @Captor
  ArgumentCaptor<ResPlanningPokerSessionDTO> sessionUpdateCaptor;

  @Test
  @SneakyThrows
  public void addStoryToSessionTest() {

    var req = new ReqUserStoryDTO();
    req.setStoryRef("JIRA-5");

    // Join session
    mockMvc.perform(MockMvcRequestBuilders.post("/stories/" + SESSION_ID).
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8").content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isCreated());

    // Check that session data send
    verify(messagingTemplate).convertAndSend(eq("/topic/session/" + SESSION_ID),
        sessionUpdateCaptor.capture());
    var sessionUpdate = sessionUpdateCaptor.getValue();
    assertNotNull(sessionUpdate.getTitle());
    assertFalse(sessionUpdate.getStories().stream().filter(it -> it.getStoryRef().equals("JIRA-5"))
        .findFirst().isEmpty());
    assertFalse(sessionUpdate.getMembers().isEmpty());
  }

  @Test
  @SneakyThrows
  public void addStoryToNonExistingSessionTest() {

    var req = new ReqUserStoryDTO();
    req.setStoryRef("JIRA-5");

    // Join session
    mockMvc.perform(MockMvcRequestBuilders.post("/stories/" + NON_EXISTING_SESSION_ID).
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8").content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isNotFound());

  }

  @Test
  @SneakyThrows
  public void removeStoryFromSessionTest() {
    mockMvc.perform(MockMvcRequestBuilders.delete("/stories/" + SESSION_ID).
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8"))
        .andExpect(status().isOk());
  }

  @Test
  @SneakyThrows
  public void removeStoryFromNonExistingSessionTest() {
    mockMvc.perform(MockMvcRequestBuilders.delete("/stories/" + NON_EXISTING_SESSION_ID).
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8"))
        .andExpect(status().isNoContent());
  }
}
