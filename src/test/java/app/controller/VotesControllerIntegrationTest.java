package app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.aspect.VoteEmit;
import app.dto.request.ReqVoteDTO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Sql(scripts = {"/reset-db.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class VotesControllerIntegrationTest extends IntegrationTest {

  @Captor
  ArgumentCaptor<VoteEmit> voteEmitArgumentCaptor;

  @Test
  @SneakyThrows
  public void voteUserStory() {

    // submit vote
    var vote = new ReqVoteDTO();
    vote.setUserStoryId(VOTING_USER_STORY_ID);
    vote.setMemberId(EXISTING_MEMBER_ID);
    vote.setValue(5);
    mockMvc.perform(MockMvcRequestBuilders.post("/votes").
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8").content(objectMapper.writeValueAsString(vote)))
        .andExpect(status().isCreated());

    verify(messagingTemplate).convertAndSend(eq("/topic/session/" + SESSION_ID + "/vote-emit"),
        voteEmitArgumentCaptor.capture());
    Mockito.reset(messagingTemplate);

    VoteEmit voteEmit = voteEmitArgumentCaptor.getValue();

    assertEquals(1, voteEmit.getTotalVotes());
    assertNotNull(voteEmit.getVoter());

    // submit 2nd vote
    var vote2 = new ReqVoteDTO();
    vote2.setUserStoryId(VOTING_USER_STORY_ID);
    vote2.setMemberId(EXISTING_MEMBER_ID_ANOTHER);
    vote2.setValue(5);
    mockMvc.perform(MockMvcRequestBuilders.post("/votes").
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8").content(objectMapper.writeValueAsString(vote2)))
        .andExpect(status().isCreated());

    verify(messagingTemplate).convertAndSend(eq("/topic/session/" + SESSION_ID + "/vote-emit"),
        voteEmitArgumentCaptor.capture());

    VoteEmit voteEmit2 = voteEmitArgumentCaptor.getValue();

    // check that counter increased
    assertEquals(2, voteEmit2.getTotalVotes());
    assertNotNull(voteEmit2.getVoter());
  }

  @Test
  @SneakyThrows
  public void doubleVoteTest() {
    var vote = new ReqVoteDTO();
    vote.setUserStoryId(VOTING_USER_STORY_ID);
    vote.setMemberId(EXISTING_MEMBER_ID);
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
  public void voteForPendingStory() {
    var vote = new ReqVoteDTO();
    vote.setUserStoryId(PENDING_USER_STORY_ID);
    vote.setMemberId(EXISTING_MEMBER_ID);
    vote.setValue(5);

    mockMvc.perform(MockMvcRequestBuilders.post("/votes").
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8").content(objectMapper.writeValueAsString(vote)))
        .andExpect(status().isForbidden());
  }

  @Test
  @SneakyThrows
  public void voteForVotedStory() {
    var vote = new ReqVoteDTO();
    vote.setUserStoryId(VOTED_USER_STORY_ID);
    vote.setMemberId(EXISTING_MEMBER_ID);
    vote.setValue(5);

    mockMvc.perform(MockMvcRequestBuilders.post("/votes").
            contentType(MediaType.APPLICATION_JSON).
            characterEncoding("UTF-8").content(objectMapper.writeValueAsString(vote)))
        .andExpect(status().isForbidden());
  }
}
