package app.aspect;

import app.domain.Vote;
import app.dto.VoteDTO;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class VoteEmittedAspect {


  private final SimpMessagingTemplate messagingTemplate;
  private final ModelMapper modelMapper;

  @AfterReturning(
      pointcut = "execution(* app.service.VoteService.getVotes(..))",
      returning = "result")
  public void afterVoteEmitted(Object result) {
    var votes = (Set<Vote>) result;
    var vote = votes.iterator().next();
    var sessionId = vote.getMember().getSession().getId();
    String topic = String.format("/topic/session/%s/vote-finished", sessionId);
    var votesDto = modelMapper.map(votes, VoteDTO[].class);
    messagingTemplate.convertAndSend(topic, votesDto);
  }

  @AfterReturning(
      pointcut = "execution(* app.service.VoteService.vote(..))",
      returning = "result")
  public void afterVotingFinished(Object result) {
    Vote vote = (Vote) result;
    var totalVotes = vote.getUserStory().getVotes().size();
    var voter = vote.getMember().getName();
    var voteEmit = new VoteEmit(totalVotes, voter);
    String topic = String.format("/topic/session/%s/vote-emit",
        vote.getMember().getSession().getId());
    messagingTemplate.convertAndSend(topic, voteEmit);
  }

  private static class VoteEmit {

    private final int totalVotes;
    private final String voter;

    public VoteEmit(int totalVotes, String voter) {
      this.totalVotes = totalVotes;
      this.voter = voter;
    }

    public int getTotalVotes() {
      return totalVotes;
    }

    public String getVoter() {
      return voter;
    }
  }

}
