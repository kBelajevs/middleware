package app.aspect;

import app.domain.Vote;
import app.dto.response.ResVoteDTO;
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
public class VoteAspect {


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
    var votesDto = modelMapper.map(votes, ResVoteDTO[].class);
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

}
