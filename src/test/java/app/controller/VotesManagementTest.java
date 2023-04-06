package app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.aspect.VoteEmit;
import app.domain.UserStoryStatus;
import app.dto.request.ReqUserStoryDTO;
import app.dto.request.ReqVoteDTO;
import app.dto.response.ResUserStoryDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
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
public class VotesManagementTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  private SimpMessagingTemplate messagingTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Captor
  ArgumentCaptor<VoteEmit> voteEmitArgumentCaptor;

  @Test
  @SneakyThrows
  public void voteUserStory(){

    // add Pending story to existing session 1
    ReqUserStoryDTO storyReq = new ReqUserStoryDTO();
    storyReq.setStatus(UserStoryStatus.PENDING);
    storyReq.setDescription("Description");
    var pendingStory = mockMvc.perform(MockMvcRequestBuilders.post("/stories/1").
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8").content(objectMapper.writeValueAsString(storyReq)))
        .andExpect(status().isCreated()).andReturn();
    String pendingStoryResult = pendingStory.getResponse().getContentAsString();
    var pendingStoryRes = objectMapper.readValue(pendingStoryResult, ResUserStoryDTO.class);
    int pendingStoryId = pendingStoryRes.getId();

    // change status to Voting
    storyReq.setStatus(UserStoryStatus.VOTING);
    mockMvc.perform(MockMvcRequestBuilders.put("/stories/"+pendingStoryId).
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8").content(objectMapper.writeValueAsString(storyReq)))
        .andExpect(status().isOk()).
        andExpect(jsonPath("$.status").value("VOTING"));

    // vote submitted
    var vote = new ReqVoteDTO();
    vote.setUserStoryId(pendingStoryId);
    vote.setMemberId(1);
    vote.setValue(5);
    mockMvc.perform(MockMvcRequestBuilders.post("/votes").
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8").content(objectMapper.writeValueAsString(vote)))
        .andExpect(status().isCreated());

    verify(messagingTemplate).convertAndSend(eq("/topic/session/1/vote-emit"), voteEmitArgumentCaptor.capture());
    Mockito.reset(messagingTemplate);

    VoteEmit voteEmit = voteEmitArgumentCaptor.getValue();

    assertEquals(1, voteEmit.getTotalVotes());
    assertNotNull( voteEmit.getVoter());

    // submit 2nd vote
    var vote2 = new ReqVoteDTO();
    vote2.setUserStoryId(pendingStoryId);
    vote2.setMemberId(2);
    vote2.setValue(5);
    mockMvc.perform(MockMvcRequestBuilders.post("/votes").
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8").content(objectMapper.writeValueAsString(vote2)))
        .andExpect(status().isCreated());

    verify(messagingTemplate).convertAndSend(eq("/topic/session/1/vote-emit"), voteEmitArgumentCaptor.capture());

    VoteEmit voteEmit2 = voteEmitArgumentCaptor.getValue();

    // check that counter increased
    assertEquals(2, voteEmit2.getTotalVotes());
    assertNotNull( voteEmit2.getVoter());
  }

}
