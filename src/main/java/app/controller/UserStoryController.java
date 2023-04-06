package app.controller;

import app.domain.UserStory;
import app.dto.UserStoryDTO;
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
  public UserStoryDTO createUserStory(@PathVariable Integer sessionId,
      @RequestBody UserStoryDTO userStoryDTO) {
    var story = modelMapper.map(userStoryDTO, UserStory.class);
    var savedStory = storyService.addStory(story, sessionId);
    return modelMapper.map(savedStory, UserStoryDTO.class);
  }

  @DeleteMapping("/stories/{idUserStory}")
  public void deleteUserStory(@PathVariable Integer storyId) {
    storyService.removeStory(storyId);
  }

  @PutMapping("/stories/{id}")
  public UserStoryDTO updateUserStory(@PathVariable Integer id, @RequestBody UserStoryDTO userStoryDTO) {
    userStoryDTO.setId(id);
    var storyToUpdate = modelMapper.map(userStoryDTO, UserStory.class);
    var updatedStory = storyService.updateStory(storyToUpdate);
    return modelMapper.map(updatedStory, UserStoryDTO.class);
  }

}
