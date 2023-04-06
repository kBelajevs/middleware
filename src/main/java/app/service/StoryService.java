package app.service;

import app.aspect.PlaningPokerSessionUpdate;
import app.domain.UserStory;
import app.domain.UserStoryStatus;
import app.exception.ActionForbiddenException;
import app.exception.NoContentException;
import app.exception.NotFoundException;
import app.repository.UserStoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoryService {

  private final PokerPlanningSessionService sessionService;
  private final UserStoryRepository storyRepository;

  public UserStory addStory(UserStory userStory, Integer sessionId) {
    var session = sessionService.getSessionByIdOrThrow(sessionId);
    userStory.setSession(session);
    storyRepository.save(userStory);
    return userStory;
  }

  public UserStory getStoryById(Integer id){
    return storyRepository.findById(id).orElseThrow(() -> new NotFoundException("Story not found"));
  }

  @PlaningPokerSessionUpdate
  public UserStory removeStory(Integer storyId) {
    var storyToDelete = storyRepository.findById(storyId)
        .orElseThrow(() -> new NoContentException("Story to delete not found"));
    if (!UserStoryStatus.PENDING.equals(storyToDelete.getStatus())) {
      storyRepository.delete(storyToDelete);
      return storyToDelete;
    } else {
      throw new ActionForbiddenException("Only PENDING stories allowed to be deleted");
    }
  }

  @PlaningPokerSessionUpdate
  public UserStory updateStory(UserStory updatedUserStory) {
    var storyToUpdate = storyRepository.findById(updatedUserStory.getId())
        .orElseThrow(() -> new NotFoundException("Story to update not found"));
    storyToUpdate.setDescription(updatedUserStory.getDescription());
    storyToUpdate.setStatus(updatedUserStory.getStatus());
    storyToUpdate.setStoryRef(updatedUserStory.getStoryRef());
    storyRepository.save(storyToUpdate);
    return storyToUpdate;
  }

}
