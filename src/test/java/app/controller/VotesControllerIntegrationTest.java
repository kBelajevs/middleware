package app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.aspect.VoteEmit;
import app.dto.request.ReqVoteDTO;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"/reset-db.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class VotesControllerIntegrationTest {

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

    // submit vote
    var vote = new ReqVoteDTO();
    vote.setUserStoryId(2);
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
    vote2.setUserStoryId(2);
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

  @Test
  @SneakyThrows
  public void doubleVoteTest(){
    var vote = new ReqVoteDTO();
    vote.setUserStoryId(2);
    vote.setMemberId(1);
    vote.setValue(5);

    mockMvc.perform(MockMvcRequestBuilders.post("/votes").
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8").content(objectMapper.writeValueAsString(vote)))
        .andExpect(status().isCreated());

    mockMvc.perform(MockMvcRequestBuilders.post("/votes").
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8").content(objectMapper.writeValueAsString(vote)))
        .andExpect(status().isForbidden());

  }

  @Test
  @SneakyThrows
  public void voteForPendingStory(){
    var vote = new ReqVoteDTO();
    vote.setUserStoryId(1);
    vote.setMemberId(1);
    vote.setValue(5);

    mockMvc.perform(MockMvcRequestBuilders.post("/votes").
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8").content(objectMapper.writeValueAsString(vote)))
        .andExpect(status().isForbidden());
  }

  @Test
  @SneakyThrows
  public void voteForVotedStory(){
    var vote = new ReqVoteDTO();
    vote.setUserStoryId(3);
    vote.setMemberId(1);
    vote.setValue(5);

    mockMvc.perform(MockMvcRequestBuilders.post("/votes").
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8").content(objectMapper.writeValueAsString(vote)))
        .andExpect(status().isForbidden());
  }
}
