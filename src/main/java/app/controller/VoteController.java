package app.controller;

import app.dto.request.ReqVoteDTO;
import app.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VoteController {

  private final VoteService voteService;

  @PostMapping("/votes")
  @ResponseStatus(HttpStatus.CREATED)
  public void vote(@RequestBody ReqVoteDTO reqVoteDTO) {
    voteService.vote(reqVoteDTO.getMemberId(), reqVoteDTO.getUserStoryId(), reqVoteDTO.getValue());
  }
}
