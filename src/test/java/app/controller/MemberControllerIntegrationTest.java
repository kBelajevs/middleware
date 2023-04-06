package app.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.dto.request.ReqMemberDTO;
import app.dto.response.ResPlanningPokerSessionDTO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class MemberControllerIntegrationTest extends IntegrationTest {

  @Captor
  ArgumentCaptor<ResPlanningPokerSessionDTO> sessionUpdateCaptor;

  @Test
  @SneakyThrows
  public void addMemberToSessionTest() {

    ReqMemberDTO req = new ReqMemberDTO();
    req.setName("My NickName");

    // Join session
    mockMvc.perform(MockMvcRequestBuilders.post("/members/" + SESSION_ID).
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8").content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isCreated());

    // Check that session data send
    verify(messagingTemplate).convertAndSend(eq("/topic/session/" + SESSION_ID),
        sessionUpdateCaptor.capture());
    var sessionUpdate = sessionUpdateCaptor.getValue();
    assertNotNull(sessionUpdate.getTitle());
    assertFalse(sessionUpdate.getStories().isEmpty());
    assertFalse(sessionUpdate.getMembers().isEmpty());
  }

  @Test
  @SneakyThrows
  public void addMemberToNonExistingSessionTest() {

    ReqMemberDTO req = new ReqMemberDTO();
    req.setName("My NickName");

    // Join session
    mockMvc.perform(MockMvcRequestBuilders.post("/members/" + NON_EXISTING_SESSION_ID).
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8").content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isNotFound());

  }

  @Test
  @SneakyThrows
  public void removeMemberFromSessionTest() {
    mockMvc.perform(MockMvcRequestBuilders.delete("/members/" + SESSION_ID).
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8"))
        .andExpect(status().isOk());
  }

  @Test
  @SneakyThrows
  public void removeMemberFromNonExistingSessionTest() {
    mockMvc.perform(MockMvcRequestBuilders.delete("/members/" + NON_EXISTING_SESSION_ID).
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8"))
        .andExpect(status().isNoContent());
  }
}
