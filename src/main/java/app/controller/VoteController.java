package app.controller;

import app.dto.VoteDTO;
import app.service.VoteService;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VoteController {

  private final ModelMapper modelMapper;
  private final VoteService voteService;

  @GetMapping("/votes/{storyId}")
  public List<VoteDTO> getAllVotes(@PathVariable Integer storyId) {
    var votes = voteService.getVotes(storyId);
    return Arrays.asList(modelMapper.map(votes, VoteDTO[].class));
  }

  @PostMapping("/votes")
  @ResponseStatus(HttpStatus.CREATED)
  public void vote(@RequestBody VoteDTO voteDTO) {
    voteService.vote(voteDTO.getMemberId(), voteDTO.getUserStoryId(), voteDTO.getValue());
  }
}
