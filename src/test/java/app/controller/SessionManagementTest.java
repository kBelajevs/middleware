package app.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.domain.UserStoryStatus;
import app.dto.request.ReqMemberDTO;
import app.dto.request.ReqPlanningPokerSessionDTO;
import app.dto.request.ReqUserStoryDTO;
import app.dto.response.ResMemberDTO;
import app.dto.response.ResPlanningPokerSessionDTO;
import app.dto.response.ResUserStoryDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionManagementTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  private SimpMessagingTemplate messagingTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Captor
  ArgumentCaptor<ResPlanningPokerSessionDTO> sessionUpdateCaptor;

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
  public void enterSessionTest(){

    ReqMemberDTO req = new ReqMemberDTO();
    req.setName("My NickName");

    // Join session
    mockMvc.perform(MockMvcRequestBuilders.post("/members/1").
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8").content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isCreated());

    // Check that session data send
    verify(messagingTemplate).convertAndSend(eq("/topic/session/1"), sessionUpdateCaptor.capture());
    var sessionUpdate = sessionUpdateCaptor.getValue();
    assertNotNull(sessionUpdate.getTitle());
    assertFalse(sessionUpdate.getStories().isEmpty());
    assertFalse(sessionUpdate.getMembers().isEmpty());
  }

  @Test
  @SneakyThrows
  public void destroySessionTest(){

    // create session
    var sessionReq = new ReqPlanningPokerSessionDTO();
    sessionReq.setTitle("Title");
    sessionReq.setDeckType("type");
    var createdSessionResult = mockMvc.perform(MockMvcRequestBuilders.post("/sessions").
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8").content(objectMapper.writeValueAsString(sessionReq)))
        .andExpect(status().isCreated()).andReturn();

    String sessionCreatedResult = createdSessionResult.getResponse().getContentAsString();
    var resultDto = objectMapper.readValue(sessionCreatedResult, ResPlanningPokerSessionDTO.class);
    int sessionId = resultDto.getSessionId();

    // add member
    ReqMemberDTO memberReq = new ReqMemberDTO();
    memberReq.setName("BOB");
    var memberAddResponse = mockMvc.perform(MockMvcRequestBuilders.post("/members/"+sessionId).
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8").content(objectMapper.writeValueAsString(memberReq)))
        .andExpect(status().isCreated()).andReturn();
    String memberResult = memberAddResponse.getResponse().getContentAsString();
    var memberResultDto = objectMapper.readValue(memberResult, ResMemberDTO.class);
    int memeberId = memberResultDto.getMemberId();

    // add story
    ReqUserStoryDTO storyReq = new ReqUserStoryDTO();
    storyReq.setStatus(UserStoryStatus.PENDING);
    storyReq.setDescription("Description");
    var storyAddResponse = mockMvc.perform(MockMvcRequestBuilders.post("/stories/"+sessionId).
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8").content(objectMapper.writeValueAsString(storyReq)))
        .andExpect(status().isCreated()).andReturn();
    String storyResult = storyAddResponse.getResponse().getContentAsString();
    var storyResultDto = objectMapper.readValue(storyResult, ResUserStoryDTO.class);
    int storyId = storyResultDto.getId();

    // delete session
    mockMvc.perform(MockMvcRequestBuilders.delete("/sessions/"+sessionId).
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8").content(objectMapper.writeValueAsString(sessionReq)))
        .andExpect(status().isOk());

    // check all data already destroyed
    mockMvc.perform(MockMvcRequestBuilders.delete("/stories/"+storyId).
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8"))
        .andExpect(status().isNoContent());
    mockMvc.perform(MockMvcRequestBuilders.delete("/members/"+memeberId).
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8"))
        .andExpect(status().isNoContent());
  }

}
