package app.controller;

import app.domain.UserStory;
import app.dto.request.ReqUserStoryDTO;
import app.dto.response.ResUserStoryDTO;
import app.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserStoryController {

  private final ModelMapper modelMapper;
  private final StoryService storyService;

  @PostMapping("/stories/{sessionId}")
  @ResponseStatus(HttpStatus.CREATED)
  public ResUserStoryDTO createUserStory(@PathVariable Integer sessionId,
      @RequestBody ReqUserStoryDTO reqUserStoryDTO) {
    var story = modelMapper.map(reqUserStoryDTO, UserStory.class);
    var savedStory = storyService.addStory(story, sessionId);
    return modelMapper.map(savedStory, ResUserStoryDTO.class);
  }

  @DeleteMapping("/stories/{storyId}")
  public void deleteUserStory(@PathVariable Integer storyId) {
    storyService.removeStory(storyId);
  }

  @PutMapping("/stories/open-voting/{storyId}")
  public ReqUserStoryDTO starVotingForStory(@PathVariable Integer storyId) {
    var updatedStory = storyService.openVoting(storyId);
    return modelMapper.map(updatedStory, ReqUserStoryDTO.class);
  }

  @PutMapping("/stories/close-voting/{storyId}")
  public ReqUserStoryDTO finishVotingForStory(@PathVariable Integer storyId) {
    var updatedStory = storyService.closeVoting(storyId);
    return modelMapper.map(updatedStory, ReqUserStoryDTO.class);
  }
}
